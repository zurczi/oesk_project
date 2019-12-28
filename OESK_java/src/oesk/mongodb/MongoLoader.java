package oesk.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class MongoLoader {
    private static Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    public static void load(String file, MongoDatabase database){
        MongoCollection collection_dates = database.getCollection("dates");
        MongoCollection collection_samples = database.getCollection("samples");
        int id = -1;
        int iterator = 0;
        ArrayList<Document> listDates = new ArrayList<>();
        ArrayList<Document> listSamples = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(file))) {
            for( String line : (Iterable<String>) stream::iterator ) {
                String[] data = line.split("<SEP>");
                long date = Long.parseLong(data[2]);
                Date readable = new Date(date*1000);
                cal.setTime(readable);
                listDates.add(toDBObjectDates(cal,id));
                listSamples.add(toDBObjectSamples(data[0], data[1] ,id));
                if(++iterator > 1000) {
                    collection_dates.insertMany(listDates);
                    collection_samples.insertMany(listSamples);
                    listDates.clear();
                    listSamples.clear();
                    iterator=0;
                }

            }
            if(listDates.size() > 0){
                collection_dates.insertMany(listDates);
                collection_samples.insertMany(listSamples);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static final Document toDBObjectDates(Calendar cal, int id){
        id++;
        Document document = new Document();
        document.put("day",cal.get(Calendar.DAY_OF_MONTH));
        document.put("month",cal.get(Calendar.MONTH));
        document.put("year",cal.get(Calendar.YEAR));
        document.put("id", id);
        return document;
    }

    public static final Document toDBObjectSamples(String user, String track, int id ){
        Document document = new Document();
        document.put("user_id",user);
        document.put( "track_id",track);
        document.put( "listen_date",id);
        return document;
    }
}
