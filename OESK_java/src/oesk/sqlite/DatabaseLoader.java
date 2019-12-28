package oesk.sqlite;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Stream;

public class DatabaseLoader {
    private static final String datesSQL = "INSERT INTO dates (day , month, year) VALUES(?,?,?);";
    private static final String samplesSQL = "INSERT INTO samples (user_id , track_id, listen_date)VALUES(?,?,?);";
    private static Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    public static void load(String path, Connection connection){
        try{
            connection.setAutoCommit(false);

            int batchSize = 0;
            int iterator = 0;

            PreparedStatement pstmt_dates = connection.prepareStatement(datesSQL);
        //    lastrowid = connection.prepareStatement(
          //          "select last_insert_rowid();");
            PreparedStatement pstmt_samples = connection.prepareStatement(samplesSQL);

            try (Stream<String> stream = Files.lines(Paths.get(path))) {
                for( String line : (Iterable<String>) stream::iterator ) {
                    String[] data = line.split("<SEP>");

                    long date = Long.parseLong(data[2]);
                    Date readable = new Date(date*1000);
                    cal.setTime(readable);

                    pstmt_dates.setInt(1, cal.get(Calendar.DAY_OF_MONTH));
                    pstmt_dates.setInt(2, cal.get(Calendar.MONTH));
                    pstmt_dates.setInt(3, cal.get(Calendar.YEAR));

                   // pstmt_dates.execute();
                    pstmt_dates.addBatch();
//                    ResultSet lastrowidResult = lastrowid.executeQuery();

                    pstmt_samples.setString(1,data[0]);
                    pstmt_samples.setString(2,data[1]);
                    pstmt_samples.setInt(3,++iterator);//lastrowidResult.getInt(1));
                   // pstmt_samples.execute();
                    pstmt_samples.addBatch();
                    if(batchSize++ > 1000){ //Execute every 100 rows
                        System.out.println(iterator);
                        pstmt_dates.executeBatch();
                        pstmt_samples.executeBatch();
                        batchSize = 0;
                    }
                    //System.out.println(iterator);//lastrowidResult.getInt(1));


                }
            }catch(Exception e){
                e.printStackTrace();
            }
            if (batchSize > 0) {
                pstmt_dates.executeBatch();
                pstmt_samples.executeBatch();
            }
            connection.commit();
            pstmt_samples.close();
          //  lastrowid.close();
            pstmt_dates.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
