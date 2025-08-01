package testUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class Assertion {

    private static final Logger logger = LogManager.getLogger(Assertion.class);

    public static void assertTrue(boolean condition, String message) {
        assertWithLog(() -> Assert.assertTrue(condition), message);
    }

    public static <T> void assertEquals(T actual, T expected, String message) {
        assertWithLog(() -> Assert.assertEquals(actual, expected), message, actual, expected);
    }

    private static void assertWithLog(Runnable assertion, String message) {
        try {
            assertion.run();
            logger.info("Assertion Passed: '{}'", message);
        } catch (AssertionError e) {
            logger.error("Assertion Failed: '{}'", message);
            throw e;
        }
    }

    private static <T> void assertWithLog(Runnable assertion, String message, T actual, T expected) {
        try {
            assertion.run();
            logger.info("Assertion Passed: '{}', actual='{}', expected='{}'", message, actual, expected);
        } catch (AssertionError e) {
            logger.error("Assertion Failed: '{}', actual='{}', expected='{}'", message, actual, expected);
            throw e;
        }
    }
}
