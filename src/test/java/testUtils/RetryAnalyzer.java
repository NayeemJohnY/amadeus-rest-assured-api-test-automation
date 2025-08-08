package testUtils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG retry analyzer that retries a test if the HTTP status code is 429 (Too Many Requests).
 * Retries up to a maximum number of times to handle rate limiting scenarios in API testing.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
  private int retryCount = 0;
  private final int maxRetryCount = 2;
  private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);

  /**
   * Determines whether the test should be retried based on the status code. Retries if the status
   * code is 429 and the maximum retry count has not been reached.
   *
   * @param result the test result
   * @return true if the test should be retried, false otherwise
   */
  @Override
  public boolean retry(ITestResult result) {
    Object statusCode = result.getAttribute("statusCode");
    if (statusCode instanceof Integer && (int) statusCode == 429 && retryCount < maxRetryCount) {
      retryCount++;
      String message =
          "Retrying test after 5 seconds due to 429 Too Many Requests (attempt " + retryCount + ")";
      logger.warn(message);
      Allure.step(message);
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }
}
