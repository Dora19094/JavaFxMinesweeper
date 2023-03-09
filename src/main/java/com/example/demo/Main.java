package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application{

    public static void main(String[] args){
            launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MenuPage.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("MediaLab Minesweeper");

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/minesweeper-thumbnail.png")));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            logout(stage);
        });
        }


    public void logout(Stage stage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout!");
        alert.setContentText("Your score will be saved");

        if (alert.showAndWait().get() == ButtonType.OK){
            System.out.print("You successfully logged out");
            stage.close();
        }


    }
}
