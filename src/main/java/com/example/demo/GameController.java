package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.Random;

public class GameController {

    private int[] dis;
    @FXML
    private GridPane boardEasy;
    private  Tile[][] tiles;
    @FXML
    private Label MinesLabel;
    @FXML
    private Label TimeLabel;
    @FXML
    private Label MarkedMinesLabel;
    @FXML
    private Label MessageLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private int totalNumOfBlanks;
    private int numOfFlagged = 0;
    private int numOfTries = 0;
    private int dimension;
    private Integer secondsLeft;
    private Timeline time = new Timeline();
    private boolean gameHasEnded;




    public void getDescription(int[] fileDis) {
        this.dis = fileDis;
    }

    
    public void makeBoard(){
        double tileWidth;
        this.gameHasEnded = false;
        this.secondsLeft = dis[2];
        this.numOfFlagged = 0;
        this.numOfTries = 0;

        if (dis[0] == 1) {
            this.tiles = new Tile[9][9];
            this.totalNumOfBlanks = 81 - dis[1];
            tileWidth = 42.33;
            this.dimension = 9;
        }
        else {
            this.tiles = new Tile[16][16];
            this.totalNumOfBlanks = 256 - dis[1];
            tileWidth = 23.81;
            this.dimension = 16;
        }

        TimeLabel.setText(this.secondsLeft.toString());
        MinesLabel.setText(String.valueOf(this.dis[1]));
        MarkedMinesLabel.setText("0");
        MessageLabel.setText("LET'S GO!");

        for (int i =0; i<this.dimension;i++){
            for (int j=0;j<this.dimension;j++){
                this.tiles[i][j] = new Tile(i,j,tileWidth);
                int finalI = i;
                int finalJ = j;
                this.tiles[i][j].setOnMouseClicked(e -> {

                    if (!this.tiles[finalI][finalJ].hasRevealed())
                    {
                        if (e.getButton() == MouseButton.PRIMARY)  //LEFT CLICK
                        {
                            leftClicked(finalI,finalJ);
                        }
                        else //RIGHT CLICK
                        {
                           rightClicked(finalI,finalJ);
                        }

                        MarkedMinesLabel.setText(String.valueOf(this.numOfFlagged));
                    }


                });
                boardEasy.setRowIndex(this.tiles[i][j],i);
                boardEasy.setColumnIndex(this.tiles[i][j],j);
                boardEasy.getChildren().add(this.tiles[i][j]);
            }
        }
        placeMines();
        putNeighborCount();
        writeToFileTheMines();
        time.playFromStart();
    }

    public void leftClicked(int i,int j){
        //FlagControl
        if (this.tiles[i][j].hasbeenFlagged())
        {
            this.tiles[i][j].setFlagged();
            this.numOfFlagged--;
        }
        //CheckGameOver
        if (this.tiles[i][j].hasMine())
        {
            System.out.print("GAME OVER\n");
            this.tiles[i][j].setImage(new Image(getClass().getResourceAsStream("images/wrongmine.png")));
            gameEnd(true);
        }
        //Open the tile
        else {
            this.tiles[i][j].openTile();
            this.numOfTries++;
            this.totalNumOfBlanks--;
            if (this.totalNumOfBlanks == 0) gameEnd(false);
            if (this.tiles[i][j].neighborMines() == 0) openManyTiles(i,j);
        }
    }

    public void rightClicked(int i,int j)
    {
        //Check if flag ability is permitted
        if (!(!this.tiles[i][j].hasbeenFlagged() && this.numOfFlagged == dis[1]))
        {
            if (this.tiles[i][j].hasbeenFlagged()) this.numOfFlagged--;
            else
            {
                this.numOfFlagged++;
                if (numOfTries < 5 && this.tiles[i][j].hasSuperMine()) superMineFlagged(i,j); //Check SuperMine property
            }
            this.tiles[i][j].setFlagged();
        }
    }

    public void openManyTiles(int x, int y){
        for (int i = x-1; i<=x+1;i++){
            for (int j=y-1;j<=y+1;j++){
                if (i>=0 && j>=0 && i<this.tiles.length && j<this.tiles[0].length){
                    if (this.tiles[i][j].hasRevealed()) continue;
                    if (this.tiles[i][j].hasbeenFlagged()) {
                        this.tiles[i][j].setFlagged();
                        this.numOfFlagged--;
                    }
                    this.tiles[i][j].openTile();
                    this.totalNumOfBlanks--;
                    if (this.totalNumOfBlanks == 0) gameEnd(false);
                    if (this.tiles[i][j].neighborMines() == 0) openManyTiles(i,j);
                }
            }
        }
    }

    public void saveScore(boolean lost) throws IOException{
        try
        {
            BufferedReader fr = new BufferedReader(
                    new FileReader(("src/main/resources/com/example/demo/scoreSaver"))
            );
            int lines = 0;
            String i;
            while ((i = fr.readLine()) != null && i.length()>0)
                lines++;
            fr.close();
            //System.out.println("The lines are: "+ lines+"\n");

            if (lines < 5)
            {
                FileWriter writer = new FileWriter("src/main/resources/com/example/demo/scoreSaver", true);
                writer.write(this.dis[1]+","+this.numOfTries+","+
                        this.dis[2]+","+((lost == true)?"computer":"player")+"\n");
                writer.close();
            }
            else
            {
                FileWriter writer = new FileWriter("src/main/resources/com/example/demo/scoreSaver");
                writer.write(this.dis[1]+","+this.numOfTries+","+
                        this.dis[2]+","+((lost == true)?"computer":"player")+"\n");
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the 'scoreSaver' file.");
        }
    }

    public void gameEnd(boolean result){
        time.stop();
        for (int i=0;i<this.dimension;i++){
            for (int j=0;j<this.dimension;j++){
                this.tiles[i][j].setOnMouseClicked(null);
            }
        }
        try
        {
            if (!gameHasEnded){
                saveScore(result);
                System.out.println("Score was saved");
                this.gameHasEnded = true;
            }
        } catch (IOException e) {
            System.out.println("Score could not be saved!");
        }
        if (result) MessageLabel.setText("GAME OVER");
        else MessageLabel.setText("YOU WON!");
    }

    public void placeMines(){
        int count = this.dis[1];
        int i = 0;
        int[] mineArray = new int[count * 2];
        while (count != 0){
            int row = (int) (Math.random() * this.tiles.length);
            int col = (int) (Math.random() * this.tiles[0].length);
            if (this.tiles[row][col].hasMine()) continue;
            this.tiles[row][col].setMine(true);
            count--;
            mineArray[i++] = row;
            mineArray[i++] = col;
        }
        //place SuperMine :
        if (dis[3] == 1) {
            Random rand = new Random();
            int randIndex = rand.nextInt(mineArray.length / 2) * 2;
            this.tiles[mineArray[randIndex]][mineArray[randIndex+1]].setSuperMine(true);
            //this.tiles[0][0].setMine();
            //this.tiles[0][0].setSuperMine();
        }
    }

    public void superMineFlagged(int x,int y){
        this.tiles[x][y].setFlagged();
        System.out.println("SuperMine property activated!\n");
        for (int i = 0;i<this.dimension;i++){
            if (this.tiles[x][i].hasbeenFlagged())
            {
                this.tiles[x][i].setFlagged();
                this.numOfFlagged-=1;
            }
            if (this.tiles[i][y].hasbeenFlagged())
            {
                this.tiles[i][y].setFlagged();
                this.numOfFlagged-=1;
            }

            if (this.tiles[x][i].hasMine())
            {
                this.tiles[x][i].setImage(new Image(getClass().getResourceAsStream("images/hitmine.png")));
                this.tiles[x][i].setRevealed(true);
            }
            else {
                this.tiles[x][i].openTile();
                this.totalNumOfBlanks--;
            }
            if (this.tiles[i][y].hasMine())
            {
                this.tiles[i][y].setImage(new Image(getClass().getResourceAsStream("images/hitmine.png")));
                this.tiles[i][y].setRevealed(true);
            }
            else
            {
                this.tiles[i][y].openTile();
                this.totalNumOfBlanks--;
            }
            if (this.totalNumOfBlanks == 0) gameEnd(false);
        }
    }

    public void putNeighborCount(){
        int[] neighbors = {-1,0, 0,-1, -1,-1, -1,1, 1,-1, 1,0, 0,1, 1,1};

        for (int i = 0;i<this.tiles.length;i++){
            for (int j = 0;j<this.tiles[0].length;j++){
                for(int n=0;n<neighbors.length;n+=2){
                    if (this.tiles[i][j].hasMine()) continue;
                    int newX = i + neighbors[n];
                    int newY = j + neighbors[n+1];
                    if (newX >= this.tiles.length || newY >= this.tiles[0].length || newX<0 || newY <0) continue;
                    if (this.tiles[newX][newY].hasMine()) this.tiles[i][j].addNeighborMine();
                }
            }
        }

    }

    public void revealAll(){
        for (int i =0;i<this.tiles.length;i++){
            for (int j=0;j<this.tiles[0].length;j++){
                if (this.tiles[i][j].hasRevealed()) continue;
                this.tiles[i][j].openTile();
            }
        }
    }

    public void doTime(){
        this.time.setCycleCount(Timeline.INDEFINITE);
        if (this.time!=null)
            this.time.stop();
        KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (secondsLeft <= 0)
                {
                    gameEnd(true);
                    return;
                }


                secondsLeft--;
                //System.out.println(String.valueOf(numOfTries));
                TimeLabel.setText(secondsLeft.toString());
            }
        });
        time.getKeyFrames().add(frame);
    }

    public void writeToFileTheMines(){
        try {
            FileWriter writer = new FileWriter("src/main/resources/com/example/demo/mediaLab/mines.txt");
            for (int i =0;i<this.dimension;i++){
                for (int j =0;j<this.dimension;j++){
                    if (this.tiles[i][j].hasMine())
                        writer.write(String.valueOf(i+1) + "," + String.valueOf(j+1) + "," + String.valueOf((this.tiles[i][j].hasSuperMine() ==true)?1:0) + "\n");
                }
            }
            writer.close();
            System.out.println("Mines-thesis written to mines.txt successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the mines.txt file.");
        }
    }


    public void newGame(ActionEvent event) throws IOException {
        time.stop();
        Parent root = FXMLLoader.load(getClass().getResource("MenuPage.fxml"));
        stage = (Stage)boardEasy.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void restartGame(ActionEvent event) throws IOException {
        makeBoard();
    }

    public void showScore(ActionEvent event) throws IOException{
        time.stop();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScorePage.fxml"));
        root = loader.load();
        ScoreController scoreController = loader.getController();
        scoreController.getDescription(this.dis);
        scoreController.setScore();

        stage = (Stage)boardEasy.getScene().getWindow();
        scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void exitGame(){
        time.stop();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout!");
        alert.setContentText("Your score will be saved");

        if (alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) boardEasy.getScene().getWindow();
            System.out.print("You successfully logged out");
            stage.close();
        }
    }

    public void solutionOfGame(ActionEvent event) throws IOException {
        revealAll();
        gameEnd(true);
    }


}
