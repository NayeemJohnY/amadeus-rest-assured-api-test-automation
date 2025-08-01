package testUtils;

import org.apache.logging.log4j.Logger;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class LoggingMatcher<T> extends TypeSafeMatcher<T> {

    private final Matcher<? super T> matcher;
    private final Logger logger;
    private final String message;

    public LoggingMatcher(Logger logger, Matcher<? super T> matcher, String message) {
        this.logger = logger;
        this.matcher = matcher;
        this.message = message;
    }

    @Override
    protected boolean matchesSafely(T actual) {
        boolean result = matcher.matches(actual);
        if (result)
            logger.info("Assertion Passed: '{}': actual='{}', expected='{}'", this.message, actual, matcher.toString());
        else
            logger.error("Assertion Failed: '{}': actual='{}', expected='{}'", this.message, actual, matcher.toString());
        return result;
    }

    @Override
    public void describeTo(Description description) {
        matcher.describeTo(description);
    }

    public static <T> LoggingMatcher<T> log(Logger logger, Matcher<? super T> matcher, String message) {
        return new LoggingMatcher<T>(logger, matcher, message);
    }
}
