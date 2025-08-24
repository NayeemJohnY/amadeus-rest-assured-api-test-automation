package testUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import testUtils.TestResultsRecords.*;
import utils.JsonUtils;

public class TestResultsReporter implements IReporter {
  private static final String TEST_PLAN_SUITE_FILE_NAME = "test-plan-suite.json";
  private static final String TEST_CASE_RESULTS_FILE_PATH = "test-results/test-results-report.json";
  private static Map<String, TestCaseInfo> testCasesMap = new HashMap<>();
  private static final Logger logger = LogManager.getLogger(TestResultsReporter.class);
  private static Map<String, TestResult> testResultsMap = new HashMap<>();

  @Override
  public void generateReport(
      List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

    TestPlanSuite testPlanSuite =
        JsonUtils.fromJson(TEST_PLAN_SUITE_FILE_NAME, TestPlanSuite.class, true);
    if (testPlanSuite != null) {
      testCasesMap = testPlanSuite.testCases();
    }
    for (ISuite suite : suites) {
      Map<String, ISuiteResult> suiteResults = suite.getResults();
      for (ISuiteResult suiteResult : suiteResults.values()) {
        ITestContext testContext = suiteResult.getTestContext();
        collectTestResults(testContext.getPassedTests());
        collectTestResults(testContext.getFailedTests());
        collectTestResults(testContext.getSkippedTests());
      }
    }

    TestResultReport testResultReport = null;
    if (testPlanSuite != null) {
      testResultReport =
          new TestResultReport(
              testPlanSuite.testPlanName(), testPlanSuite.testSuiteName(), testResultsMap);
    } else {
      testResultReport =
          new TestResultReport("Unknown Test Plan", "Unknown Test Suite", testResultsMap);
    }
    ensureOutputDirectory();
    writeResultsReport(testResultReport);
  }

  private String getParametersAsString(Object[] params) {
    String stringParams = "";
    if (params != null && params.length > 0) {
      params =
          Arrays.stream(params)
              .filter(param -> param != null && !(param instanceof ITestContext))
              .toArray();

      if (params.length > 0) {
        stringParams = Arrays.toString(params);
      }
    }
    return stringParams;
  }

  /**
   * Converts TestNG status code to readable string.
   *
   * @param status the TestNG result status code
   * @return human-readable outcome string
   */
  private String getOutcomeString(int status) {
    return switch (status) {
      case ITestResult.SUCCESS -> "Passed";
      case ITestResult.FAILURE -> "Failed";
      case ITestResult.SKIP -> "Skipped";
      default -> "Unknown";
    };
  }

  private int getNextIterationId(String testCaseId) {
    TestResult existingResult = testResultsMap.get(testCaseId);
    return existingResult != null ? existingResult.iterationDetails().size() + 1 : 1;
  }

  public void collectTestResults(IResultMap resultMap) {
    for (ITestResult testResult : resultMap.getAllResults()) {
      String outcome = getOutcomeString(testResult.getStatus());
      Long durationInMs = testResult.getEndMillis() - testResult.getStartMillis();
      String testName = testResult.getMethod().getMethodName();
      TestCaseInfo testCaseInfo = testCasesMap.get(testName);
      String testCaseId = testCaseInfo != null ? testCaseInfo.testCaseId() : "Unknown";
      String testParams = getParametersAsString(testResult.getParameters());

      String comment = "Test Name: " + testName;

      if (!testParams.isEmpty()) {
        TestIterationResult iterationResult =
            new TestIterationResult(
                getNextIterationId(testCaseId),
                outcome,
                "Test Parameters: " + testParams,
                durationInMs);

        TestResult existingResult = testResultsMap.get(testCaseId);
        if (existingResult != null) {
          testResultsMap.put(testCaseId, existingResult.withIterationResult(iterationResult));
        } else {
          // First iteration
          TestResult newResult =
              new TestResult(outcome, comment, durationInMs, List.of(iterationResult));
          testResultsMap.put(testCaseId, newResult);
        }
      } else {
        TestResult newResult = new TestResult(outcome, comment, durationInMs, List.of());
        testResultsMap.put(testCaseId, newResult);
      }
    }
  }

  private void ensureOutputDirectory() {
    File outputFile = new File(TEST_CASE_RESULTS_FILE_PATH);
    File outputDir = outputFile.getParentFile();
    if (outputDir != null && !outputDir.exists()) {
      if (outputDir.mkdirs()) {
        logger.info("Created output directory: {}", outputDir.getAbsolutePath());
      } else {
        logger.error("Failed to create output directory: {}", outputDir.getAbsolutePath());
      }
    }
  }

  private void writeResultsReport(TestResultReport report) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_CASE_RESULTS_FILE_PATH))) {
      // Convert record to Map for JsonUtils compatibility
      Map<String, Object> reportMap =
          Map.of(
              "testPlanName", report.testPlanName(),
              "testSuiteName", report.testSuiteName(),
              "testResults", report.testResults());
      String json = JsonUtils.mapToJson(reportMap);
      writer.write(json);
      logger.info("Test results report generated successfully: {}", TEST_CASE_RESULTS_FILE_PATH);
    } catch (IOException e) {
      logger.error("Failed to generate Test Results Report: ", e);
    }
  }
}
