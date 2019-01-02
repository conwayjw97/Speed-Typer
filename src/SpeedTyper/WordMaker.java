package SpeedTyper;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Cigol
 */

// Thread class that takes words from the Dictionary to put into gameWords at
// 2 second intervals
public class WordMaker implements Runnable {
    private static final int UPDATE_INTERVAL = 2000;
    
    private int randomInt, text1Bottom, text2Bottom, text1Right, text1Top, text2Top, text2Left;
    private boolean overlapFlag;
    
    private GamePanel game;
    private Vector gameWords;
    private Dictionary dictionary;
    private Word word, overlapTestWord;
    private Font font;
    private FontMetrics metrics;
    private Graphics g;
    private Random r;
    
    public WordMaker(GamePanel game, Dictionary dictionary) {
        this.game = game;
        this.dictionary = dictionary;
        gameWords = new Vector();
        font = new Font("Terminal", Font.PLAIN, 45);
        metrics = game.getFontMetrics(font);
        r = new Random();
        
        // randomInt starts at 500 because otherwise it would start at 0 and 
        // print the first word above the panel so the user couldn't see it
        randomInt = 500;
    }
    
    public Vector getGameWords(){
        return gameWords;
    }
    
    public void setGameWords(Vector gameWords){
        this.gameWords = gameWords;
    }
    
    private boolean checkOverlap(int xPosRight,int yPos){
        // checkOverlap iterates through all the gameWords and checks whether
        // a string printed on the given integers would overlap an existing
        // gameWord string
        overlapFlag = false;
        text1Bottom = yPos;
        text1Top = yPos-metrics.getHeight();
        text1Right = xPosRight;
        
        if(gameWords.size()>0){
            for(int i=0; i<gameWords.size(); i++){
                overlapTestWord = (Word) gameWords.elementAt(i);
                
                text2Bottom = overlapTestWord.getYPos();
                text2Top = overlapTestWord.getYPos()-metrics.getHeight();
                text2Left = overlapTestWord.getXPos();
                
                if((text1Right > text2Left) && ((text1Bottom > text2Top) || (text1Top < text2Bottom))){
                    overlapFlag = true;
                }
            }
        }
        return overlapFlag;
    }
    
    @Override
    public void run(){ 
        // Iterates until the game Panel tells all threads to stop
        while (game.stopThreads == false) {
            word = dictionary.getRandomWord();
            
            // If the word is already being used, try again
            if (word.getUse() == true){
                continue;
            }
            else{
                // Start the word out to the left of the panel
                word.setXPos(0 - metrics.stringWidth(word.getWord()));
                
                // Generate y positions until a non-overlapping one is found
                overlapFlag = true;
                while((overlapFlag == true) && (gameWords.size()>0)){
                    randomInt = r.nextInt((int) (game.getPreferredSize().getHeight() - metrics.getHeight()) - 100);
                    randomInt = randomInt + 50 + metrics.getHeight();
                    overlapFlag = checkOverlap(word.getXPos() + metrics.stringWidth(word.getWord()), randomInt);
                }
                
                // Set the word up for use in the game
                word.setYPos(randomInt);
                word.setUse(true);
                gameWords.addElement(word);
            }
            
            // Sleep for a bit and give other threads a chance to run
            try { 
                Thread.sleep(UPDATE_INTERVAL);  // milliseconds
            } catch (InterruptedException ignore) {}
        }
    }
}
