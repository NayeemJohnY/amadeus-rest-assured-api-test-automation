package utils;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class EncryptionUtils {

    private static Logger logger = LogManager.getLogger(EncryptionUtils.class);

    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
    }

    public static SecretKey base64ToKey(String base64Key) {
        byte[] decoded = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decoded, 0, decoded.length, "AES");
    }

    public static String encrypt(String plainText, String base64Key) {
        SecretKey secretKey = base64ToKey(base64Key);
        Cipher cipher;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encrypted = cipher.doFinal(plainText.getBytes());
        } catch (Exception e) {
            logger.error("Error occured while encrypting secret", e);
        }

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedText, String base64Key) {
        SecretKey secretKey = base64ToKey(base64Key);
        Cipher cipher;
        byte[] decrypted = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            decrypted = cipher.doFinal(decoded);
        } catch (Exception e) {
            logger.error("Error occured while decrypting secret", e);
        }

        return new String(decrypted);
    }

    public static void main(String[] args) throws Exception {
        String key = generateKey();
        logger.info("Generated Encryption key  {}", key);

        String plainText = "ABCDEFGHIJKLM";

        String encryptedText = encrypt(plainText, key);
        logger.info("Encrypted text {}", encryptedText);

        String decryptedText = decrypt(encryptedText, key);
        logger.info("Decrypted text {}", decryptedText);

        Assert.assertEquals(decryptedText, plainText);
    }
}
