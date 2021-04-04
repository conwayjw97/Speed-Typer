package speed_typer.threads;

import java.awt.Font;
import java.awt.FontMetrics;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import speed_typer.data.Dictionary;
import speed_typer.graphics.GamePanel;
import speed_typer.data.Word;

/**
 *
 * @author Cigol
 */

// Thread class that takes words from the Dictionary to put into gameWords at
// 1 second intervals
public class WordMaker implements Runnable {
    private static final Logger LOG = Logger.getLogger(WordMaker.class.getName());
    private static final int UPDATE_INTERVAL = 1000;
    
    private int randomInt, text1Bottom, text2Bottom, text1Right, text1Top, text2Top, text2Left;
    
    private final GamePanel gamePanel;
    private final Dictionary dictionary;
    private final Font font;
    private final FontMetrics metrics;
    private final Random r;
    
    private List<Word> gameWords;
    private Word word, overlapTestWord;
    
    public WordMaker(GamePanel gamePanel, Dictionary dictionary) {
        this.gamePanel = gamePanel;
        this.dictionary = dictionary;
        gameWords = new ArrayList<>();
        font = new Font("Terminal", Font.PLAIN, 45);
        metrics = gamePanel.getFontMetrics(font);
        r = new Random();
        
        // randomInt starts at 500 because otherwise it would start at 0 and 
        // print the first word above the panel so the user couldn't see it
        randomInt = 500;
    }
    
    public List<Word> getGameWords(){
        return gameWords;
    }
    
    public void setGameWords(List<Word> gameWords){
        this.gameWords = gameWords;
    }
    
    // checkOverlap iterates through all the gameWords and checks whether
    // a string printed on the given integers would overlap an existing
    // gameWord string
    private synchronized boolean checkOverlap(int xPosRight, int yPos){
        while(gamePanel.getWordRemoverThread().getState() != State.TIMED_WAITING){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "Interrupted Thread Exception", e);
            }
        }
        
        text1Bottom = yPos;
        text1Top = yPos-metrics.getHeight();
        text1Right = xPosRight;
        
        if(gameWords.size() > 0){
            for(int i=0; i<gameWords.size(); i++){
                overlapTestWord = gameWords.get(i);
                
                text2Bottom = overlapTestWord.getYPos();
                text2Top = overlapTestWord.getYPos()-metrics.getHeight();
                text2Left = overlapTestWord.getXPos();
                
                if((text1Right > text2Left) && ((text1Bottom > text2Top) || (text1Top < text2Bottom))){
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void run(){ 
        int bottomScreenBound = (int) (gamePanel.getPreferredSize().getHeight() - metrics.getHeight()) - GamePanel.INPUT_BOX_HEIGHT;
        int topScreenBound = GamePanel.SCOREBOX_HEIGHT + metrics.getHeight();
                    
        // Iterates until the game Panel tells all threads to stop
        while (gamePanel.stopThreads == false) {
            word = dictionary.getRandomWord();
            
            // If the word is already being used, try again
            if (word.getUse() == true){
                continue;
            }
            else{
                // Start the word out to the left of the panel
                word.setXPos(0 - metrics.stringWidth(word.getWord()));
                
                // Generate y positions until a non-overlapping one is found
                boolean overlapping = true;
                while((overlapping == true) && (gameWords.size()>0)){
                    randomInt = r.nextInt(bottomScreenBound - topScreenBound) + topScreenBound; // Generates a new word Y position between the scoreboard and input box
                    overlapping = checkOverlap(word.getXPos() + metrics.stringWidth(word.getWord()), randomInt);
                }
                
                // Set the word up for use in the game
                word.setYPos(randomInt);
                word.setUse(true);
                gameWords.add(word);
            }
            
            // Sleep for a bit and give other threads a chance to run
            try { 
                Thread.sleep(UPDATE_INTERVAL);  // milliseconds
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "Interrupted Thread Exception", e);
            }
        }
    }
}
