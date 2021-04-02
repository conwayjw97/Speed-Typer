package speed_typer.data;

/**
 *
 * @author Cigol
 */

// The class for individual words, everything in it seems pretty self explanatory
public class Word {
    private int yPos, xPos;
    private boolean used, entered;
    
    private String word;
    
    Word(String word){
        this.word = word;
        used = false;
        entered = false;
    }
    
    public void setXPos(int xPos){
        this.xPos = xPos;
    }
    
    public void setYPos(int yPos){
        this.yPos = yPos;
    }
    
    public void setUse(boolean status){
        used = status;
    }
    
    public void setEntered(boolean status){
        entered = status;
    }
    
    public int getXPos(){
        return xPos;
    }
    
    public int getYPos(){
        return yPos;
    }
    
    public boolean getUse(){
        return used;
    }
    
    public boolean getEntered(){
        return entered;
    }
    
    public String getWord(){
        return word;
    }
}
