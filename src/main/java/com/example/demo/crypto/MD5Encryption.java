package com.example.demo.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encryption {
    private static final String hashAlgorithm = "SHA-1";

    public static String encrypt(String password) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(hashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return hexEncode(sha.digest(password.getBytes()));
    }

    private static String hexEncode(byte[] aInput) {
        StringBuffer result = new StringBuffer();
        char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }

}
