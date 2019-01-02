/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpeedTyper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Cigol
 */
public class GameKeyListener implements KeyListener{
    private GamePanel game;
    private char c;
    
    GameKeyListener(GamePanel game){
        this.game = game;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // If the key typed is a character, call charEntered
        if(Character.isLetter(e.getKeyChar())){
            game.charEntered(e.getKeyChar());
        }
    }

    public void keyPressed(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {
        // If the key released is a backspace, call charDeleted
        // This was done in keyReleased because it didn't work in keyTyped
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            game.charDeleted();
        }
    }
    
}
