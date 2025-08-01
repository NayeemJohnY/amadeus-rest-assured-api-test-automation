package testUtils;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestResultLoggerListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestResultLoggerListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("<=========== Test started: {} ===========>", result.getMethod().getMethodName());
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            logger.info("Test Data: {}", Arrays.toString(params));
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("<=========== Test passed: {} ===========>\n", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("<=========== Test failed: {} ===========>", result.getMethod().getMethodName(),
                result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("<=========== Test skipped: {} ===========>\n", result.getMethod().getMethodName());
    }

}
