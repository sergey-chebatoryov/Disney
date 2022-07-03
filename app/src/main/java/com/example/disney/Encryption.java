package com.example.disney;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

public class Encryption {
    public static String encrypt(String clearString) {
        try {
            byte[] message = clearString.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return Base64.getEncoder().encodeToString(md.digest(message));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verify(String clearString, String encrypted) {
        return Objects.equals(encrypt(clearString), encrypted);
    }
}
