package speed_typer.data;

import speed_typer.data.Word;
import java.io.*;
import java.util.*;

/**
 *
 * @author Cigol
 */

// The class for the Dictionary that contains all the words available to the game
public class Dictionary {
    private int randomInt;
    
    private List<Word> words;
    private FileReader f;
    private BufferedReader fIN;
    private String s, fileWord;
    private StringTokenizer st;
    private Word word;
    private Random r = new Random();
    
    public Dictionary(){
        // The constructor opens the Dictionary.txt file and loads all the words
        // inside of it into the words list
        words = new ArrayList<>();
        try{
            f = new FileReader("Dictionary.txt");
            fIN = new BufferedReader(f);
            s = fIN.readLine();
            while(s != null){
                st = new StringTokenizer(s, " ");
                fileWord = st.nextToken();
                fileWord = fileWord.toUpperCase();
                word = new Word(fileWord);
                words.add(word);
                s = fIN.readLine();
            }
        }
        catch(IOException e){
            System.out.println("Error reading from file");
            System.exit(0);
        }
    }
    
    public List<Word> getWords(){
        return words;
    }
    
    public Word getRandomWord(){
        // Returns a random word object from the words list
        randomInt = r.nextInt(words.size());
        word = words.get(randomInt);
        return word;
    }
}
