package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.BufferedReader;

public class ClientMain {

    private final WebSocketClient client;

    public ClientMain(WebSocketClient client) {
        this.client = client;
    }

    public void init(String idCliente) {

            System.out.println("Iniciando cliente: " + idCliente);
            // TODO: Implementar
            //
            client.addHeader("idCliente", idCliente);
            client.connect();
    }

    public static void main(String[] args) {
        /*
           FIXME: Remover essas strings fixas
           Como podemos fazer para que o cliente receba um parâmetro indicando a qual servidor
           ele deve se conectar e o seu ID?
        */
        String removeMe = "ws://localhost:8080";
        String removeMe2 = "idCliente"; //mais próximo de um nickname

        try {
            WebSocketClient client = new Client(new URI(removeMe));

            ClientMain main = new ClientMain(client);

            main.init(removeMe2);

            BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
            String aux = null;
            while(true)
            {
                aux = br.readLine();
                if(aux != null && !aux.equals(""))
                {
                    client.send(aux);
                }
                aux = "";
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }catch (IOException io) {
            io.printStackTrace();
        }
    }
}
