package com.aleksa.jpasslocker;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
        Label subtitle = new Label("for FILENAME");
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

        return scene;
    }
}
