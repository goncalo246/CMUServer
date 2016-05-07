package WebServer;

import WebServer.WebServer;
import com.mysql.jdbc.Connection;
import dbo.ConnectToDataBase;
import dbo.Insert;
import dbo.Select;

import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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
        String msg = null;
        try {

            while (true) {

                msg = (String) inputStream.readObject();

                MensagemDoCliente mensagemDoCliente = Utils.gerarMensagemcliente(msg);

                switch (mensagemDoCliente.getTipo()) {

                    case MessagesType.CRIARCONTA:

                        email = mensagemDoCliente.getEmail();
                        username = mensagemDoCliente.getNome();
                        password = mensagemDoCliente.getPassword();

                        System.out.println("#####################################################################");
                        System.out.println("Recebi esta mensagem com tipo --> " + mensagemDoCliente.getTipo());
                        System.out.println("Recebi esta mensagem com nome --> " + mensagemDoCliente.getNome());
                        System.out.println("Recebi esta mensagem com email --> " + mensagemDoCliente.getEmail());
                        System.out.println("Recebi esta mensagem com password --> " + mensagemDoCliente.getPassword());

                        insertStatement = new Insert();
                        int success = insertStatement.insertUser(email, username, password);

                        if (success == 0) {
                            sendMessage("criarconta_insucesso");
                        } else {
                            sendMessage("criarconta_sucesso");
                        }


                        break;

                    case MessagesType.LOGIN:

                        email = mensagemDoCliente.getEmail();
                        password = mensagemDoCliente.getPassword();

                        System.out.println("#####################################################################");
                        System.out.println("Recebi esta mensagem com tipo --> " + mensagemDoCliente.getTipo());
                        System.out.println("Recebi esta mensagem com email --> " + mensagemDoCliente.getEmail());
                        System.out.println("Recebi esta mensagem com password --> " + mensagemDoCliente.getPassword());

                        selectStatement = new Select();
                        boolean confirmed = selectStatement.confirmLogin(email, password);

                        String message = "login_" + String.valueOf(confirmed);

                        sendMessage(message);

                        break;

                    case MessagesType.SAVE_TRAJECTORIES:

                        email = mensagemDoCliente.getEmail();
                        String distance = mensagemDoCliente.getDistance();
                        String time = mensagemDoCliente.getTime();

                        selectStatement = new Select();
                        int new_index_traj = selectStatement.getIndexOfTrajectory(email);

                        System.out.println("#####################################################################");
                        System.out.println("Recebi esta mensagem com tipo --> " + mensagemDoCliente.getTipo());
                        System.out.println("Recebi esta mensagem com email --> " + mensagemDoCliente.getEmail());
                        System.out.println("Recebi esta mensagem com distance --> " + mensagemDoCliente.getDistance());
                        System.out.println("Recebi esta mensagem com time --> " + mensagemDoCliente.getTime());
                        System.out.println("nova trajetoria com indice --> " + new_index_traj);

                        insertStatement = new Insert();
                        int new_traje = insertStatement.insertNewTrajectory(email, new_index_traj, distance, time);

                        ArrayList<String> locations = mensagemDoCliente.getLocations();
                        System.out.println(locations.toArray().toString());
                        selectStatement = new Select();
                        int new_index_loc = selectStatement.getIndexOfLocation();
                        int i = 0;
                        for (String l : locations) {
                            System.out.println(l);
                            String latitude = l.split("/")[0];
                            String longitude = l.split("/")[1];
                            int sequence = i;
                            insertStatement = new Insert();
                            insertStatement.insertNewLocation(email, new_index_loc, latitude, longitude, sequence, new_index_traj);
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

                        for (int j = 0; j < trajectories.size(); j++) {
                            String[] traj_split = trajectories.get(j).split(":");

                            selectStatement = new Select();
                            String latlong = selectStatement.getLocationsOfTrajectory(Integer.valueOf(traj_split[0]), email);
                            System.out.println("LatLong" + latlong);
                            String new_string = trajectories.get(j) + latlong;
                            System.out.println(new_string);
                            trajectories.set(j, new_string);

                        }

                        sendArray(trajectories);

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


    private int indexTrajectory(String email) {


        return 0;
    }

}
