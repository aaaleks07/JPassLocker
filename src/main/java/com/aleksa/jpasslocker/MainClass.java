package com.aleksa.jpasslocker;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;

public class MainClass extends Application {

    public static File file;
    public static String passHashed;

    @Override
    public void start(Stage stage) {
        startStage(stage);
        //createDatabase(stage);
    }

    public static Scene createDatabase(Stage stage){
        GridPane gridDatabase = new GridPane();
        Scene sceneDatabase = new Scene(gridDatabase);
        stage.setMinWidth(400);
        stage.setMinHeight(350);

        gridDatabase.setPadding(new Insets(-30,0,0,0));

        gridDatabase.setAlignment(Pos.CENTER);

        Label databaseTitle = new Label("Create Database");
        databaseTitle.setFont(new Font(35));

        GridPane.setHalignment(databaseTitle, HPos.CENTER);

        Label databaseName = new Label("Name of Database");
        TextField databaseNameInput = new TextField();
        VBox dataBaseNameBox = new VBox(databaseName,databaseNameInput);

        ProgressBar passStrengthBar = new ProgressBar(0);
        Label entropyText = new Label("0");
        StackPane passStrengthStackpane = new StackPane(passStrengthBar,entropyText);

        Label password = new Label("Password");
        PasswordField passwordInput = new PasswordField();
        passwordInput.textProperty().addListener(observable -> {
            double entropy = calculatePasswordEntropy(passwordInput.getText());
            System.out.println(entropy);

            if(entropy < 28){
                passStrengthBar.setStyle("-fx-accent: darkred");
                entropyText.setStyle("-fx-text-fill: black;");
            } else if (entropy < 36) {
                passStrengthBar.setStyle("-fx-accent: red");
                entropyText.setStyle("-fx-text-fill: black;");
            } else if (entropy < 60) {
                passStrengthBar.setStyle("-fx-accent: yellow");
                entropyText.setStyle("-fx-text-fill: black;");
            } else if (entropy < 128) {
                passStrengthBar.setStyle("-fx-accent: green");
                entropyText.setStyle("-fx-text-fill: white;");
            } else {
                passStrengthBar.setStyle("-fx-accent: darkgreen");
                entropyText.setStyle("-fx-text-fill: white;");
            }
            entropyText.setText(String.valueOf((double) Math.round(entropy*100) / 100));

            passStrengthBar.setProgress(entropy/100 * 0.78125);
        });
        VBox passwordBox = new VBox(password,passwordInput);

        passStrengthBar.prefWidthProperty().bind(gridDatabase.widthProperty().subtract(100));
        passStrengthBar.setBorder(Border.EMPTY);

        Button create = new Button("Create new Database");
        HBox createBox = new HBox(create);
        createBox.setPadding(new Insets(10,0,0,0));
        create.prefWidthProperty().bind(passStrengthBar.prefWidthProperty());
        create.setOnAction(actionEvent -> {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Create file");
            filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPassLocker Database", "*.jpldb"));

            file = filechooser.showSaveDialog(stage);
            try {
                Files.createFile(Path.of(Path.of(file.getAbsolutePath()) + ".jpldb"));
                BufferedWriter databaseFileBegin = Files.newBufferedWriter(
                        Paths.get(file.getAbsolutePath() + ".jpldb"),
                        StandardCharsets.UTF_8
                );

                databaseFileBegin.write(databaseNameInput.getText() + System.lineSeparator());
                passHashed = String.valueOf(passwordInput.getText().hashCode() + System.lineSeparator());
                databaseFileBegin.write(passwordInput.getText().hashCode() + System.lineSeparator());
                databaseFileBegin.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        gridDatabase.setVgap(10);
        gridDatabase.add(databaseTitle,0,1);
        gridDatabase.add(dataBaseNameBox,0,3);
        gridDatabase.add(passwordBox,0,4);
        gridDatabase.add(passStrengthStackpane,0,5);
        gridDatabase.add(createBox, 0,6);

        return sceneDatabase;
    }


    public static void startStage(Stage stage) {
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
            try{
                FileChooser fileDialog = new FileChooser();
                File file = fileDialog.showOpenDialog(stage);
                pathLabel.setText(file.getAbsolutePath());
            }catch (Exception e){
                System.out.println("No File opened");
            }
        });

        create.setOnAction(actionEvent -> {stage.setScene(createDatabase(stage));} );
        exit.setOnAction(actionEvent -> {System.exit(0);});


        grid.add(title,0,0);
        grid.add(pathLabel,0,1);
        grid.add(create,0,2);
        grid.add(open,0,3);
        grid.add(exit,0,4);

        grid.setVgap(10);


        stage.setScene(scene);
        stage.show();


        /**
         * Create Database Scene
         */

        GridPane createDataBaseGrid = new GridPane();
        Scene createDatabase = new Scene(createDataBaseGrid);
    }

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


    public static void main(String[] args) {
        launch();
    }

}
