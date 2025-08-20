package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for managing OAuth2 access tokens for Amadeus API authentication. Handles secure
 * credential decryption, token caching, expiry, and thread safety.
 */
public class TokenManager {

  private static final Logger logger = LogManager.getLogger(TokenManager.class);

  private static final ReadProperties readProperties = new ReadProperties();
  private static final String SECRET_KEY =
      System.getProperty("AMADEUS_TEST_SECRET", System.getenv("AMADEUS_TEST_SECRET"));
  private static final String clientId =
      EncryptionUtils.decrypt(readProperties.getProperty("clientId"), SECRET_KEY);
  private static final String clientSecret =
      EncryptionUtils.decrypt(readProperties.getProperty("clientSecret"), SECRET_KEY);
  private static final String grantType = readProperties.getProperty("grantType");
  private static final String tokenURI = readProperties.getProperty("tokenURI");
  private static final String TOKEN_EXPIRY_SECONDS =
      System.getProperty("TOKEN_EXPIRY_SECONDS", System.getenv("TOKEN_EXPIRY_SECONDS"));

  private static String accessToken;
  private static LocalDateTime expiryIn;

  /**
   * Returns a valid access token, refreshing it if expired or near expiry.
   *
   * @return OAuth2 access token string
   */
  public static String getToken() {
    // Refresh token 30 seconds before expiry to avoid edge cases
    if (accessToken == null || LocalDateTime.now().plusSeconds(30).isAfter(expiryIn)) {
      accessToken = generateToken();
    }
    return accessToken;
  }

  /**
   * Generates a new access token using client credentials. Ensures no test-level RestAssured
   * specification interferes with the request.
   *
   * @return OAuth2 access token string
   */
  private static String generateToken() {
    if (clientId == null || clientSecret == null) {
      throw new IllegalStateException("Client credentials not properly configured");
    }

    synchronized (TokenManager.class) {
      RequestSpecification originalSpec = RestAssured.requestSpecification;
      RestAssured.requestSpecification = null; // Clear global spec
      try {
        logger.info("Generating new access token...");
        Response response =
            RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .formParam("grant_type", grantType)
                .when()
                .post(tokenURI);

        if (response.getStatusCode() != 200) {
          logger.error(
              "Token generation failed. Status: {}, Response: {}",
              response.getStatusCode(),
              response.getBody().asString());
          throw new RuntimeException("Failed to generate token: " + response.getStatusCode());
        }

        int expiryInSeconds = response.jsonPath().getInt("expires_in");
        // If TOKEN_EXPIRY_SECONDS is provided as System or Env property, override the expires_in
        System.out.println("TOKEN_EXPIRY_SECONDS: " + TOKEN_EXPIRY_SECONDS);
        if (!TOKEN_EXPIRY_SECONDS.isEmpty()) {
          expiryInSeconds = Integer.parseInt(TOKEN_EXPIRY_SECONDS);
        }

        expiryIn = LocalDateTime.now().plusSeconds(expiryInSeconds);
        logger.info("Token generated successfully, expires at: {}", expiryIn);
        return response.jsonPath().getString("access_token");
      } catch (Exception e) {
        logger.error("Error generating token", e);
        throw new RuntimeException("Token generation failed", e);
      } finally {
        RestAssured.requestSpecification = originalSpec; // Restore
      }
    }
  }
}
