package dbo;

import com.mysql.jdbc.Connection;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Goncalo Pinto on 29/04/2016.
 */
public class Select {

    private Connection conn;
    private String query;

    public Select() {
        conn = new ConnectToDataBase().startConnectioToDataBase();
    }

    public boolean confirmLogin(String email, String password) {
        query = "SELECT * FROM user WHERE email=" + "\"" + email + "\"" +
                "and password=" + "\"" + password + "\"";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = (ResultSet) stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsernameByMail(String email) {
        query = "SELECT * FROM user WHERE email=" + "\"" + email + "\"";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = (ResultSet) stmt.executeQuery();

            String username = "";
            while (rs.next()) {
                username = rs.getString("username");
            }
            return username;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getIndexOfTrajectory(String email) {
        query = "SELECT * FROM trajetoria WHERE user_email=" + "\"" + email + "\"";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = (ResultSet) stmt.executeQuery();

            int new_index = 0;

            while (rs.next()) {
                new_index = rs.getInt("idtragetoria");
            }
            return new_index + 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getIndexOfLocation() {
        query = "SELECT COUNT(*) FROM localizacao";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = (ResultSet) stmt.executeQuery();

            // Get the number of rows from the result set
            rs.next();
            int rowcount = rs.getInt(1);

            return rowcount + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<String> getTrajectories(String email) {
        ArrayList<String> trajectories = new ArrayList<String>();

        query = "SELECT * FROM trajetoria WHERE user_email=" + "\"" + email + "\"";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = (ResultSet) stmt.executeQuery();

            String position = "";

            while (rs.next()) {
                int id = rs.getInt("idtragetoria");
                double distance = rs.getDouble("distancia");
                String time = rs.getString("tempo");

                position = id + ":" + Double.valueOf(distance) + ":" + time;
                trajectories.add(position);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trajectories;
    }

    public String getLocationsOfTrajectory(int id_trajectoria, String email) {

        query = "SELECT * FROM localizacao WHERE user_email=" + "\"" + email + "\"" + " and idtragetoria=" + "\"" + id_trajectoria + "\"" + " order by sequencia ASC";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = (ResultSet) stmt.executeQuery();

            String positions = "";

            while (rs.next()) {
                String latitude = rs.getString("latitude");
                String longitude = rs.getString("longitude");

                positions += ":" + latitude + "/" + longitude;
            }


            return positions;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getDistanceAndTimeByEmail(String email) {
        query = "SELECT * FROM trajetoria WHERE user_email=" + "\"" + email + "\"";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = (ResultSet) stmt.executeQuery();

            double distance = 0.0;
            double time = 0.0;

            while (rs.next()) {
                double dist = rs.getDouble("distancia");
                String ti = rs.getString("tempo");

                distance += dist;
                time += Double.valueOf(ti);
            }


            return distance + ":" + time;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
