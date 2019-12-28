package oesk.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import java.util.List;

public class MongoConnect {
    public static MongoClient mongoClient = new MongoClient();

    public static MongoDatabase createDatabase(String fileName){
        MongoCursor<String> dbsCursor = mongoClient.listDatabaseNames().iterator();
        while(dbsCursor.hasNext()) {
            if(dbsCursor.next().equals("db_java_"+fileName)){
                mongoClient.getDatabase("db_java_"+fileName).drop();
            }
        }
        MongoDatabase database = mongoClient.getDatabase("db_java_"+fileName);
        return database;

    }
}
