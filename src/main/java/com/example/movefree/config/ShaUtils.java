package com.example.movefree.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaUtils {

    private static final String ALGORITHM_TYPE = "SHA3-256";

    public static String decode(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(ALGORITHM_TYPE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] result = md.digest(password.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

}

