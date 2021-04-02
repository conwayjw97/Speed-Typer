package speed_typer;

import speed_typer.graphics.GamePanel;
import speed_typer.listeners.GameKeyListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 *
 * @author Cigol
 */
public class GameFrame extends JFrame{
    private static final int X_SIZE = 1400, Y_SIZE = 800;
    
    private GamePanel gamePanel;
    
    private BufferedImage cursorImg;
    private Cursor blankCursor;
    
    
    public GameFrame(){
        gamePanel = new GamePanel();
    }
    
    public void startGame(){
        // Set frame size
        gamePanel.setPreferredSize(new Dimension(X_SIZE, Y_SIZE));
        
        // Action listeners are implemented in the frame declaration
        // These serve to detect the user's hardware input actions
        gamePanel.addKeyListener(new GameKeyListener(gamePanel));
        
        // Turn cursor invisible when inside the Frame
        // Transparent 16 x 16 pixel cursor image.
        cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        // Create a new blank cursor.
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        // Set the blank cursor to the JFrame.
        gamePanel.setCursor(blankCursor);
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        
        // Frame settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Speed Typer");
        setSize(X_SIZE, Y_SIZE);
        setResizable(false);
        setVisible(true);
        
        // Put game on the frame
        add(gamePanel);
    }
}
