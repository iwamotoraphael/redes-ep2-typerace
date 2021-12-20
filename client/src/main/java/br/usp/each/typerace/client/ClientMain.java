package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;


public class ClientMain{

    private final WebSocketClient client;

    public ClientMain(WebSocketClient client) {
        this.client = client;
    }

    /*Inicializa a conexão do client*/
    public void init(String idCliente) {

            System.out.println("Iniciando cliente: " + idCliente);
            System.out.println("------------------------------------------------------------------------------------------\n");
            client.addHeader("idCliente", idCliente);//adiciona o idCliente à requisição
            client.connect();//conecta ao servidor
    }

    public static void main(String[] args) {
        String serverURI = "ws://localhost:8080";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            WebSocketClient client = new Client(new URI(serverURI));

            ClientMain main = new ClientMain(client);

            System.out.println("Digite seu nome de usuario: ");
            String nickName = br.readLine();

            main.init(nickName);

            String wordBuffer = "";

            while(true)
            {
                wordBuffer = br.readLine();
                if(wordBuffer.length() > 0)//a palavra so e computada caso ela possua ao menos uma letra.
                {
                    //fecha a conexão através do comando quit, independente de como foi escrito
                    if(wordBuffer.equalsIgnoreCase("quit")) {
                        client.close();
                        break;
                    }
                    client.send(wordBuffer);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }catch (IOException io) {
            io.printStackTrace();
        }
    }
}
