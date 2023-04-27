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
import com.aleksa.jpasslocker.EncryptDecrypt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PasswordEditor extends Application {
    private static final double ENTROPY_THRESHOLD_1 = 28;
    private static final double ENTROPY_THRESHOLD_2 = 36;
    private static final double ENTROPY_THRESHOLD_3 = 60;
    private static final double ENTROPY_THRESHOLD_4 = 128;

    private Label categoryName = new Label("Press any saved passwords to edit or see");
    private Label usernameLabel = new Label("Username");
    private Label passwordLabel = new Label("Password:");
    private TextField username = new TextField();
    private PasswordField password = new PasswordField();
    private TextField passwordUnmasked = new TextField();
    private CheckBox showPassword = new CheckBox("Show password");
    private Label passwordStrengthLabel = new Label("0");
    private ProgressBar passwordStrengthProgressBar = new ProgressBar(0.0001);
    private Button save = new Button("Save");

    @Override
    public void start(Stage stage) {
        List<Button> buttons = List.of(
                createButton("Hello", "MyUsername", "MyPassword"),
                createButton("Cisco", "Nutzername", "Aleksa"),
                createButton("Windows", "Aleksa", "ciscocisco123"),
                createButton("Outlook", "aleksa@email.com", "passwort")
        );

        window(stage, buttons);
    }

    private void window(Stage stage, List<Button> buttons) {
        VBox root = new VBox();
        Scene scene = new Scene(root);
        stage.setMinWidth(650);
        stage.setMinHeight(350);

        ScrollPane buttonScrollPane = createButtonBox(buttons, root);
        GridPane passwordEditorPane = createPasswordEditorPane(stage);

        root.getChildren().addAll(buttonScrollPane, passwordEditorPane);
        stage.setScene(scene);
        stage.show();
    }

    private ScrollPane createButtonBox(List<Button> buttons, VBox root) {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(0, 20, 0, 20));
        buttonBox.getChildren().addAll(buttons);

        ScrollPane scrollPane = new ScrollPane(buttonBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.prefWidthProperty().bind(root.widthProperty());
        scrollPane.prefHeightProperty().bind(root.heightProperty().divide(5));
        buttonBox.prefHeightProperty().bind(scrollPane.prefHeightProperty().subtract(10));

        return scrollPane;
    }

    private GridPane createPasswordEditorPane(Stage stage) {
        GridPane passwordEditorPane = new GridPane();
        passwordEditorPane.setVgap(5);
        passwordEditorPane.setAlignment(Pos.CENTER);
        categoryName.setFont(new Font(30));
        GridPane.setHalignment(categoryName, HPos.CENTER);

        save.prefWidthProperty().bind(password.widthProperty());
        username.prefWidthProperty().bind(stage.widthProperty().subtract(100));
        password.prefWidthProperty().bind(stage.widthProperty().subtract(100));
        passwordStrengthProgressBar.prefWidthProperty().bind(stage.widthProperty().subtract(100));

        StackPane passwordStrength = new StackPane(passwordStrengthProgressBar, passwordStrengthLabel);
        passwordEditorPane.add(categoryName, 0, 0);
        passwordEditorPane.add(usernameLabel, 0, 1);
        passwordEditorPane.add(passwordLabel, 0, 3);
        passwordEditorPane.add(username, 0, 2);
        passwordEditorPane.add(password, 0, 4);
        passwordEditorPane.add(passwordUnmasked, 0, 4);
        passwordEditorPane.add(showPassword, 0, 5);
        passwordEditorPane.add(passwordStrength, 0, 6);
        passwordEditorPane.add(save, 0, 7);

        configurePasswordFields();
        configureShowPasswordCheckbox();

        hidePasswordEditorControls();

        return passwordEditorPane;
    }

    private void configurePasswordFields() {
        passwordUnmasked.textProperty().addListener((observable, oldValue, newValue) -> {
            password.setText(newValue);
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            double entropy = MainClass.calculatePasswordEntropy(newValue);
            updatePasswordStrength(entropy);

            passwordUnmasked.setText(newValue);
            passwordStrengthLabel.setText(String.valueOf(entropy));
        });
    }

    private void configureShowPasswordCheckbox() {
        showPassword.setOnAction(event -> {
            boolean isSelected = showPassword.isSelected();
            password.setVisible(!isSelected);
            passwordUnmasked.setVisible(isSelected);
        });
    }

    private void hidePasswordEditorControls() {
        usernameLabel.setVisible(false);
        username.setVisible(false);
        passwordLabel.setVisible(false);
        password.setVisible(false);
        showPassword.setVisible(false);
        passwordStrengthProgressBar.setVisible(false);
        passwordStrengthLabel.setVisible(false);
        save.setVisible(false);
    }

    private Button createButton(String className, String usernameString, String passwordString) {
        Button button = new Button(className);
        button.setOnAction(event -> {
            categoryName.setText(className);
            username.setText(usernameString);
            password.setText(passwordString);

            usernameLabel.setVisible(true);
            username.setVisible(true);
            passwordLabel.setVisible(true);
            password.setVisible(true);
            showPassword.setVisible(true);
            passwordStrengthProgressBar.setVisible(true);
            passwordStrengthLabel.setVisible(true);
            save.setVisible(true);
        });

        return button;
    }

    private void updatePasswordStrength(double entropy) {
        passwordStrengthProgressBar.setProgress(entropy / 100 * 0.78125);

        if (entropy < ENTROPY_THRESHOLD_1) {
            applyPasswordStrengthStyle("-fx-accent: darkred", "-fx-text-fill: black;");
        } else if (entropy < ENTROPY_THRESHOLD_2) {
            applyPasswordStrengthStyle("-fx-accent: red", "-fx-text-fill: black;");
        } else if (entropy < ENTROPY_THRESHOLD_3) {
            applyPasswordStrengthStyle("-fx-accent: yellow", "-fx-text-fill: black;");
        } else if (entropy < ENTROPY_THRESHOLD_4) {
            applyPasswordStrengthStyle("-fx-accent: green", "-fx-text-fill: white;");
        } else {
            applyPasswordStrengthStyle("-fx-accent: darkgreen", "-fx-text-fill: white;");
        }
    }

    private void applyPasswordStrengthStyle(String progressBarStyle, String labelStyle) {
        passwordStrengthProgressBar.setStyle(progressBarStyle);
        passwordStrengthLabel.setStyle(labelStyle);
    }
}