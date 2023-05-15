package com.aleksa.jpasslocker;

import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GlobalVariables {

    public static Stage mainStage;
    public static String fileName;
    public static String password;
    public static Map<String, List<String>> allPasswordData;
    public static List<String> allData = new ArrayList<>();
    public static List<Button> allButtons = new ArrayList<>();

    public static File file = new File("test.jpldb");

    public static int triesLeft = 3;



    public static void setAllVariables(Path path){
        if(path.endsWith("jpldb")){
            throw new IllegalArgumentException(path.toString());
        }
    }
}
