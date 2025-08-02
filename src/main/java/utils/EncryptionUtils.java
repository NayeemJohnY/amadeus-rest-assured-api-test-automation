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

/**
 * Utility class for encryption and decryption using AES algorithm. Provides methods for key
 * generation, encryption, and decryption of text.
 */
public class EncryptionUtils {

  private static final Logger logger = LogManager.getLogger(EncryptionUtils.class);

  /**
   * Generates a new AES key and returns it as a Base64 encoded string.
   *
   * @return Base64 encoded string representation of the generated key
   * @throws NoSuchAlgorithmException if the AES algorithm is not available
   */
  public static String generateKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(256);
    return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
  }

  /**
   * Converts a Base64 encoded string key to a SecretKey object.
   *
   * @param base64Key the Base64 encoded key string
   * @return a SecretKey object for use with AES encryption/decryption
   */
  public static SecretKey base64ToKey(String base64Key) {
    byte[] decoded = Base64.getDecoder().decode(base64Key);
    return new SecretKeySpec(decoded, 0, decoded.length, "AES");
  }

  /**
   * Encrypts a plain text string using AES encryption.
   *
   * @param plainText the text to encrypt
   * @param base64Key the Base64 encoded encryption key
   * @return Base64 encoded encrypted string, or null if encryption fails
   */
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

  /**
   * Decrypts an encrypted string using AES decryption.
   *
   * @param encryptedText the Base64 encoded encrypted text
   * @param base64Key the Base64 encoded decryption key
   * @return the decrypted text, or null if decryption fails
   */
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

  /**
   * Main method for demonstrating encryption/decryption functionality. Generates a key, encrypts a
   * sample text, and decrypts it back.
   *
   * @param args command line arguments (not used)
   * @throws Exception if any cryptographic operations fail
   */
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
