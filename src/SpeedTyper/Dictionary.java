package SpeedTyper;

import java.io.*;
import java.util.*;

/**
 *
 * @author Cigol
 */

// The class for the Dictionary that contains all the words available to the game
public class Dictionary {
    private int randomInt;
    
    private Vector words;
    private FileReader f;
    private BufferedReader fIN;
    private String s, fileWord;
    private StringTokenizer st;
    private Word word;
    private Random r = new Random();
    
    Dictionary(){
        // The constructor opens the Dictionary.txt file and loads all the words
        // inside of it into the words Vector
        words = new Vector();
        try{
            f = new FileReader("Dictionary.txt");
            fIN = new BufferedReader(f);
            s = fIN.readLine();
            while(s != null){
                st = new StringTokenizer(s, " ");
                fileWord = st.nextToken();
                fileWord = fileWord.toUpperCase();
                word = new Word(fileWord);
                words.addElement(word);
                s = fIN.readLine();
            }
        }
        catch(IOException e){
            System.out.println("Error reading from file");
            System.exit(0);
        }
    }
    
    public Vector getWords(){
        return words;
    }
    
    public Word getRandomWord(){
        // Returns a random word object from the words Vector
        randomInt = r.nextInt(words.size());
        word = (Word) words.elementAt(randomInt);
        return word;
    }
}
