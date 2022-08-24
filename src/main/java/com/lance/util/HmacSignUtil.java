package com.lance.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class HmacSignUtil {

    /**
     * Get combination of message to be encrypted
     *
     * @param secretKey:  LINE Pay secret key
     * @param uri:        Requested URI
     * @param requestStr: The string of request content
     * @param nonce:      random string
     * @return String: The combination of message to be encrypted
     */
    public static String getAuthTex(String secretKey, String uri, String requestStr, String nonce) {
        return secretKey + uri + requestStr + nonce;
    }

    /**
     * Decrypt a string as LINE Pay required format
     *
     * @param secret:  The secret key to be encrypted message
     * @param message: The string to be encrypted
     * @return String: The string of LINE Pay required format
     */
    public static String encrypt(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(message.getBytes()));
    }
}
