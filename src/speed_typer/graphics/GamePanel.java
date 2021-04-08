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
    
    public static final boolean GRAPHICAL_DEBUG_MODE = false;
    public static final int MAX_MISSED_SCORE = 3;
    public static final int SCOREBOX_HEIGHT = 50;
    public static final int INPUT_BOX_HEIGHT = 50;
    
    private int score, wordsMissed, difficulty;
    private boolean gameStarted;
    
    private String wordInput;
    private List<Word> gameWords;
    private Word word;
    private Dictionary dictionary;
    private WordMaker wordMaker;
    private PanelUpdater updater;
    private WordRemover wordRemover;
    private PanelPainter panelPainter;
    
    private Thread updaterThread, wordMakerThread, wordRemoverThread;
    
    public boolean stopThreads;
    
    public GamePanel(){
        // Object Initialization 
        dictionary = new Dictionary();
        panelPainter = new PanelPainter(wordMaker);
        
        // Primitive Initialization
        gameStarted = false;
        difficulty = 1;
    }
    
    private void startGame(){
        gameStarted = true;
        
        // Object Initialization 
        // Threads are Initialized on their own too to evidence their 
        // constructors
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
    
    public void enterPressed(){
        if(!gameStarted){
            startGame();
        }
    }

    public void charEntered(char c){
        // If the user hasn't lost yet, add character to wordInput
        if(wordsMissed != MAX_MISSED_SCORE){
            wordInput += c;
            wordInput = wordInput.toUpperCase();
            checkWords();
            repaint();
        }
        
        // If the user has already lost and presses 'r', restart the game
        else if((wordsMissed == MAX_MISSED_SCORE)&&(Character.toLowerCase(c) == 'r')){
            startGame();
        }
    }
    
     public void numEntered(int n){
        // Set difficulty if applicable
        if(!gameStarted && n > 0 && n < 4){
            difficulty = n;
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
                score++;
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
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.black);
        
        panelPainter.setDimensions(getWidth(), getHeight());
        
        if(!gameStarted){
            panelPainter.paintGameIntroScreen(g, difficulty);
        }
        else{
            panelPainter.paintScoreBox(g, score, wordsMissed);

            if(GRAPHICAL_DEBUG_MODE){
                panelPainter.paintScreenHeight(g);
            }

            panelPainter.paintInputBox(g, wordInput);

            panelPainter.paintGameWords(g);

            if(wordsMissed == MAX_MISSED_SCORE){
                panelPainter.paintGameOverScreen(g, score);
            }
        }
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
}
