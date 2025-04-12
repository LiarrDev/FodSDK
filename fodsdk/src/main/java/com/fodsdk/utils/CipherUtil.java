package com.fodsdk.utils;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CipherUtil {

    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs/z/qvUIoPtSdUtyRCs4"
            + "BRtQgmJ7sm9WDkocFIVfxjGV8BbRehI3Jb3Z5OjrJEa4U3QbjpIwp+VQHf5xVRDe"
            + "c92s8E3ByR+QKDRY+BGPB2rxMtoXKl7Q8kd+jcwG0BPuFTUix65MNfLAYSm8Gn30"
            + "1eVQBhz3gAqmt4jBoxWtGPRshEbTiPWFBoX2twoBlpsWYDrqiK4P+XnbWAKuQxJc"
            + "9gj718aEqMhyfqS9ojYNbElQKaieI/V5qcazDYCBQfKthcXeT+gJ4LFmejNP0Z5v"
            + "peOmw3xBTVVA8Jp+AJPFSQ23HtaWwV0qaExhdyiwmxgv6ssg+8KErlz8xzZqBMhJ"
            + "XwIDAQAB";

    public static String rsa(String data) {
        try {
            byte[] decodedKey = Base64.decode(PUBLIC_KEY, Base64.NO_WRAP);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encryptedData, Base64.NO_WRAP);
        } catch (
                NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = messageDigest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
