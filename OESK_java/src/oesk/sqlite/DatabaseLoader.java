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

            PreparedStatement pstmtDates = connection.prepareStatement(datesSQL);
            PreparedStatement pstmtSamples = connection.prepareStatement(samplesSQL);

            try (Stream<String> stream = Files.lines(Paths.get(path))) {
                for( String line : (Iterable<String>) stream::iterator ) {
                    String[] data = line.split("<SEP>");
                    long date = Long.parseLong(data[2]);
                    Date readable = new Date(date*1000);
                    cal.setTime(readable);

                    pstmtDates.setInt(1, cal.get(Calendar.DAY_OF_MONTH));
                    pstmtDates.setInt(2, cal.get(Calendar.MONTH));
                    pstmtDates.setInt(3, cal.get(Calendar.YEAR));
                    pstmtDates.addBatch();

                    pstmtSamples.setString(1,data[0]);
                    pstmtSamples.setString(2,data[1]);
                    pstmtSamples.setInt(3,++iterator);
                    pstmtSamples.addBatch();

                    if(batchSize++ > 1000){
                        //System.out.println(iterator);
                        pstmtDates.executeBatch();
                        pstmtSamples.executeBatch();
                        batchSize = 0;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            if (batchSize > 0) {
                pstmtDates.executeBatch();
                pstmtSamples.executeBatch();
            }

            connection.commit();
            pstmtSamples.close();
            pstmtDates.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
