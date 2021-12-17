package br.usp.each.typerace.game;

import java.io.*;
import java.util.*;

public class Match {

    private Map<String, Integer> playersCurrentIndexes;
    private Map<String, Integer> playersScores;

    private String[] matchWords;

    public Match(){
        playersCurrentIndexes = new HashMap<>();
        playersScores = new HashMap<>();

        matchWords = new String[100];
    }

    public void init(Set<String> names)
    {
        try {
            for (String name : names) {
                playersCurrentIndexes.put(name, 0);
                playersScores.put(name, 0);
            }

            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/server/src/main/resources/dicionario.txt"));

            String line = br.readLine();
            ArrayList<String> allWords = new ArrayList<>();
            Random r = new Random();

            while (line != null)
            {
                allWords.add(line);
                line = br.readLine();
            }

            for(int i = 0;i<100;i++)
            {
                String tempWord = allWords.get(r.nextInt(5000));
                matchWords[i] = tempWord;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String[] getWords()
    {
        return matchWords;
    }

    public static void main(String[] args) {

        Match m = new Match();
        m.init(new HashSet<String>());

        System.out.println("Palavras: ");
        for(String word : m.getWords())
            System.out.print(word+" ");
    }
}
