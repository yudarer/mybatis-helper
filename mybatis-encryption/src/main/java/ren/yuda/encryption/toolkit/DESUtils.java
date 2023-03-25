package ren.yuda.encryption.toolkit;

import ren.yuda.encryption.EncryptException;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class DESUtils {

    public static String encrypt(byte[] dataSource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            return Base64.getEncoder().encodeToString(cipher.doFinal(dataSource));
        } catch (InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeySpecException |
                 NoSuchPaddingException | BadPaddingException e) {
            throw new EncryptException("encrypt error,please check \n", e);
        }

    }

    public static String decrypt(String src, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            return new String(cipher.doFinal(Base64.getDecoder().decode(src)));
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new EncryptException("decrypt error,please check \n", e);
        }
    }

}
