package dbo;

import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Goncalo Pinto on 29/04/2016.
 */
public class Insert {

    private Connection conn;

    public Insert() {
        conn = new ConnectToDataBase().startConnectioToDataBase();
    }

    public int insertUser(String email, String user, String pass) {
        try {
            PreparedStatement pStmt = conn.prepareStatement("insert into user(username,email,password) values (?, ?, ?)");

            pStmt.setString(1, user);
            pStmt.setString(2, email);
            pStmt.setString(3, pass);


            return pStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int insertNewTrajectory(String email, int index, String distance, String time) {
        try {
            PreparedStatement pStmt = conn.prepareStatement("insert into trajetoria(idtragetoria,user_email,tempo,distancia) values (?, ?, ?, ?)");

            pStmt.setInt(1, index);
            pStmt.setString(2, email);
            pStmt.setString(3, time);
            pStmt.setDouble(4, Double.valueOf(distance));

            return pStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int insertNewLocation(String email, int index, String latitude, String longitude, int sequ, int id_traj) {
        try {
            PreparedStatement pStmt = conn.prepareStatement("insert into localizacao(idlocalizacao,latitude,longitude,sequencia,idtragetoria,user_email) values (?, ?, ?, ?, ?, ?)");

            pStmt.setInt(1, index);
            pStmt.setString(2, latitude);
            pStmt.setString(3, longitude);
            pStmt.setInt(4, sequ);
            pStmt.setInt(5, id_traj);
            pStmt.setString(6, email);

            return pStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

}
