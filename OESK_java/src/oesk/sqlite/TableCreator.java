package oesk.sqlite;

import java.sql.Connection;
import java.sql.Statement;

public final class TableCreator {

    public static void createTables(Connection connection){
        try{
            Statement stmt = connection.createStatement();
            stmt.execute(removeIfExistDates());
            stmt.execute(removeIfExistSamples());
            //connection.commit();
          //  stmt.close();
            //stmt = connection.createStatement();
            stmt.execute(createTableDates());
            stmt.execute(createTableSamples());
            stmt.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static String createTableSamples(){
        return "CREATE TABLE samples (\n" +
                "        user_id varchar(40) NOT NULL,\n" +
                "        track_id varchar(18) NOT NULL,\n" +
                "        listen_date INTEGER ,\n" +
                "        foreign key(listen_date) references dates(id)\n" +
                "    )";
    }

    private static String removeIfExistSamples(){
        return "DROP TABLE IF EXISTS samples";
    }

    private static String removeIfExistDates(){
        return "DROP TABLE IF EXISTS dates";
    }

    private static String createTableDates(){
        return "CREATE TABLE dates (\n" +
                "        ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "                day integer,\n" +
                "                month integer ,\n" +
                "                year integer\n" +
                "    )";
    }

}
