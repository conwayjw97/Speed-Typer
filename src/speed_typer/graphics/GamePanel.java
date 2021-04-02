package speed_typer.graphics;

import speed_typer.data.Dictionary;
import speed_typer.data.Word;
import speed_typer.threads.WordRemover;
import speed_typer.threads.PanelUpdater;
import speed_typer.threads.WordMaker;
import java.awt.*;
import java.util.Vector;
import javax.swing.JPanel;

/**
 *
 * @author Cigol
 */
public class GamePanel extends JPanel{
    private static final int MAX_MISSED_SCORE = 3;
    
    private int i, score, missedScore;
    private char c;
    
    private String wordInput;
    private Vector gameWords;
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
        updater = new PanelUpdater(this, wordMaker);
        wordRemover = new WordRemover(this, wordMaker);
        
        // Primitive Initialization
        score = missedScore = 0;
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
    
    public void charEntered(char c){
        this.c = c;
        
        // If the user hasn't lost yet, add character to wordInput
        if (missedScore != MAX_MISSED_SCORE){
            wordInput += c;
            wordInput = wordInput.toUpperCase();
            checkWords();
            repaint();
        }
        
        // If the user has already lost and presses 'r', restart the game
        else if ((missedScore == MAX_MISSED_SCORE)&&(Character.toLowerCase(c) == 'r')){
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
    
    public void checkWords(){
        // If the wordInput is equal to any of the gameWords, set that gameWord
        // to Entered, clear the wordInput, and increment the score
        gameWords = wordMaker.getGameWords();
        for(int i=0; i<gameWords.size(); i++){
                word = (Word) gameWords.elementAt(i);
                if ((word.getWord()).equals(wordInput)){
                    word.setEntered(true);
                    wordInput = "";
                    score ++;
                }
            }
        wordMaker.setGameWords(gameWords);
    }
    
    public void missWord(){
        // If the user misses a word before it leaves the screen, increment the
        // missedScore, and if the missedScore reaches the max, stop the game
        missedScore++;
        if (missedScore == MAX_MISSED_SCORE){
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
        g.drawString("SCORE: " + score + "   WORDS MISSED: " + missedScore + "/3", 5, 37);
        
        // [DEBUGGING, DELETE IF NECESSARY] Paint the height of the screen on 
        // the panel
//        for(i=0; i<getHeight(); i+=50){
//            g.drawString(String.valueOf(i), getWidth()-100, i);
//            g.drawLine(getWidth()-100, i, getWidth(), i);
//        }
        
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
            word = (Word) gameWords.elementAt(i);
            if(word.getEntered() == true){
                g.setColor(Color.darkGray);
            }
            else if(word.getEntered() == false){
                g.setColor(Color.white);
            }
            g.drawString(word.getWord(), word.getXPos(), word.getYPos());
            // [DEBUGGING, DELETE IF NECESSARY] Paint the coordinates of the word
            // below it
//            font = new Font("Terminal", Font.PLAIN, 12);
//            g.setFont(font);
//            g.drawString(String.valueOf(word.getXPos()), word.getXPos(), word.getYPos()+10);
//            g.drawString(String.valueOf(word.getYPos()), word.getXPos()+30, word.getYPos()+10);
        }
        
        // Draw GameOver screen, if the user has lost
        if(missedScore == MAX_MISSED_SCORE){
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
