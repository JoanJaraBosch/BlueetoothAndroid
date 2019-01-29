package com.example.joan.practica2ame_joajara_i_aleixiglesias;

import android.util.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionPass {

    public String encrypt (String value){
       /* Key key = generatedKey();
        Cipher cipher = Cipher.getInstance(EncryptionPass.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;*/
        String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(value.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return value;
    }

   /* private  Key generatedKey() throws Exception{
        Key key = new SecretKeySpec(EncryptionPass.KEY.getBytes(),EncryptionPass.ALGORITHM);
        return key;
    }*/

    public static String transformArraytoString(String[] aux) {
        String ret = Arrays.toString(aux);
        return ret.substring(1, ret.length()-1);
    }
}
