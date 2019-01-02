package SpeedTyper;

import java.util.Vector;

/**
 *
 * @author Cigol
 */

// Thread class that moves all the words across the screen at 25 millisecond
// intervals
public class Updater implements Runnable {
    private static final int UPDATE_INTERVAL = 25;
    
    private int pos = 0;
    
    private Vector gameWords;
    private GamePanel game;
    private WordMaker wordMaker;
    private Word word;
    
    public Updater(GamePanel game, WordMaker wordMaker) {
        this.game = game;
        this.wordMaker = wordMaker;
    }
    
    @Override
    public void run(){
        // Iterates until the game Panel tells all threads to stop
        while (game.stopThreads == false) {
            gameWords = wordMaker.getGameWords();
            
            // Push all words 3 pixels to the right
            for(int i=0; i<gameWords.size(); i++){
                word = (Word) gameWords.elementAt(i);
                pos = word.getXPos();
                word.setXPos(pos + 3);
            }
            
            // Call back paintComponent()
            game.repaint(); 
            
            // Sleep for a bit and give other threads a chance to run
            try { 
                Thread.sleep(UPDATE_INTERVAL);  // milliseconds
            } catch (InterruptedException e) {}
        }
    }
}
