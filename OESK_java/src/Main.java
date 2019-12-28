import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import oesk.mongodb.MongoConnect;
import oesk.mongodb.MongoLoader;
import oesk.sqlite.Connect;
import oesk.sqlite.DatabaseLoader;
import oesk.sqlite.TableCreator;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    private static final String[] files = {"50sample","100sample","150sample","250sample","500sample"};


    public static void main(String[] args) {
      //testSQLite();
      testMongo();
    }

    public static void testSQLite(){
        Connection connection = Connect.getInstance();

        TableCreator.createTables(connection);
        System.out.println("Started load database");
        DatabaseLoader.load("C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\files\\" + files[0] + ".txt", connection);
    }

    public static void testMongo(){
        MongoDatabase database = MongoConnect.createDatabase(files[0]);
        MongoLoader.load("C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\files\\" + files[0] + ".txt",database);

    }

}

