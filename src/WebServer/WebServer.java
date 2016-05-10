package WebServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Goncalo Pinto on 28/04/2016.
 */
public class WebServer {

    private ServerSocket serverSocket;
    private List<DealWithClient> clientReps = new ArrayList<DealWithClient>();

    private void startServing() {
        try {
            serverSocket = new ServerSocket(6666); // Server socket

        } catch (IOException e) {
            System.out.println("Could not listen on port: 6666");
        }

        System.out.println("Server started. Listening to the port 6666");
        System.out.println("Wait for clients connections");

        while (true) {
            try {

                Socket s = serverSocket.accept();
                System.out.println("Client connected");
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

                ObjectInputStream in = new ObjectInputStream(s.getInputStream());

                DealWithClient deal = new DealWithClient(out, in, this);
                deal.start();

                clientReps.add(deal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new WebServer().startServing();

    }

}
