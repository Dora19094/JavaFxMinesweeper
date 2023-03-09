package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * The class Tile.
 */
public class Tile extends ImageView {

    private boolean isFlagged = false, isMine, isSuperMine, isRevealed;
    private final int x, y;
    private int neighborMines = 0;

    /**
     * Instantiates a new Tile.
     *
     * @param x         the x-coordinate in the gridPane
     * @param y         the y-coordinate in the gridPane
     * @param tileWidth the tile's width in the gridPane
     */
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


    /**
     * Getter method.
     *
     * @return true if the tile has been revealed else false
     */
    public boolean hasRevealed(){
        return this.isRevealed;
    }


    /**
     * Getter method.
     *
     * @return true if the tile has mine else false
     */
    public boolean hasMine(){
        return this.isMine;
    }


    /**
     * Getter method.
     *
     * @return true if the tile has been flagged else false
     */
    public boolean hasbeenFlagged(){ return this.isFlagged;}


    /**
     * Getter method.
     *
     * @return true if tile has SuperMine property else false
     */
    public boolean hasSuperMine(){
        return this.isSuperMine;
    }

    /**
     * Getter method.
     *
     * @return the number of tiles that are neighbors of the current tile
     * and have mine
     */
    public int neighborMines(){return this.neighborMines;}


    /**
     * Setter method. If "revealed" property is true the tile has been
     * revealed else it hasn't been revealed.
     *
     * @param r the "revealed" property of the tile.
     */
    public void setRevealed(boolean r){
        this.isRevealed = r;
    }

    /**
     * Setter method. It decides whether the current tile
     * has a mine or not.
     *
     * @param m is "mine" property. If it is true then the
     * tile has a mine.
     */
    public void setMine(boolean m){
        this.isMine = m;
    }


    /**
     * Setter method. If the tile has been revealed it can't been flagged.
     * If it isn't, then if it is already flagged we replace the flag-image of the
     * tile with a blank one. If it isn't already flagged we replace its image with
     * the flag-image.
     */
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


    /**
     * Setter method. It decides whether the tile has the "super-mine"
     * property or not.
     *
     * @param s the "super-mine" property. True if tile has a super-mine
     * else false.
     */
    public void setSuperMine(boolean s){
        this.isSuperMine = s;
    }

    /**
     * It increases by one the number of neighboring tiles that have mine.
     * This method is used in computing the number of mine-neighbors for
     * every tile in the gridPane.
     *
     */
    public void addNeighborMine(){
        this.neighborMines++;
    }


    /**
     * If the tile has already been revealed it does nothing.
     * Else it sets the tile's image appropriately. If it has a mine
     * then it sets the mine-image, else the image with the number of
     * neighboring mines. And if it is empty the blank image.
     */
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
