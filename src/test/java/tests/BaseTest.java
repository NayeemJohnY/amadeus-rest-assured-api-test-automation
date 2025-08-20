package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import testUtils.AnnotationTransformer;
import testUtils.TestResultLoggerListener;
import utils.ReadProperties;
import utils.RestAssuredLoggerFilter;
import utils.TokenManager;

/**
 * Base test class for Amadeus API tests. Handles common setup like authentication, request logging,
 * and configuration. All test classes should extend this to inherit suite-level and per-method
 * setup.
 */
@Listeners({TestResultLoggerListener.class, io.qameta.allure.testng.AllureTestNg.class})
public class BaseTest {

  protected static final ReadProperties readProperties = new ReadProperties();

  protected Logger logger = LogManager.getLogger(getClass());

  /**
   * Sets up suite-wide RestAssured configuration before any tests run. Adds logging filters and
   * sets the base URI for all requests.
   */
  @BeforeSuite(alwaysRun = true)
  public void setUpSuite() {
    RestAssured.filters(new RestAssuredLoggerFilter(), new AllureRestAssured());
    RestAssured.baseURI = readProperties.getProperty("baseURI");
  }

  /**
   * Updates the OAuth2 token before each test method to ensure valid authentication. Sets the
   * request specification with the latest token for all requests in the test.
   */
  @BeforeMethod(alwaysRun = true)
  public void updateToken() {
    try {
      String token = TokenManager.getToken();
      RestAssured.requestSpecification =
          new RequestSpecBuilder().setAuth(RestAssured.oauth2(token)).build();
      logger.debug("Token refreshed if needed");
    } catch (Exception e) {
      logger.error("Failed to refresh token", e);
      throw new RuntimeException("Token refresh failed", e);
    }
  }
}
