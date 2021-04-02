package speed_typer.threads;

import java.util.List;
import speed_typer.threads.WordMaker;
import speed_typer.graphics.GamePanel;
import speed_typer.data.Word;

/**
 *
 * @author Cigol
 */

// Thread class that removes all words that have moved off the screen from the
// gameWords list at 100 second intervals
public class WordRemover implements Runnable {
    private static final int UPDATE_INTERVAL = 100;
    
    private int pos;
    
    private List<Word> gameWords;
    private GamePanel game;
    private WordMaker wordMaker;
    private Word word;
    
    public WordRemover(GamePanel game, WordMaker wordMaker) {
        this.game = game;
        this.wordMaker = wordMaker;
    }

    @Override
    public void run() {
        // Iterates until the game Panel tells all threads to stop
        while (game.stopThreads == false) {
            gameWords = wordMaker.getGameWords();
            
            // Removes all words that have gone too far right from the gameWords
            // list, if the word wasn't entered then the missWord method is
            // called to indicate the user missed that word
            for(int i=0; i<gameWords.size(); i++){
                word = gameWords.get(i);
                pos = word.getXPos();
                if (pos > game.getPreferredSize().getWidth()){
                    if (word.getEntered() == false){
                        game.missWord();
                    }
                    gameWords.remove(i);
                }
            }
            
            // Sleep for a bit and give other threads a chance to run
            try { 
                Thread.sleep(UPDATE_INTERVAL);  // milliseconds
            } catch (InterruptedException ignore) {}
        }
    }
}
