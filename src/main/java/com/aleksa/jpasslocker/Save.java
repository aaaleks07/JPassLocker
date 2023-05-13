package com.aleksa.jpasslocker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.aleksa.jpasslocker.GlobalVariables.*;

public class Save {
    public static void toFile() {
        StringBuilder allDataInString = new StringBuilder();

        BufferedWriter fileToWrite;
        try {
            fileToWrite = Files.newBufferedWriter(
                    Path.of(file.getPath())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        allDataInString.append(allData.get(0)).append(System.lineSeparator());

        for (int i = 1; i < allData.size(); i++) {
            allDataInString.append(allData.get(i).split(";")[0]).append(";").append(allData.get(i).split(";")[1]).append(";").append(allData.get(i).split(";")[2]).append(System.lineSeparator());
        }

        String encrypted;
        try {
            encrypted = EncryptDecrypt.encrypt(String.valueOf(allDataInString), password);
        } catch (Exception e) {
            throw new RuntimeException("Error in Encryption");
        }

        try {
            fileToWrite.write(encrypted);
            fileToWrite.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(encrypted);
    }
}
