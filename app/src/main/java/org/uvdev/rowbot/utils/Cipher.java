package org.uvdev.rowbot.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Cipher {

    public static final String PASSWORD = "L4vaL4mp";
    private static final int ITERATION_COUNT = 1000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = KEY_LENGTH / 8;

    private static final String GITHUB_PERSONAL_ACCESS_TOKEN =
            "4151b0703efc44f95b7eb1296e883c54ce63dbed";

    private static final String SALT = "a8h+YzEGTRNOCnBmnl1I8XYuhAlEMxFNSVqFIYNWhq0=";
    private static final String IV = "19FdvE6sJaok+EY092rdrA==";
    private static final String ACCESS_TOKEN = "9SUtpl0doSqsVBN+w4+K9pxwt27bfsIGt7UO53Z6yQz1K6HMHg0lAx0WJbx/l33u";

    public static void encrypt()
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException,
            BadPaddingException, IllegalBlockSizeException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        random.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, ivParams);
        byte[] ciphertext = cipher.doFinal(GITHUB_PERSONAL_ACCESS_TOKEN.getBytes("UTF-8"));

        Log.d("Cipher", "salt = \"" + Base64.encode(salt, Base64.DEFAULT) + "\"");
        Log.d("Cipher", "iv = \"" + Base64.encode(iv, Base64.DEFAULT) + "\"");
        Log.d("Cipher", "cipher = \"" + Base64.encode(ciphertext, Base64.DEFAULT) + "\"");
    }

    public static String getGithubAccessToken(String password) {
        if (TextUtils.isEmpty(password)) {
            return null;
        }
        String token = null;

        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(),
                    Base64.decode(SALT, Base64.DEFAULT), ITERATION_COUNT, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            SecretKey key = new SecretKeySpec(keyBytes, "AES");

            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParams = new IvParameterSpec(Base64.decode(IV, Base64.DEFAULT));
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, ivParams);
            token = new String(cipher.doFinal(Base64.decode(ACCESS_TOKEN, Base64.DEFAULT)),
                    "UTF-8");
        } catch (NoSuchAlgorithmException|NoSuchPaddingException|InvalidKeyException
                |InvalidAlgorithmParameterException|InvalidKeySpecException
                |IllegalBlockSizeException|BadPaddingException|UnsupportedEncodingException e) {
            Log.e("Cipher", "Failed to get github auth token", e);
            return null;
        }

        return token;
    }
}
