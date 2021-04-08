/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speed_typer.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import speed_typer.data.Word;
import speed_typer.threads.WordMaker;

/**
 *
 * @author cigol
 */
public class PanelPainter {
    private WordMaker wordMaker;
    private Font font;
    private FontMetrics metrics;
    private int width;
    private int height;
    
    PanelPainter(WordMaker wordMaker){
        this.wordMaker = wordMaker;
        
        width = 0;
        height = 0;
    }
    
    public void setDimensions(int width, int height){
        if(this.width==0 && this.height==0){
            this.width = width;
            this.height = height;
        }
    }
    
    public void paintScoreBox(Graphics g, int score, int wordsMissed){
        // Draw scorebox
        font = new Font("Terminal", Font.ITALIC, 45);
        g.setFont(font);
        g.setColor(Color.white);
        g.fillRect(0, 45, width, 5);
        g.drawString("SCORE: " + score + "   WORDS MISSED: " + wordsMissed + "/3", 5, 37);
    }
    
    public void paintScreenHeight(Graphics g){
        for(int i=0; i<height; i+=50){
            g.drawString(String.valueOf(i), width-100, i);
            g.drawLine(width-100, i, width, i);
        }
    }
    
    public void paintInputBox(Graphics g, String wordInput){
        // Draw userinput and input box
        font = new Font("Terminal", Font.ITALIC, 45);
        g.setColor(Color.white);
        g.fillRect(0, height-50, width, 5);
        g.drawString(wordInput, 5, height-5);
    }
    
    public void paintGameWords(Graphics g){
        // Draw the gameWords
        font = new Font("Terminal", Font.PLAIN, 45);
        g.setFont(font);
        List<Word> gameWords = wordMaker.getGameWords();
        for(int i=0; i<gameWords.size(); i++){
            Word word = gameWords.get(i);
            if(word.getEntered() == true){
                g.setColor(Color.darkGray);
            }
            else if(word.getEntered() == false){
                g.setColor(Color.white);
            }
            g.drawString(word.getWord(), word.getXPos(), word.getYPos());
            
            // [DEBUGGING, DELETE IF NECESSARY] Paint the coordinates of the word
            // below it
            if(GamePanel.GRAPHICAL_DEBUG_MODE){
                font = new Font("Terminal", Font.PLAIN, 12);
                g.setFont(font);
                g.drawString(String.valueOf(word.getXPos()), word.getXPos(), word.getYPos()+10);
                g.drawString(String.valueOf(word.getYPos()), word.getXPos()+30, word.getYPos()+10);
            }
        }
    }
    
    public void paintGameOverScreen(Graphics g, int score){
        // Draw GameOver screen, if the user has lost
        g.setColor(Color.white);
        g.fillRect(0, 50, width, height-50-45);
        g.setColor(Color.black);

        font = new Font("Terminal", Font.PLAIN, 128);
        paintCenteredText(g, font, "FINAL SCORE: " + Integer.toString(score), (height / 2)-20);

        font = new Font("Terminal", Font.PLAIN, 64);
        paintCenteredText(g, font, "FINAL SCORE: " + Integer.toString(score), (height / 2)+40);
        paintCenteredText(g, font, "PRESS R TO RESTART", (height / 2)+100);
    }
    
    public void paintGameIntroScreen(Graphics g){
        // Draw GameOver screen, if the user has lost
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);

        font = new Font("Terminal", Font.PLAIN, 128);
        paintCenteredText(g, font, "SPEED TYPER", (height / 2)-20);

        font = new Font("Terminal", Font.PLAIN, 64);
        paintCenteredText(g, font, "Press ENTER to Start", (height / 2)+40);
    }
    
    private void paintCenteredText(Graphics g, Font font, String text, int y){
        metrics = g.getFontMetrics(font);
        int x = (width - metrics.stringWidth(text)) / 2;

        g.setFont(font); 
        g.drawString(text, x, y);
    }
}
