package br.usp.each.typerace.game;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Match {

    private Map<String, Integer> playersCurrentIndexes; //Map que armazena a posição de cada jogador no vetor de palavras
    private Map<String, Integer> playersScores;//Map que armazena a pontuação de cada jogador

    private String[] matchWords;//Vetor que armazena as palavras a serem digitadas na partida

    //Variáveis que armazenam o ínicio e a duração da partidas
    private long start;
    private long matchDuration;

    /*Construtor padrão que inicializa as variáveis globais*/
    public Match(){
        playersCurrentIndexes = new HashMap<>();
        playersScores = new HashMap<>();

        matchWords = new String[50];
    }

    /*Método que inicializa a partida, sorteando as palavras, insere os jogadores nas variáveis globais e a hora do início da partida*/
    public void init(Set<String> names)
    {
        try {
            for (String name : names) {
                playersCurrentIndexes.put(name, 0);
                playersScores.put(name, 0);
            }

            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/src/main/resources/dicionario.txt"));

            String line = br.readLine();
            ArrayList<String> allWords = new ArrayList<>();
            Random r = new Random();

            while (line != null)
            {
                allWords.add(line);
                line = br.readLine();
            }

            for(int i = 0;i<50;i++)
            {
                String tempWord = allWords.get(r.nextInt(5000));
                matchWords[i] = tempWord;
            }

            start = System.currentTimeMillis();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /*retorna as palavras utilizadas na partida*/
    private String[] getWords()
    {
        return matchWords;
    }

    /*Verifica se o index atual de um jogador é válido*/
    private boolean playerValid(String player)
    {
        int index = playersCurrentIndexes.get(player);
        return index<matchWords.length;
    }

    /*Retorna a palavra que um jogador deve digitar*/
    public String playersNextWord(String player)
    {
        if(playerValid(player))
        {
            return(matchWords[playersCurrentIndexes.get(player)]);
        }
        else
            return null;
    }

    /*Verifica se um jogador escreveu a palavra correta*/
    public void verifyPlayerWord(String player, String word)
    {
        if(playerValid(player)){
            int index = playersCurrentIndexes.get(player);
            playersCurrentIndexes.replace(player, index + 1);
            if(word.equalsIgnoreCase(matchWords[index]))
                playersScores.replace(player, playersScores.get(player)+1);
        }
    }

    /*Verifica se a partida alcançou uma das condições de término*/
    public boolean checkGameOver()
    {
        boolean everybodyLost = true;
        for(String player : playersScores.keySet())
        {
            if(playersCurrentIndexes.get(player) < 50)
                everybodyLost = false;

            if(playersScores.get(player) >= 20) {
                matchDuration = System.currentTimeMillis()-start;
                return true;
            }
        }

        if(everybodyLost)
        {
            matchDuration = System.currentTimeMillis()-start;
            return true;
        }
        return false;
    }

    /*Considera que o jogador errou todas as palavras restantes*/
    public void removePlayer(String player)
    {
        if(playersCurrentIndexes.containsKey(player))
            playersCurrentIndexes.replace(player,50);
    }

    /*Retorna as estatísticas da partida*/
    public String getStats()
    {
        String stats = "";
        long duration = matchDuration/1000;
        long minutes = duration/60;
        long seconds = duration%60;
        List<Map.Entry<String, Integer>> list = playersScores.entrySet().stream().sorted((x, y)->{return -(x.getValue().compareTo(y.getValue()));}).collect(Collectors.toList());
        int position = 1;
        for(Map.Entry e : list)
        {
            int erros = playersCurrentIndexes.get(e.getKey()) - (int) e.getValue();
            stats += position+". "+e.getKey()+" - "+e.getValue()+" pontos e "+erros+" erros.\n";
            position++;
        }
        stats += "A partida durou incriveis "+minutes+" minutos e "+seconds+" segundos.\nParabens a todos os jogadores!";
        return stats;
    }


}
