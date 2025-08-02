package testUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

/**
 * TestNG annotation transformer that automatically assigns a retry analyzer to test methods.
 * This enables automatic retry of failed tests based on custom logic in {@link RetryAnalyzer}.
 */
public class AnnotationTransformer implements IAnnotationTransformer {

  /**
   * Transforms the test annotation to set a retry analyzer if not already present.
   *
   * @param annotation the test annotation
   * @param testClass the test class
   * @param testConstructor the test constructor
   * @param testMethod the test method
   */
  @Override
  public void transform(
      ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

    Class<? extends IRetryAnalyzer> retyAnalyzer = annotation.getRetryAnalyzerClass();
    if (retyAnalyzer == null) {
      annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
  }
}
