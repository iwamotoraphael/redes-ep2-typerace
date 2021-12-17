package br.usp.each.typerace.server;

import br.usp.each.typerace.game.Match;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server extends WebSocketServer {

    //variaveis para conexao
    private final Map<String, WebSocket> connections;
    private final Map<String, Integer> usernames;

    //variavel partida
    private Match match;

    public Server(int port, Map<String, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
        this.usernames = new HashMap<>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // TODO: Implementar
        String idCliente = handshake.getFieldValue("idCliente");
        if(connections.containsKey(idCliente))//verifica se o idUsuario já foi utilizado
        {
            Integer intExtra = usernames.get(idCliente);
            String extra = intExtra.toString();
            usernames.replace(idCliente, intExtra+1);
            idCliente = idCliente+extra;
            usernames.put(idCliente, 1);
        }
        else
        {
            usernames.put(idCliente, 1);
        }
        connections.put(idCliente, conn);
        conn.send("Bem vindo ao jogo, "+idCliente+"!");
        broadcast("O jogador "+idCliente+" entrou na sessao");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // TODO: Implementar
        String nome = null;
        for(Map.Entry<String, WebSocket> x : connections.entrySet())
        {
            if(x.getValue().equals(conn))
            {
                nome = x.getKey();
                break;
            }
        }
        if(nome != null) {
            connections.remove(nome);//passar o idUsuario através do close(message)
            broadcast("O jogador " + nome + " saiu");
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // TODO: Implementar
        System.out.println("conexao: "+conn+" "+message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        // TODO: Implementar
        System.out.println("Erro "+ex+" na conexao: "+conn.getRemoteSocketAddress());
    }

    @Override
    public void onStart() {
        // TODO: Implementar
        System.out.println("Servidor iniciado na porta "+getPort());
    }

    private void createMatch()
    {

    }
}
