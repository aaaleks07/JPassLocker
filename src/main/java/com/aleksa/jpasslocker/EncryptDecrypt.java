package com.aleksa.jpasslocker;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptDecrypt {
    private static final String ALGORITHM = "AES";
    private static final int KEY_LENGTH = 16;

    public static String encrypt(String message, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedMessage, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = initCipher(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage.getBytes(StandardCharsets.UTF_8)));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static SecretKeySpec generateKey(String password) {
        byte[] key = new byte[KEY_LENGTH];
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(passwordBytes, 0, key, 0, Math.min(passwordBytes.length, key.length));
        return new SecretKeySpec(key, ALGORITHM);
    }

    private static Cipher initCipher(int mode, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, key);
        return cipher;
    }
}
