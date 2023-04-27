package com.aleksa.jpasslocker;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.aleksa.jpasslocker.EncryptDecrypt;

public class OpenFileData {
    public static void main(String[] args) throws Exception {
        System.out.println(allDecryptedData(Path.of("test.jpldb"), "passwort"));
    }

    public static List<String> allDecryptedData(Path path, String password) throws Exception {
        BufferedReader file = Files.newBufferedReader(
                Paths.get(path.toUri())
        );

        List<String> data = new ArrayList<>();

        String buffer = "";

        while ((buffer = file.readLine()) != null){
            data.add(EncryptDecrypt.decrypt(buffer, password));
        }

        return data;
    }
}
