from datetime import datetime
import pymongo
from OESKlab.measure_script import fn_timer
import time
from OESKlab.measure_cpu_and_memory import DisplayCPUAndMemory
from OESKlab.measure_script import Measurement


files = ["50sample", "100sample", "150sample", "250sample", "500sample"]
my_client = pymongo.MongoClient("mongodb://localhost:27017/")
measurement_list = []


def run_one_file(file):
    measurement = Measurement(file)
    measurement_list.append(measurement)

    display_cpu = DisplayCPUAndMemory(measurement)

    display_cpu.start()
    try:
        t0 = time.time()
        db_list = my_client.list_database_names()
        if file in db_list:
            my_client.drop_database(file)
        my_db = my_client[file]
        samples = my_db["samples"]
        dates = my_db["dates"]
        file_path = file_name(file)
        insert_into_db(file_path, samples, dates)
        t1 = time.time()
    finally:
        display_cpu.stop()
        measurement.time = t1-t0


def insert_into_db(file, samples_collection, dates_collection):
    samples = []
    dates = []
    id = 0
    iterator = 0
    with open(file, encoding="ISO-8859-1") as f:
        for line in f:
            samples_data, dates_data = create_json(line, id)
            samples.append(samples_data)
            dates.append(dates_data)
            id += 1
            iterator += 1
            if iterator > 1000:
                #print(id)
                dates_collection.insert_many(dates)
                samples_collection.insert_many(samples)
                samples = []
                dates = []
                iterator = 0
    if iterator > 0:
        dates_collection.insert_many(dates)
        samples_collection.insert_many(samples)


def create_json(line, id):
    data = line.split('<SEP>')
    readable = datetime.fromtimestamp(int(data[2]))
    dates = {"day": readable.strftime("%d"),
                  "month": readable.strftime("%m"),
                  "year": readable.strftime("%y"),
                  "_id": id
                  }

    samples = {"user_id": data[0], "track_id": data[1], "listen_date": id}
    return samples, dates


def main():
    for file in files:
        print(file)
        run_one_file(file)
    my_client.close()
    for x in measurement_list:
        print(str(x))



def file_name(file):
    return "C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\files\\" + file + ".txt"


if __name__ == "__main__":
    main()
