package speed_typer.threads;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import speed_typer.threads.WordMaker;
import speed_typer.graphics.GamePanel;
import speed_typer.data.Word;

/**
 *
 * @author Cigol
 */

// Thread class that removes all words that have moved off the screen from the
// gameWords list at 500 millisecond intervals
public class WordRemover implements Runnable {
    private static final Logger LOG = Logger.getLogger(WordRemover.class.getName());
    private static final int UPDATE_INTERVAL = 500;
    
    private int pos;
    private List<Word> gameWords;
    private GamePanel gamePanel;
    private Word word;
    
    public boolean isWorking = false;
    
    public WordRemover(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    private synchronized void checkForRemovals(){
        gameWords = gamePanel.getWordMaker().getGameWords();
        
        // Removes all words that have gone too far right from the gameWords
        // list, if the word wasn't entered then the missWord method is
        // called to indicate the user missed that word
        for(int i=0; i<gameWords.size(); i++){
            word = gameWords.get(i);
            pos = word.getXPos();
            if (pos > gamePanel.getPreferredSize().getWidth()){
                if (word.getEntered() == false){
                    gamePanel.wordMissed();
                }
                gameWords.remove(i);
            }
        }
    }

    @Override
    public void run() {
        // Iterates until the game Panel tells all threads to stop
        while (gamePanel.stopThreads == false) {
            checkForRemovals();
            
            try {
                // Sleep for a bit and give other threads a chance to run
                Thread.sleep(UPDATE_INTERVAL);  // milliseconds
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "Interrupted Thread Exception", e);
            }
        }
    }
}
