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
    private char c;
    
    public GameKeyListener(GamePanel game){
        this.game = game;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // If the key typed is a character, call charEntered
        if(Character.isLetter(e.getKeyChar())){
            game.charEntered(e.getKeyChar());
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // If the key released is a backspace, call charDeleted
        // This was done in keyReleased because it didn't work in keyTyped
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            game.charDeleted();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Unsupported
    }
    
}