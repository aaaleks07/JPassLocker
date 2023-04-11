package com.aleksa.jpasslocker;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Nikolic Aleksa (aleksa.nikolic@htl.rennweg.at)
 * Utility class for encryption and decryption using the AES algorithm.
 */
public class EncryptDecrypt {
    private static final String ALGORITHM = "AES";
    private static final int KEY_LENGTH = 16;

    /**
     * Encrypts a message using the AES algorithm.
     *
     * @param message  The message to be encrypted.
     * @param password The password used for encryption.
     * @return The encrypted message as a Base64 encoded string.
     * @throws Exception If an error occurs during encryption.
     */
    public static String encrypt(String message, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts a message using the AES algorithm.
     *
     * @param encryptedMessage The encrypted message as a Base64 encoded string.
     * @param password         The password used for decryption.
     * @return The decrypted message.
     * @throws Exception If an error occurs during decryption.
     */
    public static String decrypt(String encryptedMessage, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = initCipher(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage.getBytes(StandardCharsets.UTF_8)));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Generates a key for the AES algorithm based on the provided password.
     *
     * @param password The password used to generate the key.
     * @return A SecretKeySpec instance containing the generated key.
     */
    private static SecretKeySpec generateKey(String password) {
        byte[] key = new byte[KEY_LENGTH];
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(passwordBytes, 0, key, 0, Math.min(passwordBytes.length, key.length));
        return new SecretKeySpec(key, ALGORITHM);
    }

    /**
     * Initializes a Cipher instance with the specified mode and key.
     *
     * @param mode The mode of the cipher (e.g., Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE).
     * @param key  The SecretKeySpec instance containing the key.
     * @return A Cipher instance initialized with the specified mode and key.
     * @throws Exception If an error occurs during cipher initialization.
     */
    private static Cipher initCipher(int mode, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, key);
        return cipher;
    }
}
