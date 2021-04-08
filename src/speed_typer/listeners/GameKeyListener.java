/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speed_typer.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import speed_typer.graphics.GamePanel;

/**
 *
 * @author Cigol
 */
public class GameKeyListener implements KeyListener{
    private GamePanel game;
    
    public GameKeyListener(GamePanel game){
        this.game = game;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // If the key typed is a character, call charEntered
        if(Character.isLetter(e.getKeyChar())){
            game.charEntered(e.getKeyChar());
        }
        // If the key typed is a digit, call numEntered
        else if(Character.isDigit(e.getKeyChar())){
            game.numEntered(Character.getNumericValue(e.getKeyChar()));
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // These were done in keyReleased because it didn't work in keyTyped
        switch(e.getKeyCode()){
            case KeyEvent.VK_ENTER:
                game.enterPressed();
                break;
            // If the key released is a backspace, call charDeleted
            case KeyEvent.VK_BACK_SPACE:
                game.charDeleted();
                break;
            // If the key released is delete, call wordDeleted
            case KeyEvent.VK_DELETE:
                game.wordDeleted();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }
    
}
