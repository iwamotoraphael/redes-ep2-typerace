package br.usp.each.typerace.game;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Match {

    private Map<String, Integer> playersCurrentIndexes;
    private Map<String, Integer> playersScores;

    private String[] matchWords;

    private long start;
    private long matchDuration;

    public Match(){
        playersCurrentIndexes = new HashMap<>();
        playersScores = new HashMap<>();

        matchWords = new String[50];
    }

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

    /*pega palavras*/
    private String[] getWords()
    {
        return matchWords;
    }

    private boolean playerValid(String player)
    {
        int index = playersCurrentIndexes.get(player);
        return index<matchWords.length;//retorna true se o index do jogador for válido
    }

    public String playersNextWord(String player)
    {
        if(playerValid(player))
        {
            return(matchWords[playersCurrentIndexes.get(player)]);
        }
        else
            return null;
    }

    public void verifyPlayerWord(String player, String word)
    {
        if(playerValid(player)){
            int index = playersCurrentIndexes.get(player);
            playersCurrentIndexes.replace(player, index + 1);
            if(word.equalsIgnoreCase(matchWords[index]))
                playersScores.replace(player, playersScores.get(player)+1);
        }
    }

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

    public void removePlayer(String player)
    {
        if(playersCurrentIndexes.containsKey(player))
            playersCurrentIndexes.replace(player,50);
    }

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
