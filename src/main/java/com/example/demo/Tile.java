package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Tile extends ImageView {

    private boolean isFlagged = false, isMine = false, isSuperMine = false, isRevealed = false;
    private final int x, y;
    private int neighborMines = 0;

    Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getRow(){
        return x;
    }

    public int getColumn(){
        return y;
    }

    public boolean hasRevealed(){
        return this.isRevealed;
    }

    public boolean hasMine(){
        return this.isMine;
    }

    public boolean hasbeenFlagged(){ return this.isFlagged;}

    public boolean hasSuperMine(){
        return this.isSuperMine;
    }

    public int neighborMines(){return this.neighborMines;}

    public void setRevealed(){
        this.isRevealed = true;
    }

    public void setMine(){
        this.isMine = true;
    }

    public void setFlagged(){
        if (this.hasRevealed()) return;
        else {
            if (this.isFlagged == true) {
                this.isFlagged = false;
                this.setImage(new Image(getClass().getResourceAsStream("images/blank.png")));
            } else {
                this.isFlagged = true;
                this.setImage(new Image(getClass().getResourceAsStream("images/flag.png")));
            }
        }
    }

    public void setSuperMine(){
        this.isSuperMine = true;
    }
    public void addNeighborMine(){
        this.neighborMines++;
    }

    public void openTile(){
        if (this.hasRevealed()) return;
        this.isRevealed = true;

        if (this.isMine == true)
            this.setImage(new Image(getClass().getResourceAsStream("images/mine.png")));

        else if(this.neighborMines == 0)
            this.setImage(new Image(getClass().getResourceAsStream("images/exposed.png")));
        else
            this.setImage(new Image(getClass().getResourceAsStream("images/number"+this.neighborMines+".png")));
    }






}
