package testUtils;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** TestNG listener that logs test execution events using Log4j2. */
public class TestResultLoggerListener implements ITestListener {

  private static final Logger logger = LogManager.getLogger(TestResultLoggerListener.class);

  /**
   * Called when a test method starts. Logs the method name and parameters.
   *
   * @param result the test result
   */
  @Override
  public void onTestStart(ITestResult result) {
    logger.info("<=========== Test started: {} ===========>", result.getMethod().getMethodName());
    Object[] params = result.getParameters();
    if (params != null && params.length > 0) {
      logger.info("Test Data: {}", Arrays.toString(params));
    }
  }

  /**
   * Called when a test method passes. Logs the method name.
   *
   * @param result the test result
   */
  @Override
  public void onTestSuccess(ITestResult result) {
    logger.info("<=========== Test passed: {} ===========>\n", result.getMethod().getMethodName());
  }

  /**
   * Called when a test method fails. Logs the method name and throwable.
   *
   * @param result the test result
   */
  @Override
  public void onTestFailure(ITestResult result) {
    logger.error(
        "<=========== Test failed: {} ===========>",
        result.getMethod().getMethodName(),
        result.getThrowable());
  }

  /**
   * Called when a test method is skipped. Logs the method name.
   *
   * @param result the test result
   */
  @Override
  public void onTestSkipped(ITestResult result) {
    logger.warn("<=========== Test skipped: {} ===========>\n", result.getMethod().getMethodName());
  }
}
