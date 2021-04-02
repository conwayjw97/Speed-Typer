/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speed_typer;

import javax.swing.SwingUtilities;

/**
 *
 * @author cigol
 */
public class SpeedTyper {
    public static void main(String[] args){
        // Run GUI codes in Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameFrame gameFrame = new GameFrame(); 
                gameFrame.startGame();
            }
        });
    }
}
