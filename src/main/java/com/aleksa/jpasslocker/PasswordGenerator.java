package com.aleksa.jpasslocker;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[{]};:',<.>/?";

    public static String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        List<String> characterSets = new ArrayList<>();
        characterSets.add(UPPERCASE_LETTERS);
        characterSets.add(LOWERCASE_LETTERS);
        characterSets.add(NUMBERS);
        characterSets.add(SPECIAL_CHARACTERS);

        int charactersPerSet = length / characterSets.size();
        int extraCharacters = length % characterSets.size();

        for (String characterSet : characterSets) {
            for (int i = 0; i < charactersPerSet; i++) {
                int randomIndex = random.nextInt(characterSet.length());
                password.append(characterSet.charAt(randomIndex));
            }
        }

        for (int i = 0; i < extraCharacters; i++) {
            String randomCharacterSet = characterSets.get(random.nextInt(characterSets.size()));
            int randomIndex = random.nextInt(randomCharacterSet.length());
            password.append(randomCharacterSet.charAt(randomIndex));
        }

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(length);
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(randomIndex));
            password.setCharAt(randomIndex, temp);
        }

        return password.toString();
    }
}
