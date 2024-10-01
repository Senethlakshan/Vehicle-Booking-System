//package com.jxg.isn_backend.util;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//public class EncryptionUtil {
//
//    private static final String ALGORITHM = "AES";
//    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
//
//    // Generate a new AES key
//    public static SecretKey generateKey() throws Exception {
//        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
//        keyGen.init(128); // Key size
//        return keyGen.generateKey();
//    }
//
//    // Encrypt the data using the provided key
//    public static String encrypt(String data, SecretKey key) throws Exception {
//        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
//        return Base64.getEncoder().encodeToString(encryptedBytes);
//    }
//
//    // Decrypt the encrypted data using the provided key
//    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
//        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
//        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
//        return new String(decryptedBytes, StandardCharsets.UTF_8);
//    }
//}
