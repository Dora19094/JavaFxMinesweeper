package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class MenuController {
    @FXML
    private TextField MenuTextField;
    @FXML
    private TextField NewScenarioId;
    @FXML
    private TextField DifficultyLevel;
    @FXML
    private TextField MinesNumber;
    @FXML
    private TextField SuperMineAdded;
    @FXML
    private TextField AvailableTime;
    @FXML
    private AnchorPane menuPane;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label WarningLabel;
    private int[] discription;
    private boolean readyToStart = false;

    public void loadFile(ActionEvent event) throws IOException {
        this.discription = new int[4];
        String filename = MenuTextField.getText();
        try {
            BufferedReader fr = new BufferedReader(
                    new FileReader("src/main/resources/com/example/demo/mediaLab/SCENARIO"+filename+".txt")
            );

            String i = "";
            int j = 0;
            while ((i = fr.readLine()) != null && i.length()>0) {
                this.discription[j++] = Integer.parseInt(i);
                //System.out.println(this.discription[j-1]);
            }
            //Checks
            if (j!=4) throw new InvalidDescriptionException("The desciption structure is wrong");
            if (this.discription[0] != 1 && this.discription[0] != 2) throw new InvalidValueException("Wrong desciption's values");
            switch(this.discription[0]){
                case 1: if (this.discription[1] < 9 || this.discription[1] > 11) throw new InvalidValueException("Wrong desciption's values");
                    if (this.discription[2] < 120 || this.discription[2] > 180) throw new InvalidValueException("Wrong desciption's values");
                    if (this.discription[3] != 0) throw new InvalidValueException("Wrong desciption's values");
                    break;
                case 2 : if (this.discription[1] < 35 || this.discription[1] > 45) throw new InvalidValueException("Wrong desciption's values");
                    if (this.discription[2] < 240 || this.discription[2] > 360) throw new InvalidValueException("Wrong desciption's values");
                    if (this.discription[3] != 0 && this.discription[3] != 1) throw new InvalidValueException("Wrong desciption's values");
                    break;
                default: throw new InvalidValueException("Wrong desciption's values");
            }

            //System.out.println(j);
            WarningLabel.setText("OK!");
            readyToStart = true;

        }catch(FileNotFoundException e){
            readyToStart = false;
            WarningLabel.setText("File not found. Please try again!");
        }catch(InvalidDescriptionException e){
            readyToStart = false;
            WarningLabel.setText("Wrong desciption structure");
        }catch(InvalidValueException e){
            readyToStart = false;
            WarningLabel.setText("Wrong desciption's values");
        }
    }



    public void startGame(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Level1.fxml"));
        root = loader.load();
        if (!readyToStart)
        {
            WarningLabel.setText("Load a valid filename or create a valid game!");
            return;
        }

        GameController gameController = loader.getController();
        gameController.getDescription(this.discription);
        gameController.makeBoard();
        gameController.doTime();


        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public boolean createFile(String id){
        try {
            File folder = new File("src/main/resources/com/example/demo/mediaLab");
            File file = new File(folder, ("SCENARIO" + id + ".txt"));
            boolean result = file.createNewFile();
            if (result) {
                System.out.println("File created successfully.");
                return true;
            } else {
                readyToStart = false;
                WarningLabel.setText("This scenario already exists!");
                System.out.println("File already exists.");
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            readyToStart = false;
            return false;
        }

    }

    public void createGame() throws IOException{
        this.discription = new int[4];
        String id = NewScenarioId.getText();

        try {
            if  (DifficultyLevel.getText().isEmpty() || MinesNumber.getText().isEmpty()
                    || AvailableTime.getText().isEmpty() || SuperMineAdded.getText().isEmpty())
                throw new InvalidDescriptionException("The desciption structure is wrong");

            this.discription[0] = Integer.parseInt(DifficultyLevel.getText());
            this.discription[1] = Integer.parseInt(MinesNumber.getText());
            this.discription[2] = Integer.parseInt(AvailableTime.getText());
            this.discription[3] = Integer.parseInt(SuperMineAdded.getText());

            //Check values :
            if (this.discription[0] != 1 && this.discription[0] != 2) throw new InvalidValueException("Wrong desciption's values");
            switch(this.discription[0]){
                case 1: if (this.discription[1] < 9 || this.discription[1] > 11) throw new InvalidValueException("Wrong desciption's values");
                    if (this.discription[2] < 120 || this.discription[2] > 180) throw new InvalidValueException("Wrong desciption's values");
                    if (this.discription[3] != 0) throw new InvalidValueException("Wrong desciption's values");
                    break;
                case 2 : if (this.discription[1] < 35 || this.discription[1] > 45) throw new InvalidValueException("Wrong desciption's values");
                    if (this.discription[2] < 240 || this.discription[2] > 360) throw new InvalidValueException("Wrong desciption's values");
                    if (this.discription[3] != 0 && this.discription[3] != 1) throw new InvalidValueException("Wrong desciption's values");
                    break;
                default: throw new InvalidValueException("Wrong desciption's values");
            }
            if (!createFile(id)) return;
            FileWriter writer = new FileWriter("src/main/resources/com/example/demo/mediaLab/SCENARIO"+id+".txt");
            for (int i =0;i<4;i++)
                writer.write(String.valueOf(this.discription[i]) + "\n");

            writer.close();
            System.out.println("Data written to file successfully.");
            readyToStart = true;
            WarningLabel.setText("OK!");

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            readyToStart = false;
            e.printStackTrace();
        }catch(NullPointerException e){
            readyToStart = false;
            WarningLabel.setText("File not found. Please try again!");
        }catch(InvalidDescriptionException e){
            readyToStart = false;
            WarningLabel.setText("Wrong desciption structure");
        }catch(InvalidValueException e){
            readyToStart = false;
            WarningLabel.setText("Wrong desciption's values");
        }

    }

}
