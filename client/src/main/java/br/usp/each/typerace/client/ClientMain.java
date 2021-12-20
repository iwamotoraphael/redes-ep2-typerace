package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Scanner;

public class ClientMain{

    private final WebSocketClient client;

    public ClientMain(WebSocketClient client) {
        this.client = client;
    }

    public void init(String idCliente) {

            System.out.println("Iniciando cliente: " + idCliente);
            System.out.println("------------------------------------------------------------------------------------------\n");
            // TODO: Implementar
            //
            client.addHeader("idCliente", idCliente);//adiciona o idCliente à requisição
            client.connect();//conecta ao servidor
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
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            WebSocketClient client = new Client(new URI(removeMe));

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
