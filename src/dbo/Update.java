package dbo;

import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Goncalo Pinto on 12/05/2016.
 */
public class Update {

    private Connection conn;
    private String query;

    public Update() {
        conn = new ConnectToDataBase().startConnectioToDataBase();
    }

    public void setBikeAvailability(String identifier, boolean isAvailable) {

        try {
            String query = "update bicicleta set disponibilidade = ? where idbicicleta = ?";

            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setString(1, "" + isAvailable);
            preparedStmt.setString(2, identifier);

            preparedStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
