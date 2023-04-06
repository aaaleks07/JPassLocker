package com.aleksa.jpasslocker;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Nikolic Aleksa (aleksa.nikolic@htl.rennweg.at)
 */
public class EncryptDecrypt {
    private static final String ALGORITHM = "AES";
    private static final int KEY_LENGTH = 16;

    /**
     * Encrypts a message using AES algorithm
     * @param message
     * @param password
     * @return
     * @throws Exception
     */
    public static String encrypt(String message, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts a message using AES algorithm
     * @param encryptedMessage
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedMessage, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Generates a key for AES algorithm
     * @param password
     * @return
     */
    private static SecretKeySpec generateKey(String password){
        byte[] key = new byte[KEY_LENGTH];
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(passwordBytes, 0, key, 0, Math.min(passwordBytes.length, key.length));
        return new SecretKeySpec(key, ALGORITHM);
    }
}
