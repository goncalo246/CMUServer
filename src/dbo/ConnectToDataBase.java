package dbo;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Goncalo Pinto on 28/04/2016.
 */
public class ConnectToDataBase {

    private static final String URL = "jdbc:mysql://localhost:3307/cmudb";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection conn = null;


    public Connection startConnectioToDataBase() {

        try {
            Class.forName(DRIVER).newInstance();

            System.out.println("Try Connecting...");
            conn = (Connection) DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected...");

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
