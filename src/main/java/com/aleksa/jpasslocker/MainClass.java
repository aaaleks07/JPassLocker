package com.aleksa.jpasslocker;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainClass extends Application {

    @Override
    public void start(Stage stage) throws Exception {
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

    public static void main(String[] args) {
        launch();
    }
}
