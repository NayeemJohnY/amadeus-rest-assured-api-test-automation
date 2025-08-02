package testUtils;

import org.apache.logging.log4j.Logger;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * A Hamcrest matcher that logs assertion results using Log4j2.
 *
 * @param <T> the type of object being matched
 */
public class LoggingMatcher<T> extends TypeSafeMatcher<T> {

  private final Matcher<? super T> matcher;
  private final Logger logger;
  private final String message;

  /**
   * Constructs a LoggingMatcher.
   *
   * @param logger the logger to use
   * @param matcher the matcher to delegate to
   * @param message the assertion message
   */
  public LoggingMatcher(Logger logger, Matcher<? super T> matcher, String message) {
    this.logger = logger;
    this.matcher = matcher;
    this.message = message;
  }

  /**
   * Evaluates the matcher and logs the result.
   *
   * @param actual the actual value
   * @return true if the match is successful, false otherwise
   */
  @Override
  protected boolean matchesSafely(T actual) {
    boolean result = matcher.matches(actual);
    if (result) {
      logger.info(
          "Assertion Passed: '{}': actual='{}', expected='{}'",
          this.message,
          actual,
          matcher.toString());
    } else {
      logger.error(
          "Assertion Failed: '{}': actual='{}', expected='{}'",
          this.message,
          actual,
          matcher.toString());
    }
    return result;
  }

  /**
   * Describes the matcher to the given description.
   *
   * @param description the description to append to
   */
  @Override
  public void describeTo(Description description) {
    matcher.describeTo(description);
  }

  /**
   * Factory method to create a LoggingMatcher.
   *
   * @param logger the logger to use
   * @param matcher the matcher to delegate to
   * @param message the assertion message
   * @param <T> the type of object being matched
   * @return a new LoggingMatcher instance
   */
  public static <T> LoggingMatcher<T> log(
      Logger logger, Matcher<? super T> matcher, String message) {
    return new LoggingMatcher<T>(logger, matcher, message);
  }
}
