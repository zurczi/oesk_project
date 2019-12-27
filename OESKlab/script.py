import sqlite3
from datetime import datetime

def main():
    conn = sqlite3.connect('C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\sqlite\\python.db')
    create_samples_table(conn)
    cursor = conn.cursor()
    read_into_database(cursor, "C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\files\\50sample.txt")
    conn.commit()
    cursor.close()
    conn.close()

def create_samples_table(conn):
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

    print(cursor.execute('select count(*) from dates'))


if __name__ == "__main__":
    main()
