package WebServer;

import java.util.ArrayList;
import java.util.Arrays;

import static WebServer.MessagesType.CRIARCONTA;

/**
 * Created by Goncalo Pinto on 28/04/2016.
 */
public class Utils {


    private static String email;

    public static MensagemDoCliente gerarMensagemcliente(Object msg) {

        String[] mensagem = null;
        String tipo;
        ArrayList<String> men = null;

        if (msg instanceof ArrayList<?>) {
            men = (ArrayList<String>) msg;
            tipo = men.get(0);
        } else {
            mensagem = ((String) msg).split(":");
            tipo = mensagem[0];
        }


        switch (tipo) {
            case CRIARCONTA:
                System.out.println("A mensagem foi" + tipo + ", portanto vou tentar criar conta");
                String nome = mensagem[1];
                email = mensagem[2];
                String password = mensagem[3];
                return new MensagemDoCliente(CRIARCONTA, nome, email, password);

            case MessagesType.LOGIN:
                System.out.println("A mensagem foi" + tipo + ", portanto vou verificar login");
                return new MensagemDoCliente(MessagesType.LOGIN, mensagem[1], mensagem[2]);

            case MessagesType.SAVE_TRAJECTORIES:
                email = mensagem[1];
                String distance = mensagem[2];
                String time = mensagem[3];
                String pontos = mensagem[4];

                String[] locations = Arrays.copyOfRange(mensagem, 5, mensagem.length - 1);

                System.out.println(locations.toString());
                return new MensagemDoCliente(MessagesType.SAVE_TRAJECTORIES, email, distance, time, pontos, new ArrayList<String>(Arrays.asList(locations)));

            case MessagesType.GET_TRAJECTORIES:

                email = mensagem[1];

                return new MensagemDoCliente(MessagesType.GET_TRAJECTORIES, email);

            case MessagesType.GET_USER_INFORMATIONS:

                email = mensagem[1];

                return new MensagemDoCliente(MessagesType.GET_USER_INFORMATIONS, email);

            case MessagesType.GET_STATIONS:

                return new MensagemDoCliente(MessagesType.GET_STATIONS);

            case MessagesType.SET_AVAILABILITY_BIKE:

                String identifier = mensagem[1];
                boolean isAvailable = Boolean.valueOf(mensagem[2]);

                return new MensagemDoCliente(MessagesType.SET_AVAILABILITY_BIKE, identifier, isAvailable);

            case MessagesType.UPDATE_TRAJECTORIES:

                ArrayList<String> messa = men;

                ArrayList<String> subList = new ArrayList<>(messa.subList(2, messa.size()));

                return new MensagemDoCliente(messa.get(0), messa.get(1), subList);
            default:
                return null;
        }
    }
}
