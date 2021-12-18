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
    private boolean inMatch;

    public Server(int port, Map<String, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
        this.usernames = new HashMap<>();
        inMatch = false;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
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

        if(connections.size() == 1)
            broadcast("O jogador "+idCliente+" entrou, existe "+connections.size()+" jogador conectado.");
        else
            broadcast("O jogador "+idCliente+" entrou, existem "+connections.size()+" jogadores conectados.");

        conn.send("Bem vindo ao jogo, "+idCliente+"!");
        conn.send("Jogadores conectados: "+connections.keySet());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String nome = getConnectionName(conn);
        //busca o nome correspondente à conexão no map e o exclui
        if(nome != null) {
            connections.remove(nome);//passar o idUsuario através do close(message)

            if(connections.size() == 1)
                broadcast("O jogador "+nome+" saiu, existe "+connections.size()+" jogador conectado.");
            else
                broadcast("O jogador "+nome+" saiu, existem "+connections.size()+" jogadores conectados.");
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

        String player = getConnectionName(conn);
        if(inMatch)//verifica se uma partida está em andamento
        {
            match.verifyPlayerWord(player, message);
            if(match.checkGameOver())
            {
                inMatch = false;
                conn.send("Fim de jogo");
                //exibir placar
                broadcast(match.getStats());
            }
            else {
                conn.send(match.playersNextWord(player));
            }
        }
        else
        {
            if(message.equalsIgnoreCase("start"))
            {
                createMatch();//inicia uma partida
                broadcast(match.playersNextWord(player));
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Erro "+ex+" na conexao: "+conn.getRemoteSocketAddress());
    }

    @Override
    public void onStart() {
        System.out.println("Servidor iniciado na porta "+getPort());
    }

    private void createMatch()
    {
        match = new Match();
        match.init(connections.keySet());
        this.inMatch = true;
    }

    private String getConnectionName(WebSocket conn)
    {
        for(Map.Entry<String, WebSocket> x : connections.entrySet())
        {
            if(x.getValue().equals(conn))
            {
                return x.getKey();
            }
        }

        return null;
    }
}
