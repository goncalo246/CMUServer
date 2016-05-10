package WebServer;

import java.util.ArrayList;
import java.util.Arrays;

import static WebServer.MessagesType.CRIARCONTA;

/**
 * Created by Goncalo Pinto on 28/04/2016.
 */
public class Utils {


    private static String email;

    public static MensagemDoCliente gerarMensagemcliente(String msg) {
        String[] mensagem = msg.split(":");

        switch (mensagem[0]) {
            case CRIARCONTA:
                System.out.println("A mensagem foi" + mensagem[0] + ", portanto vou tentar criar conta");
                String nome = mensagem[1];
                email = mensagem[2];
                String password = mensagem[3];
                return new MensagemDoCliente(CRIARCONTA, nome, email, password);

            case MessagesType.LOGIN:
                System.out.println("A mensagem foi" + mensagem[0] + ", portanto vou verificar login");
                return new MensagemDoCliente(MessagesType.LOGIN, mensagem[1], mensagem[2]);

            case MessagesType.SAVE_TRAJECTORIES:
                email = mensagem[1];
                String distance = mensagem[2];
                String time = mensagem[3];

                String[] locations = Arrays.copyOfRange(mensagem, 4, mensagem.length - 1);

                System.out.println(locations.toString());
                return new MensagemDoCliente(MessagesType.SAVE_TRAJECTORIES, email, distance, time, new ArrayList<String>(Arrays.asList(locations)));

            case MessagesType.GET_TRAJECTORIES:

                email = mensagem[1];

                return new MensagemDoCliente(MessagesType.GET_TRAJECTORIES, email);

            case MessagesType.GET_USER_INFORMATIONS:

                email = mensagem[1];

                return new MensagemDoCliente(MessagesType.GET_USER_INFORMATIONS, email);
            default:
                return null;
        }
    }
}
