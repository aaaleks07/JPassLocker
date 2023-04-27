package com.aleksa.jpasslocker;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GlobalVariables {
    public static String fileName;
    public static String password;
    public static Map<String, List<String>> allPasswordData;

    public static File file;
    public static void setAllVariables(Path path){
        if(!path.endsWith(".jpldb")){
            throw new IllegalArgumentException(path.toString());
        }
    }
}
