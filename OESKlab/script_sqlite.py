from OESKlab.measure_script import fn_timer
import sqlite3
from datetime import datetime
from OESKlab.measure_cpu_and_memory import DisplayCPUAndMemory
from OESKlab.measure_script import Measurement
import time


files = ["50sample", "100sample", "150sample", "250sample", "500sample"]
conn = sqlite3.connect('C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\sqlite\\python.db')
measurement_list = []


def file_name(file):
    return "C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\files\\" + file + ".txt"


#@fn_timer
def run_one_file(path):
    measurement = Measurement(path)
    measurement_list.append(measurement)

    display_cpu = DisplayCPUAndMemory(measurement)

    display_cpu.start()
    try:
        t0 = time.time()
        create_samples_table()
        cursor = conn.cursor()
        read_into_database(cursor, path)
        conn.commit()
        cursor.close()
        t1 = time.time()
    finally:
        display_cpu.stop()
        measurement.time = t1-t0

def create_samples_table():
    conn.execute('DROP TABLE IF EXISTS samples')
    conn.execute('DROP TABLE IF EXISTS dates')
    conn.execute(''' CREATE TABLE dates (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    day integer,
    month integer ,
    year integer
    )''')

    conn.execute(''' CREATE TABLE samples (
    user_id varchar(40) NOT NULL,
    track_id varchar(18) NOT NULL,
    listen_date INTEGER ,
    foreign key(listen_date) references dates(id)
    )''')


def read_into_database(cursor, file_name):
    with open(file_name, encoding="ISO-8859-1") as f:
        for line in f:
            data = line.split('<SEP>')
            readable = datetime.fromtimestamp(int(data[2]))
            cursor.execute('INSERT INTO dates (day , month, year) VALUES(?,?,?);',
                           (readable.strftime("%d"), readable.strftime("%m"), readable.strftime("%y")))
            date_id = cursor.lastrowid
            cursor.execute('INSERT INTO samples (user_id , track_id, listen_date)VALUES(?,?,?);',
                           (data[0], data[1], date_id))

    # print(cursor.execute('select count(*) from dates'))


def main():
    jdjd = [0,1]
    for x in jdjd:
        print("Obliczenie nr : " + str(x))
        for file in files:
            run_one_file(file_name(file))
        #conn.close()
    for x in measurement_list:
        print(str(x))
    conn.close()

if __name__ == "__main__":
    main()
