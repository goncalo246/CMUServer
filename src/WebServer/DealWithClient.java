package WebServer;

import WebServer.WebServer;
import com.mysql.jdbc.Connection;
import dbo.ConnectToDataBase;
import dbo.Insert;
import dbo.Select;
import dbo.Update;

import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by Goncalo Pinto on 28/04/2016.
 */
public class DealWithClient extends Thread {

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private WebServer server;
    private Connection conn;

    private Select selectStatement;
    private Insert insertStatement;
    private Update updateStatement;

    private String email = "";
    private String username = "";
    private String password = "";

    public DealWithClient(ObjectOutputStream outputStream, ObjectInputStream inputStream, WebServer server) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.server = server;
        startDB();
    }

    private synchronized void sendMessage(String message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private synchronized void sendArray(ArrayList<String> message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startDB() {
        conn = new ConnectToDataBase().startConnectioToDataBase();
    }

    @Override
    public void run() {
        Object msg = null;
        try {

            while (true) {

                msg = (Object) inputStream.readObject();

                System.out.println("------> " + msg);

                MensagemDoCliente mensagemDoCliente = Utils.gerarMensagemcliente(msg);

                switch (mensagemDoCliente.getTipo()) {

                    case MessagesType.CRIARCONTA:

                        email = mensagemDoCliente.getEmail();
                        username = mensagemDoCliente.getNome();
                        password = mensagemDoCliente.getPassword();

//                        System.out.println("#####################################################################");
//                        System.out.println("Recebi esta mensagem com tipo --> " + mensagemDoCliente.getTipo());
//                        System.out.println("Recebi esta mensagem com nome --> " + mensagemDoCliente.getNome());
//                        System.out.println("Recebi esta mensagem com email --> " + mensagemDoCliente.getEmail());
//                        System.out.println("Recebi esta mensagem com password --> " + mensagemDoCliente.getPassword());

                        insertStatement = new Insert();
                        int success = insertStatement.insertUser(email, username, password);

                        insertStatement.closeConnection();
                        if (success == 0) {
                            sendMessage("criarconta_insucesso");
                        } else {
                            sendMessage("criarconta_sucesso");
                        }


                        break;

                    case MessagesType.LOGIN:

                        email = mensagemDoCliente.getEmail();
                        password = mensagemDoCliente.getPassword();

                        selectStatement = new Select();
                        boolean confirmed = selectStatement.confirmLogin(email, password);

                        selectStatement.closeConnection();

                        String message = "login_" + String.valueOf(confirmed);
                        if (confirmed) {
                            selectStatement = new Select();
                            message += ":" + selectStatement.getUsernameByMail(email);
                            selectStatement.closeConnection();
                        }

                        selectStatement = new Select();

                        String info = selectStatement.getDistanceAndTimeByEmail(email);

                        selectStatement.closeConnection();


                        System.out.println("Vou enviar --> " + message);
                        sendMessage(message + ":" + info);

                        break;

                    case MessagesType.SAVE_TRAJECTORIES:

                        email = mensagemDoCliente.getEmail();
                        String distance = mensagemDoCliente.getDistance();
                        String time = mensagemDoCliente.getTime();
                        String pontos = mensagemDoCliente.getPontos();

                        selectStatement = new Select();
                        int new_index_traj = selectStatement.getIndexOfTrajectory(email);

                        selectStatement.closeConnection();

                        insertStatement = new Insert();
                        int new_traje = insertStatement.insertNewTrajectory(email, new_index_traj, distance, time, pontos);

                        insertStatement.closeConnection();

                        ArrayList<String> locations = mensagemDoCliente.getLocations();
                        System.out.println(locations.toArray().toString());
                        selectStatement = new Select();
                        int new_index_loc = selectStatement.getIndexOfLocation();

                        selectStatement.closeConnection();
                        int i = 0;
                        for (String l : locations) {
                            System.out.println(l);
                            String latitude = l.split("/")[0];
                            String longitude = l.split("/")[1];
                            int sequence = i;
                            insertStatement = new Insert();
                            insertStatement.insertNewLocation(email, new_index_loc, latitude, longitude, sequence, new_index_traj);
                            insertStatement.closeConnection();
                            new_index_loc++;
                            i++;

                        }

                        if (new_traje != 0) {
                            sendMessage(MessagesType.SAVE_TRAJECTORIES_SUCESSO);
                        } else {
                            sendMessage(MessagesType.SAVE_TRAJECTORIES_INSUCESSO);
                        }
                        break;

                    case MessagesType.GET_TRAJECTORIES:

                        email = mensagemDoCliente.getEmail();
                        selectStatement = new Select();
                        ArrayList<String> trajectories = selectStatement.getTrajectories(email);

                        selectStatement.closeConnection();
                        for (int j = 0; j < trajectories.size(); j++) {
                            String[] traj_split = trajectories.get(j).split(":");

                            selectStatement = new Select();
                            String latlong = selectStatement.getLocationsOfTrajectory(Integer.valueOf(traj_split[0]), email);
                            String new_string = trajectories.get(j) + latlong;
                            System.out.println(new_string);
                            trajectories.set(j, new_string);

                            selectStatement.closeConnection();

                        }

                        sendArray(trajectories);

                        break;

                    case MessagesType.GET_USER_INFORMATIONS:

                        email = mensagemDoCliente.getEmail();

                        selectStatement = new Select();

                        sendMessage(selectStatement.getDistanceAndTimeByEmail(email));

                        selectStatement.closeConnection();

                        break;

                    case MessagesType.GET_STATIONS:

                        System.out.println(mensagemDoCliente.getTipo());
                        selectStatement = new Select();

                        ArrayList<String> stations = selectStatement.getStations();
                        System.out.println("" + stations);
                        sendArray(stations);

                        selectStatement.closeConnection();

                        break;

                    case MessagesType.SET_AVAILABILITY_BIKE:

                        updateStatement = new Update();

                        System.out.println("Vou mudar a disponibilidade para ---> " + mensagemDoCliente.isAvailable());
                        updateStatement.setBikeAvailability(mensagemDoCliente.getIdentifier(), mensagemDoCliente.isAvailable());

                        updateStatement.closeConnection();

                        break;

                    case MessagesType.UPDATE_TRAJECTORIES:

                        email = mensagemDoCliente.getEmail();

                        ArrayList<String> locals = new ArrayList<String>();

                        selectStatement = new Select();
                        int new_index_traj2 = selectStatement.getIndexOfTrajectory(email);
                        selectStatement.closeConnection();

                        System.out.println("size--->" + mensagemDoCliente.getData());
                        for (String s : mensagemDoCliente.getData()) {
                            String[] split = s.split(":");

                            String dist = split[0];
                            String tim = split[1];
                            String po = split[2];

                            insertStatement = new Insert();
                            insertStatement.insertNewTrajectory(email, new_index_traj2, dist, tim, po);
                            insertStatement.closeConnection();


                            insertLocationOfTrajectory(new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(split, 3, split.length))), new_index_traj2);

                            new_index_traj2++;

                        }

                        sendMessage("Sucesso");


                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void insertLocationOfTrajectory(ArrayList<String> location, int id_traj) {

        System.out.println("---->Localizaçoes ---> " + location);

        selectStatement = new Select();
        int new_index_loc = selectStatement.getIndexOfLocation();
        selectStatement.closeConnection();

        int seq = 0;
        for (String spl : location) {
            String[] coord = spl.split("/");

            String lat = coord[0];
            String lon = coord[1];

            insertStatement = new Insert();
            insertStatement.insertNewLocation(email, new_index_loc, lat, lon, seq, id_traj);
            insertStatement.closeConnection();

            System.out.println("id--->" + new_index_loc);

            new_index_loc++;
            seq++;
        }

    }

}
