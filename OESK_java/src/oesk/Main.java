package oesk;

import com.mongodb.client.MongoDatabase;
import oesk.mongodb.MongoConnect;
import oesk.mongodb.MongoLoader;
import oesk.sqlite.Connect;
import oesk.sqlite.DatabaseLoader;
import oesk.sqlite.TableCreator;

import java.sql.Connection;
import java.util.ArrayList;

public class Main {

    private static final String[] files = {"50sample","100sample","150sample","250sample","500sample"};

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            System.out.println("SQLIte - " + i);
            ArrayList<Measurement> sqliteResult = testSQLite();
            for (Measurement m : sqliteResult) {
                System.out.println(m.toString());
            }
            System.out.println("Mongo - " + i);
            ArrayList<Measurement> mongoResult = testMongo();
            for (Measurement m : mongoResult) {
                System.out.println(m.toString());
            }
        }
    }

    public static ArrayList<Measurement> testSQLite() {
        ArrayList<Measurement> sqliteMeasuremnet = new ArrayList<>();
        Connection connection = Connect.getInstance();
        for (String file : files) {
            Measurement measurement = new Measurement(file);
            sqliteMeasuremnet.add(measurement);
            MeasurementCPUandMemory measurementCPUandMemory = new MeasurementCPUandMemory(measurement);
            measurementCPUandMemory.start();

            long startTime = System.currentTimeMillis();

            TableCreator.createTables(connection);
            DatabaseLoader.load(getPath(file), connection);
            measurementCPUandMemory.doStop();
            long stopTime = System.currentTimeMillis();

            long elapsedTime = (stopTime - startTime)/1000;
            measurement.setTime(elapsedTime);
        }
        return sqliteMeasuremnet;
    }

    public static ArrayList<Measurement> testMongo() {
        ArrayList<Measurement> mongoMeasurements = new ArrayList<>();
        for (String file : files) {
            Measurement measurement = new Measurement(file);
            mongoMeasurements.add(measurement);

            MeasurementCPUandMemory measurementCPUandMemory = new MeasurementCPUandMemory(measurement);
            measurementCPUandMemory.start();

            long startTime = System.currentTimeMillis();

            MongoDatabase database = MongoConnect.createDatabase(file);
            MongoLoader.load(getPath(file), database);
            measurementCPUandMemory.doStop();
            long stopTime = System.currentTimeMillis();
            long elapsedTime = (stopTime - startTime) / 1000;
            measurement.setTime(elapsedTime);
        }
        return mongoMeasurements;
    }

    private static String getPath(String file) {
        return "C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\files\\" + file + ".txt";
    }

}

