package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScoreController {
    @FXML
    private Label mines1Label;
    @FXML
    private Label moves1Label;
    @FXML
    private Label time1Label;
    @FXML
    private Label winner1Label;
    @FXML
    private Label mines2Label;
    @FXML
    private Label moves2Label;
    @FXML
    private Label time2Label;
    @FXML
    private Label winner2Label;
    @FXML
    private Label mines3Label;
    @FXML
    private Label moves3Label;
    @FXML
    private Label time3Label;
    @FXML
    private Label winner3Label;
    @FXML
    private Label mines4Label;
    @FXML
    private Label moves4Label;
    @FXML
    private Label time4Label;
    @FXML
    private Label winner4Label;
    @FXML
    private Label mines5Label;
    @FXML
    private Label moves5Label;
    @FXML
    private Label time5Label;
    @FXML
    private Label winner5Label;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int[] des = new int[4];

    public void getDescription(int[] des){this.des = des;}


    public void setScore(){
        //TODO
        try{
            BufferedReader fr = new BufferedReader(
                    new FileReader("src/main/resources/com/example/demo/scoreSaver")
            );

            String i;
            int lines = 0;
            while ((i = fr.readLine()) != null && i.length()>0) {
                lines++;
                String[] arr = i.split(",");
                switch(lines){
                    case 1:
                        mines1Label.setText(String.valueOf(arr[0]));
                        moves1Label.setText(String.valueOf(arr[1]));
                        time1Label.setText(String.valueOf(arr[2]));
                        winner1Label.setText(String.valueOf(arr[3]));
                        break;
                    case 2:
                        mines2Label.setText(String.valueOf(arr[0]));
                        moves2Label.setText(String.valueOf(arr[1]));
                        time2Label.setText(String.valueOf(arr[2]));
                        winner2Label.setText(String.valueOf(arr[3]));
                        break;
                    case 3:
                        mines3Label.setText(String.valueOf(arr[0]));
                        moves3Label.setText(String.valueOf(arr[1]));
                        time3Label.setText(String.valueOf(arr[2]));
                        winner3Label.setText(String.valueOf(arr[3]));
                        break;
                    case 4:
                        mines4Label.setText(String.valueOf(arr[0]));
                        moves4Label.setText(String.valueOf(arr[1]));
                        time4Label.setText(String.valueOf(arr[2]));
                        winner4Label.setText(String.valueOf(arr[3]));
                        break;
                    case 5:
                        mines5Label.setText(String.valueOf(arr[0]));
                        moves5Label.setText(String.valueOf(arr[1]));
                        time5Label.setText(String.valueOf(arr[2]));
                        winner5Label.setText(String.valueOf(arr[3]));
                        break;
                }
            }
        }catch(IOException e){
            System.out.println("Error reading score!\n");
        }
    }

    public void backToGame(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("GamePage.fxml"));
        root = loader.load();

        GameController gameController = loader.getController();
        gameController.getDescription(this.des);
        gameController.doTime();
        gameController.makeBoard();


        stage = (Stage)mines1Label.getScene().getWindow();
        scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }




}
