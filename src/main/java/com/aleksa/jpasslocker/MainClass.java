package com.aleksa.jpasslocker;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

import static com.aleksa.jpasslocker.GlobalVariables.file;
import static com.aleksa.jpasslocker.GlobalVariables.mainStage;

/**
 * @author Nikolic Aleksa (aleksa.nikolic@htl.rennweg.at)
 */

public class MainClass extends Application {

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        startStage(mainStage);
    }

    /**
     * Starts the main stage (First stage on program start)
     * @param stage
     */
    public void startStage(Stage stage) {
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid);
        stage.setMinWidth(400);
        stage.setMinHeight(300);

        grid.setAlignment(Pos.CENTER);

        grid.setGridLinesVisible(false);

        Label title = new Label("JPassLocker");
        title.setFont(new Font(45));
        title.setAlignment(Pos.CENTER);
        GridPane.setHalignment(title, HPos.CENTER);

        Label pathLabel = new Label();

        Button exit = new Button("Exit");
        Button open = new Button("Open");
        Button create = new Button("Create New Database");

        exit.prefWidthProperty().bind(grid.widthProperty().subtract(50));
        open.prefWidthProperty().bind(grid.widthProperty().subtract(50));
        create.prefWidthProperty().bind(grid.widthProperty().subtract(50));


        open.setOnAction(actionEvent -> {
            boolean succes = false;
            while (!succes){
                try{
                    FileChooser fileDialog = new FileChooser();
                    //fileDialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPassLocker Database", ".jpldb"));
                    fileDialog.setInitialDirectory(new File(System.getProperty("user.home")));
                    file = fileDialog.showOpenDialog(stage);
                    pathLabel.setText(file.getAbsolutePath());
                    GlobalVariables.setAllVariables(file.toPath());
                    GlobalVariables.fileName = file.getName();
                    succes = true;
                }catch (IllegalArgumentException e){
                    System.out.println("Wrong file ending");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Wrong file ending");
                    alert.setContentText("Please choose a file with a .jpldb ending");
                    alert.showAndWait(); 
                }catch (NullPointerException e){
                    System.out.println("No File selected");
                    break;
                }
            }

            if(succes){
                stage.setScene(openDatabase.openDatabase());
            }
        });

        create.setOnAction(actionEvent -> {stage.setScene(createDatabase.createDatabase(stage));} );
        exit.setOnAction(actionEvent -> {System.exit(0);});


        grid.add(title,0,0);
        grid.add(pathLabel,0,1);
        grid.add(create,0,2);
        grid.add(open,0,3);
        grid.add(exit,0,4);

        grid.setVgap(10);


        stage.setScene(scene);
        stage.show();
    }


    /**
     * Calculates the entropy of a password for the password strength bar
     * @param password
     * @return
     */
    public static double calculatePasswordEntropy(String password) {
        String charSet = "";
        if (password.matches(".*[a-z].*")) {
            charSet += "abcdefghijklmnopqrstuvwxyz";
        }
        if (password.matches(".*[A-Z].*")) {
            charSet += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if (password.matches(".*\\d.*")) {
            charSet += "0123456789";
        }
        if (password.matches(".*[\\p{Punct}].*")) {
            charSet += "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        }

        int charSetSize = charSet.length();
        int passwordLength = password.length();

        return passwordLength * Math.log(charSetSize) / Math.log(2);
    }


    public static void main(String[] args){
        launch();
    }

}
