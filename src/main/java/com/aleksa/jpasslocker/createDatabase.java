package com.aleksa.jpasslocker;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.aleksa.jpasslocker.*;
import com.aleksa.jpasslocker.GlobalVariables.*;

import static com.aleksa.jpasslocker.GlobalVariables.file;

public class createDatabase {
    /**
     * Scene for creating a new database
     * @param stage
     * @return
     */
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
            double entropy = MainClass.calculatePasswordEntropy(passwordInput.getText());
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

            GlobalVariables.password = passwordInput.getText();

            /**
             * DEBUGGING
             */
            System.out.println(GlobalVariables.password);
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

                String writtenData = databaseNameInput.getText() + System.lineSeparator();
                databaseFileBegin.write(EncryptDecrypt.encrypt(writtenData, GlobalVariables.password));
                databaseFileBegin.close();

            } catch (Exception e) {
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
}
