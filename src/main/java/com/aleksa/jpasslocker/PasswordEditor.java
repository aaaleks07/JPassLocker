package com.aleksa.jpasslocker;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PasswordEditor extends Application {
    public static File file;
    public static String masterpassword;
    public static Label categoryName = new Label("Press any saved passwords to edit or see");
    public static Label usernameLabel = new Label("Username");
    public static Label passwortLabel = new Label("Password:");
    public static TextField username = new TextField();
    public static PasswordField password = new PasswordField();
    public static TextField passwordUnmasked = new TextField();
    public static CheckBox showPassword = new CheckBox("Show password");
    public static Label passwordStrengthLabel = new Label("0");
    public static ProgressBar passwordStrengthProgressBar = new ProgressBar(0.0001);
    public static Button save = new Button("Save");

    @Override
    public void start(Stage stage) throws Exception {
        List<Button> buttons = new ArrayList<>();
        buttons.add(button("Hello", "MyUsername", "MyPassword"));
        buttons.add(button("Cisco", "Nutzername", "Aleksa"));
        buttons.add(button("Windows", "Aleksa", "ciscocisco123"));
        buttons.add(button("Outlook", "aleksa@email.com", "passwort"));

        window(stage, buttons);
    }

    public static void window(Stage stage, List<Button> buttons){
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox);
        stage.setMinWidth(650);
        stage.setMinHeight(350);

        HBox box = new HBox();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(box);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.prefWidthProperty().bind(vBox.widthProperty());
        scrollPane.prefHeightProperty().bind(vBox.heightProperty().divide(5));
        vBox.getChildren().add(scrollPane);

        for (int i = 0; i < buttons.size(); i++) {
            box.getChildren().add(buttons.get(i));
        }
        box.setAlignment(Pos.CENTER);

        box.setPadding(new Insets(0,20,0,20));
        box.prefHeightProperty().bind(scrollPane.prefHeightProperty().subtract(10));
        box.setSpacing(20);




        GridPane pane = new GridPane();
        GridPane.setHalignment(categoryName, HPos.CENTER);
        pane.setVgap(5);
        categoryName.setFont(new Font(30));



        save.prefWidthProperty().bind(password.widthProperty());

        username.prefWidthProperty().bind(stage.widthProperty().subtract(100));
        password.prefWidthProperty().bind(stage.widthProperty().subtract(100));
        passwordStrengthProgressBar.prefWidthProperty().bind(stage.widthProperty().subtract(100));

        StackPane passwordStrength = new StackPane(passwordStrengthProgressBar,passwordStrengthLabel);

        pane.add(categoryName,0,0);

        pane.setAlignment(Pos.CENTER);
        pane.add(usernameLabel,0,1);
        pane.add(passwortLabel,0,3);

        pane.add(username,0,2);
        pane.add(password,0,4);
        pane.add(passwordUnmasked,0,4);
        pane.add(showPassword,0,5);
        pane.add(passwordStrength,0,6);
        pane.add(save,0,7);

        passwordUnmasked.textProperty().addListener((observable, oldValue, newValue) -> {
            password.setText(newValue);
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordStrengthProgressBar.setProgress(MainClass.calculatePasswordEntropy(password.getText())/100 * 0.78125);
            passwordUnmasked.setText(newValue);

            double entropy = MainClass.calculatePasswordEntropy(password.getText());

            if(entropy < 28){
                passwordStrengthProgressBar.setStyle("-fx-accent: darkred");
                passwordStrengthLabel.setStyle("-fx-text-fill: black;");
            } else if (entropy < 36) {
                passwordStrengthProgressBar.setStyle("-fx-accent: red");
                passwordStrengthLabel.setStyle("-fx-text-fill: black;");
            } else if (entropy < 60) {
                passwordStrengthProgressBar.setStyle("-fx-accent: yellow");
                passwordStrengthLabel.setStyle("-fx-text-fill: black;");
            } else if (entropy < 128) {
                passwordStrengthProgressBar.setStyle("-fx-accent: green");
                passwordStrengthLabel.setStyle("-fx-text-fill: white;");
            } else {
                passwordStrengthProgressBar.setStyle("-fx-accent: darkgreen");
                passwordStrengthLabel.setStyle("-fx-text-fill: white;");
            }

            passwordStrengthLabel.setText(String.valueOf(entropy));
        });

        passwordUnmasked.setVisible(false);

        showPassword.setOnAction(actionEvent -> {
            if(showPassword.isSelected()){
                password.setVisible(false);
                passwordUnmasked.setVisible(true);
            }else{
                passwordUnmasked.setVisible(false);
                password.setVisible(true);
            }
        });

        usernameLabel.setVisible(false);
        username.setVisible(false);
        passwortLabel.setVisible(false);
        password.setVisible(false);
        showPassword.setVisible(false);
        passwordStrengthProgressBar.setVisible(false);
        passwordStrengthLabel.setVisible(false);
        save.setVisible(false);

        vBox.getChildren().add(pane);

        stage.setScene(scene);
        stage.show();
    }

    public static Button button(String Class, String usernameString, String passwordString){
        Button button = new Button(Class);
        button.setOnAction(actionEvent -> {
            categoryName.setText(Class);
            username.setText(usernameString);
            password.setText(passwordString);

            usernameLabel.setVisible(true);
            username.setVisible(true);
            passwortLabel.setVisible(true);
            password.setVisible(true);
            showPassword.setVisible(true);
            passwordStrengthProgressBar.setVisible(true);
            passwordStrengthLabel.setVisible(true);
            save.setVisible(true);

        });

        return button;
    }
}
