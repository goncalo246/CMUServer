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
    private String pontos;
    private String password;
    private String distance;
    private String time;
    private ArrayList<String> locations;
    private String identifier;
    private boolean availability;
    private ArrayList<String> data;

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

    public MensagemDoCliente(String tipo, String email, String distance, String time, String pontos, ArrayList<String> locations) {
        this.tipo = tipo;
        this.email = email;
        this.distance = distance;
        this.time = time;
        this.pontos = pontos;
        this.locations = locations;
    }

    public MensagemDoCliente(String tipo, String email) {
        this.tipo = tipo;
        this.email = email;
    }

    public MensagemDoCliente(String tipo, String identifier, boolean availability) {
        this.tipo = tipo;
        this.identifier = identifier;
        this.availability = availability;
    }

    public MensagemDoCliente(String tipo, String email, ArrayList<String> data) {
        this.tipo = tipo;
        this.email = email;
        this.data = data;
    }

    public MensagemDoCliente(String tipo) {
        this.tipo = tipo;
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

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public String getPontos() {
        return pontos;
    }

    public boolean isAvailable() {
        return availability;
    }
}
