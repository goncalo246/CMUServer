package WebServer;

import sun.plugin2.message.Message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Goncalo Pinto on 28/04/2016.
 */
public class MensagemDoCliente implements Serializable {

    private String tipo;
    private String mensagem;
    private String nome;
    private String email;
    private String password;
    private String distance;
    private String time;
    private ArrayList<String> locations;

    public MensagemDoCliente(String tipo, String email, String password) {
        this.tipo = tipo;
        this.email = email;
        this.password = password;
    }

    public MensagemDoCliente(String tipo, String nome, String email, String password) {
        this.tipo = tipo;
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    public MensagemDoCliente(String tipo, String email, String distance, String time, ArrayList<String> locations) {
        this.tipo = tipo;
        this.email = email;
        this.distance = distance;
        this.time = time;
        this.locations = locations;
    }

    public MensagemDoCliente(String tipo, String email) {
        this.tipo = tipo;
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }
}
