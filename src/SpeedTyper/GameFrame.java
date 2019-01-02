package SpeedTyper;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 *
 * @author Cigol
 */
public class GameFrame extends JFrame{
    private static final int X_SIZE = 1200, Y_SIZE = 800;
    
    private GamePanel game;
    
    private BufferedImage cursorImg;
    private Cursor blankCursor;
    
    
    public GameFrame(){
        game = new GamePanel();
        game.setPreferredSize(new Dimension(X_SIZE, Y_SIZE));
        
        // Action listeners are implemented in the frame declaration
        // These serve to detect the user's hardware input actions
        game.addKeyListener(new GameKeyListener(game));
        
        // Turn cursor invisible when inside the Frame
        // Transparent 16 x 16 pixel cursor image.
        cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        // Create a new blank cursor.
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        // Set the blank cursor to the JFrame.
        game.setCursor(blankCursor);
        game.setFocusable(true);
        game.requestFocusInWindow();
        
        // Frame settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Speed Typer");
        setSize(X_SIZE, Y_SIZE);
        setResizable(false);
        setVisible(true);
        
        // Put game on the frame
        add(game);
    }
    
    public static void main(String[] args){
        // Run GUI codes in Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame(); // Let the constructor do the job
            }
        });
    }
}
