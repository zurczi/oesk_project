from datetime import datetime
import pymongo


def create_json(file):
    samples = []
    dates = []
    id = 0
    with open(file, encoding="ISO-8859-1") as f:
        for line in f:
            data = line.split('<SEP>')
            readable = datetime.fromtimestamp(int(data[2]))
            dates.append({"day": readable.strftime("%d"),
                      "month": readable.strftime("%m"),
                      "year": readable.strftime("%y"),
                      "_id": id
                      })

            samples.append({"user_id": data[0], "track_id": data[1], "listen_date": id})
            id += 1
    print(dates)
    return samples, dates

def main():
    myclient = pymongo.MongoClient("mongodb://localhost:27017/")
    fileName = "50sample"
    dblist = myclient.list_database_names()
    if fileName in dblist:
        myclient.drop_database(fileName)
    mydb = myclient[fileName]
    samples = mydb["samples"]
    dates = mydb["dates"]

    samples_data, dates_data = create_json("C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\files\\" + fileName + ".txt")
    samples.insert_many(samples_data)
    dates.insert_many(dates_data)
    myclient.close()

    
if __name__ == "__main__":
    main()
