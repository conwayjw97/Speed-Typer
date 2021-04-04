package speed_typer.graphics;

import java.awt.Font;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import java.util.logging.Logger;
import speed_typer.data.Dictionary;
import speed_typer.data.Word;
import speed_typer.threads.WordRemover;
import speed_typer.threads.PanelUpdater;
import speed_typer.threads.WordMaker;
import javax.swing.JPanel;

/**
 *
 * @author Cigol
 */
public class GamePanel extends JPanel{
    private static final Logger LOG = Logger.getLogger(GamePanel.class.getName());
    private static final boolean GRAPHICAL_DEBUG_MODE = false;
    private static final int MAX_MISSED_SCORE = 3;
    
    public static final int SCOREBOX_HEIGHT = 50;
    public static final int INPUT_BOX_HEIGHT = 50;
    
    private int i, score, wordsMissed;
    private char c;
    
    private String wordInput;
    private List<Word> gameWords;
    private Word word;
    private Dictionary dictionary;
    private WordMaker wordMaker;
    private PanelUpdater updater;
    private WordRemover wordRemover;
    private Font font;
    private FontMetrics metrics;
    
    private Thread updaterThread, wordMakerThread, wordRemoverThread;
    
    public boolean stopThreads;
    
    public GamePanel(){
        // Put the constructor in its own method so it can be called again
        // to restart the game
        startGame();
    }
    
    private void startGame(){
        // Object Initialization 
        // Threads are Initialized on their own too to evidence their 
        // constructors
        dictionary = new Dictionary();
        wordMaker = new WordMaker(this, dictionary);
        wordRemover = new WordRemover(this);
        
        updater = new PanelUpdater(this, wordMaker);
        
        // Primitive Initialization
        score = wordsMissed = 0;
        wordInput = "";
        stopThreads = false;
        
        // Thread Initialization
        wordMakerThread = new Thread(wordMaker);
        updaterThread = new Thread(updater);
        wordRemoverThread = new Thread(wordRemover);
        
        // Thread Starting
        wordMakerThread.start();
        updaterThread.start();
        wordRemoverThread.start();
    }
    
    public WordMaker getWordMaker(){
        return wordMaker;
    }
    
    public Thread getWordMakerThread(){
        return wordMakerThread;
    }
    
    public WordRemover getWordRemover(){
        return wordRemover;
    }
    
    public Thread getWordRemoverThread(){
        return wordRemoverThread;
    }
    
    public void charEntered(char c){
        // If the user hasn't lost yet, add character to wordInput
        if (wordsMissed != MAX_MISSED_SCORE){
            wordInput += c;
            wordInput = wordInput.toUpperCase();
            checkWords();
            repaint();
        }
        
        // If the user has already lost and presses 'r', restart the game
        else if ((wordsMissed == MAX_MISSED_SCORE)&&(Character.toLowerCase(c) == 'r')){
            startGame();
        }
    }
    
    public void charDeleted(){
        // If there's anything in the wordInput, delete the last letter
        if(wordInput.length() > 0){
            wordInput = wordInput.substring(0, wordInput.length() - 1);
            checkWords();
            repaint();
        }
    }
    
    public void wordDeleted(){
        // If there's anything in the wordInput, delete the last letter
        if(wordInput.length() > 0){
            wordInput = "";
            repaint();
        }
    }
    
    // If the wordInput is equal to any of the gameWords, set that gameWord
    // to Entered, clear the wordInput, and increment the score
    public void checkWords(){
        gameWords = wordMaker.getGameWords();
        for(int i=0; i<gameWords.size(); i++){
                word = gameWords.get(i);
                if ((word.getWord()).equals(wordInput)){
                    word.setEntered(true);
                    wordInput = "";
                    score ++;
                }
            }
        wordMaker.setGameWords(gameWords);
    }
    
    // If the user misses a word before it leaves the screen, increment the
    // missedScore, and if the missedScore reaches the max, stop the game
    public void wordMissed(){
        wordsMissed++;
        if (wordsMissed == MAX_MISSED_SCORE){
            stopThreads = true;
            repaint();
        }
    }
    
    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.black);
        
        // Draw scorebox
        font = new Font("Terminal", Font.ITALIC, 45);
        g.setFont(font);
        g.setColor(Color.white);
        g.fillRect(0, 45, getWidth(), 5);
        g.drawString("SCORE: " + score + "   WORDS MISSED: " + wordsMissed + "/3", 5, 37);
        
        // [DEBUGGING, DELETE IF NECESSARY] Paint the height of the screen on 
        // the panel
        if(GRAPHICAL_DEBUG_MODE){
            for(i=0; i<getHeight(); i+=50){
                g.drawString(String.valueOf(i), getWidth()-100, i);
                g.drawLine(getWidth()-100, i, getWidth(), i);
            }
        }
        
        // Draw userinput and input box
        font = new Font("Terminal", Font.ITALIC, 45);
        g.setFont(font);
        g.setColor(Color.white);
        g.fillRect(0, getHeight()-50, getWidth(), 5);
        g.drawString(wordInput, 5, getHeight()-5);
        
        // Draw the gameWords
        font = new Font("Terminal", Font.PLAIN, 45);
        g.setFont(font);
        gameWords = wordMaker.getGameWords();
        for(int i=0; i<gameWords.size(); i++){
            font = new Font("Terminal", Font.PLAIN, 45);
            g.setFont(font);
            word = gameWords.get(i);
            if(word.getEntered() == true){
                g.setColor(Color.darkGray);
            }
            else if(word.getEntered() == false){
                g.setColor(Color.white);
            }
            g.drawString(word.getWord(), word.getXPos(), word.getYPos());
            
            // [DEBUGGING, DELETE IF NECESSARY] Paint the coordinates of the word
            // below it
            if(GRAPHICAL_DEBUG_MODE){
                font = new Font("Terminal", Font.PLAIN, 12);
                g.setFont(font);
                g.drawString(String.valueOf(word.getXPos()), word.getXPos(), word.getYPos()+10);
                g.drawString(String.valueOf(word.getYPos()), word.getXPos()+30, word.getYPos()+10);
            }
        }
        
        // Draw GameOver screen, if the user has lost
        if(wordsMissed == MAX_MISSED_SCORE){
            g.setColor(Color.white);
            g.fillRect(0, 50, getWidth(), getHeight()-50-45);
            
            g.setColor(Color.black);
            font = new Font("Terminal", Font.PLAIN, 128);
            String text = "GAME OVER";
            
            metrics = g.getFontMetrics(font);
            int x = (getWidth() - metrics.stringWidth(text)) / 2;
            int y = (getHeight() / 2);
            
            g.setFont(font); 
            g.drawString(text, x, y-20);
            
            font = new Font("Terminal", Font.PLAIN, 64);
            text = "FINAL SCORE: " + Integer.toString(score);
            
            metrics = g.getFontMetrics(font);
            x = (getWidth() - metrics.stringWidth(text)) / 2;
            y = (getHeight() / 2);
            
            g.setFont(font); 
            g.drawString(text, x, y+40);
            
            text = "PRESS R TO RESTART";
            
            metrics = g.getFontMetrics(font);
            x = (getWidth() - metrics.stringWidth(text)) / 2;
            y = (getHeight() / 2);
            
            g.setFont(font); 
            g.drawString(text, x, y+100);
        }
    }
}
