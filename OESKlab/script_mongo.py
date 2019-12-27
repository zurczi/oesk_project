from datetime import datetime
import pymongo




def read_into_database(samples, dates, file):



def main():
    myclient = pymongo.MongoClient("mongodb://localhost:27017/")
    fileName = "50sample"
    dblist = myclient.list_database_names()
    if fileName in dblist:
        myclient.drop_database(fileName)
    mydb = myclient[fileName]
    samples = mydb["samples"]
    dates = mydb["dates"]

    read_into_database(samples, dates, "C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\files\\"+fileName+".txt")

if __name__ == "__main__":
    main()