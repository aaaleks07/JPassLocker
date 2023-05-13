package com.aleksa.jpasslocker;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenFileData {
    public static void main(String[] args) throws Exception {
        System.out.println(allDecryptedData(Path.of("test.jpldb"), "passwort"));
    }

    public static List<String> allDecryptedData(Path path, String password) throws Exception {
        BufferedReader file = Files.newBufferedReader(
                Paths.get(path.toUri())
        );


        String buffer = EncryptDecrypt.decrypt(file.readLine(), password);
        String[] bufferArray = buffer.split(System.lineSeparator());

        return new ArrayList<>(Arrays.asList(bufferArray));
    }
}
