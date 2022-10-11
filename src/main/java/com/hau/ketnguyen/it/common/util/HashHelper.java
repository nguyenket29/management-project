package com.hau.ketnguyen.it.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * The Class HashHelper.
 */

/**
 * The Constant log.
 */
@Slf4j
public final class HashHelper {
    private final static int GCM_IV_LENGTH = 12;
    private final static int GCM_TAG_LENGTH = 16;

    public static String aesEncrypt(String privateString, String secretKey) {
        try {
            String secretKeyDecode = new String(Base64.getDecoder().decode(secretKey), StandardCharsets.UTF_8);
            SecretKey skey = new SecretKeySpec(secretKeyDecode.getBytes(StandardCharsets.UTF_8), "AES");
            byte[] iv = new byte[GCM_IV_LENGTH];
            (new SecureRandom()).nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
            cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);

            byte[] ciphertext = cipher.doFinal(privateString.getBytes(StandardCharsets.UTF_8));
            byte[] encrypted = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static String aesDecrypt(String encrypted, String secretKey) {
        try {
            String secretKeyDecode = new String(Base64.getDecoder().decode(secretKey), StandardCharsets.UTF_8);
            SecretKey skey = new SecretKeySpec(secretKeyDecode.getBytes(StandardCharsets.UTF_8), "AES");

            byte[] decoded = Base64.getDecoder().decode(encrypted);

            byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
            cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);

            byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

            return new String(ciphertext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String blowFishEncrypt(String privateString, String secretKey) {
        String REncrypt = "";
        try {
            String secretKeyDecode = new String(Base64.getDecoder().decode(secretKey), StandardCharsets.UTF_8);
            SecretKey skey = new SecretKeySpec(secretKeyDecode.getBytes(StandardCharsets.UTF_8), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);

            byte[] encrypt = privateString.getBytes(StandardCharsets.UTF_8);
            int limit = 8 - (encrypt.length % 8);
            byte[] buff = new byte[encrypt.length + limit];

            System.arraycopy(encrypt, 0, buff, 0, encrypt.length);

            for (int i = encrypt.length; i < encrypt.length + limit; i++) {
                buff[i] = 0x0;
            }

            byte[] ciphertext = cipher.doFinal(buff);
            REncrypt = Base64.getEncoder().encodeToString(ciphertext);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return REncrypt;
    }

    public static String blowFishDecrypt(String encrypted, String secretKey){
        try {
            String secretKeyDecode = new String(Base64.getDecoder().decode(secretKey), StandardCharsets.UTF_8);
            SecretKey skey = new SecretKeySpec(secretKeyDecode.getBytes(StandardCharsets.UTF_8), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skey);

            byte[] decoded = Base64.getDecoder().decode(encrypted);
            byte[] ciphertext = cipher.doFinal(decoded);

            int length = 0;
            for (int i = 0; i < ciphertext.length; i++) {
                if (ciphertext[length] != 0x0) {
                    length++;
                }
            }

            byte[] finals = new byte[length];
            for (int j = 0; j < finals.length; j++) {
                if(ciphertext[j] != 0x0) {
                    finals[j] = ciphertext[j];
                }
            }

            return new String(finals, StandardCharsets.UTF_8);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    private static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        } else {
            StringBuilder str = new StringBuilder();
            for (byte b : data) {
                if ((b & 0xFF) < 16)
                    str.append("0").append(Integer.toHexString(b & 0xFF));
                else
                    str.append(Integer.toHexString(b & 0xFF));
            }
            return str.toString().toUpperCase();
        }
    }

    private static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }
}
