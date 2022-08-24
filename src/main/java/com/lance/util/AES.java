package com.lance.util;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;


/**
 * Encryption and decryption of string using AES/ECB/PKCS5Padding
 */
public final class AES {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    /**
     * Set key for use in encryption or decryption
     *
     * @param key: The key to be used for encrypting
     */
    public static void setKey(String key) {
        MessageDigest sha = null;
        try {
            AES.key = key.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            AES.key = sha.digest(AES.key);
            AES.key = Arrays.copyOf(AES.key, 16);
            secretKey = new SecretKeySpec(AES.key, "AES");
        } catch (NoSuchAlgorithmException e) {
        }
    }


    /**
     * Encrypt a string.
     *
     * @param str:    The value to be encrypted
     * @param secret: The key to be use in the encryption
     * @return String: Encrypted base64 encoded string if successful or return null if failed
     */
    public static String encrypt(String str, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypt a protected string.
     *
     * @param str:    The value to be decrypted
     * @param secret: The key to be use in the decryption
     * @return String: The real string if successful or return null if failed
     */
    public static String decrypt(String str, String secret) {
        String returnString = null;
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            returnString = new String(cipher.doFinal(Base64.getDecoder().decode(str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }

    public static void main(String[] args) {
        System.out.println(AES.encrypt(args[0], "lanceli"));
    }
}
