package oesk.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Connect {
    //private static Connection connection = connect();

    public static Connection getInstance(){
        return connect();
    }

    private static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:C:\\Users\\48783\\Desktop\\mgr1\\OESKlab\\sqlite\\java.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void disconnect(Connection connection){
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
