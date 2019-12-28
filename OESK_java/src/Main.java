import com.mongodb.client.MongoDatabase;
import oesk.mongodb.MongoConnect;
import oesk.mongodb.MongoLoader;
import oesk.sqlite.Connect;
import oesk.sqlite.DatabaseLoader;
import oesk.sqlite.TableCreator;

import java.sql.Connection;

public class Main {

    private static final String[] files = {"50sample","100sample","150sample","250sample","500sample"};


    public static void main(String[] args) {
      //testSQLite();
      testMongo();
    }

    public static void testSQLite(){
        Connection connection = Connect.getInstance();
        for (String file: files) {
            TableCreator.createTables(connection);
            System.out.println("Started load database");
            DatabaseLoader.load(getPath(file), connection);
        }
    }

    public static void testMongo(){
        for (String file: files) {
            MongoDatabase database = MongoConnect.createDatabase(files[0]);
            MongoLoader.load(getPath(file), database);
        }
    }

    private static String getPath(String file){
        return "C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\files\\" + file + ".txt" ;
    }

}

