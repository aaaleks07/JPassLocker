package com.aleksa.jpasslocker;

import javafx.application.Application;
import javafx.event.Event;
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

import java.util.List;
import java.util.Objects;

import static com.aleksa.jpasslocker.GlobalVariables.*;

// TODO Clear ScrollPane buttonScrollPane on new creation of password
public class PasswordEditor extends Application {
    private static final double ENTROPY_THRESHOLD_1 = 28;
    private static final double ENTROPY_THRESHOLD_2 = 36;
    private static final double ENTROPY_THRESHOLD_3 = 60;
    private static final double ENTROPY_THRESHOLD_4 = 128;

    private Label categoryName = new Label("Press any saved passwords to edit or see");
    private Label categoryNameInputLabel = new Label("Name");
    private TextField categoryNameInput = new TextField();
    private Label usernameLabel = new Label("Username");
    private Label passwordLabel = new Label("Password:");
    private TextField username = new TextField();
    private PasswordField password = new PasswordField();
    private TextField passwordUnmasked = new TextField();
    private CheckBox showPassword = new CheckBox("Show password");
    private Label passwordStrengthLabel = new Label("0");
    private ProgressBar passwordStrengthProgressBar = new ProgressBar(0.0001);
    private Button save = new Button("Save");
    private Button remove = new Button("Remove");
    VBox root = new VBox();
    ScrollPane buttonScrollPane;

    public void window() {
        root = new VBox();
        Scene scene = new Scene(root);
        mainStage.setMinWidth(650);
        mainStage.setMinHeight(500);

        //TODO ToDelete System.out.println()
        System.out.println("AllData:\n" + allData);

        for (int i = 1; i < allData.size(); i++) {
            allButtons.add(createButton(i));
        }

        buttonScrollPane = createButtonBox(allButtons, root);
        System.out.println("AllButtons:\n" + allButtons);
        Button addNew = new Button("+");
        addNew.setOnAction(actionEvent -> createNewPassword());
        addNew.setFont(new Font(20));
        addNew.minWidthProperty().bind(addNew.heightProperty());
        addNew.minHeightProperty().bind(buttonScrollPane.heightProperty());

        HBox topBar = new HBox(addNew, remove,buttonScrollPane);
        GridPane passwordEditorPane = createPasswordEditorPane(mainStage);

        root.getChildren().addAll(topBar, passwordEditorPane);
        mainStage.setScene(scene);
        mainStage.show();
    }

    private void createNewPassword(){
        allData.add("NewPassword;username;password");
        Button newButton = createButton(allData.size()-1);
        allButtons.add(newButton);
        HBox buttonBox = (HBox)buttonScrollPane.getContent();
        buttonBox.getChildren().add(newButton);
        //TODO Delete System.out.println()
        System.out.println("created");
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
        remove.prefWidthProperty().bind(password.widthProperty());
        username.prefWidthProperty().bind(stage.widthProperty().subtract(100));
        password.prefWidthProperty().bind(stage.widthProperty().subtract(100));
        passwordStrengthProgressBar.prefWidthProperty().bind(stage.widthProperty().subtract(100));

        StackPane passwordStrength = new StackPane(passwordStrengthProgressBar, passwordStrengthLabel);
        passwordEditorPane.add(categoryName, 0, 0);
        passwordEditorPane.add(categoryNameInputLabel,0,1);
        passwordEditorPane.add(categoryNameInput,0,2);
        passwordEditorPane.add(usernameLabel, 0, 3);
        passwordEditorPane.add(passwordLabel, 0, 5);
        passwordEditorPane.add(username, 0, 4);
        passwordEditorPane.add(password, 0, 6);
        passwordEditorPane.add(passwordUnmasked, 0, 6);
        passwordEditorPane.add(showPassword, 0, 7);
        passwordEditorPane.add(passwordStrength, 0, 8);
        passwordEditorPane.add(save, 0, 9);
        passwordEditorPane.add(remove, 0, 10);

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

    boolean isSelected = showPassword.isSelected();
    private void configureShowPasswordCheckbox() {
        showPassword.setOnAction(event -> {
            password.setVisible(!isSelected);
            passwordUnmasked.setVisible(isSelected);
        });
    }

    private void hidePasswordEditorControls() {
        categoryNameInputLabel.setVisible(false);
        categoryNameInput.setVisible(false);
        usernameLabel.setVisible(false);
        username.setVisible(false);
        passwordLabel.setVisible(false);
        password.setVisible(false);
        showPassword.setVisible(false);
        passwordUnmasked.setVisible(false);
        passwordStrengthProgressBar.setVisible(false);
        passwordStrengthLabel.setVisible(false);
        save.setVisible(false);
        remove.setVisible(false);
    }

    private Button createButton(int id) {
        Button button = new Button(allData.get(id).split(";")[0]);
        button.setOnAction(event -> {
            categoryName.setText(allData.get(id).split(";")[0]);
            username.setText(allData.get(id).split(";")[1]);
            password.setText(allData.get(id).split(";")[2]);

            categoryNameInputLabel.setVisible(true);
            categoryNameInput.setVisible(true);
            usernameLabel.setVisible(true);
            username.setVisible(true);
            passwordLabel.setVisible(true);
            password.setVisible(true);
            showPassword.setVisible(true);
            passwordStrengthProgressBar.setVisible(true);
            passwordStrengthLabel.setVisible(true);
            save.setVisible(true);
            remove.setVisible(true);

            save.setOnAction(actionEvent -> {

                if(categoryName.getText().equals("")){
                    categoryName.setText("");
                }
                if(Objects.equals(username.getText(), "")){
                    username.setText("");
                }
                if(Objects.equals(password.getText(), "") || Objects.equals(passwordUnmasked.getText(), "")){
                    password.setText("");
                    passwordUnmasked.setText("");
                }
                //TODO Delete System.out.println()
                System.out.println(username.getText());
                allData.set(id, categoryName.getText() + ";" + username.getText() + ";" + password.getText());
                Save.toFile();
            });

            remove.setOnAction(actionEvent -> {
                int i = id;
                if (i <= allButtons.size()) {
                    allData.remove(i);
                    allButtons.remove(i);
                    HBox buttonBox = (HBox) buttonScrollPane.getContent();
                    buttonBox.getChildren().remove(i);
                    hidePasswordEditorControls();
                    categoryName.setText("Press any saved passwords to edit or see");
                }
            });
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

    @Override
    public void start(Stage stage) throws Exception {
    }
}