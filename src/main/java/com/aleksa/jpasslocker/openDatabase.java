package com.aleksa.jpasslocker;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static com.aleksa.jpasslocker.GlobalVariables.*;
import static java.lang.Thread.sleep;

public class openDatabase {
    /**
     * Scene for opening a database
     * @return
     */
    public static Scene openDatabase(){
        GridPane pane = new GridPane();
        Scene scene = new Scene(pane);
        pane.setAlignment(Pos.CENTER);
        pane.setVgap(10);

        Label title = new Label("Enter password");
        title.setFont(new Font(25));
        Label subtitle = new Label("for " + fileName);
        subtitle.setFont(new Font(15));
        VBox box = new VBox(title,subtitle);
        box.setAlignment(Pos.CENTER);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setFocusTraversable(false);
        Button submit = new Button("Encrypt");

        submit.prefWidthProperty().bind(password.widthProperty());

        pane.add(box,0,0);
        pane.add(password,0,1);
        pane.add(submit,0,2);

        submit.setOnAction(actionEvent -> {
            try {
                allData = OpenFileData.allDecryptedData(file.toPath(), password.getText());
                System.out.println(allData);
                GlobalVariables.password = password.getText();
                PasswordEditor pe = new PasswordEditor();
                pe.window();
            } catch (Exception e) {
                triesLeft--;
                if(triesLeft == 0){
                    System.exit(2);
                }
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Wrong password\n" + triesLeft + " tries left");
                alert.setContentText("You entered a wrong password");
                mainStage.close();
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                alert.showAndWait();
                mainStage.setScene(openDatabase.openDatabase());
                mainStage.show();
            }
        });

        return scene;
    }


}
