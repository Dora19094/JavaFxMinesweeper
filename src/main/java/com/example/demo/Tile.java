package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Tile extends ImageView {

    private boolean isFlagged = false, isMine, isSuperMine, isRevealed;
    private final int x, y;
    private int neighborMines = 0;

    Tile(int x, int y,double tileWidth){
        this.x = x;
        this.y = y;
        this.setImage(new Image(getClass().getResourceAsStream("images/blank.png")));
        this.setPreserveRatio(true);
        this.setFitWidth(tileWidth);
        this.setRevealed(false);
        this.setMine(false);
        this.setSuperMine(false);
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

    public void setRevealed(boolean r){
        this.isRevealed = r;
    }

    public void setMine(boolean m){
        this.isMine = m;
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

    public void setSuperMine(boolean s){
        this.isSuperMine = s;
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
