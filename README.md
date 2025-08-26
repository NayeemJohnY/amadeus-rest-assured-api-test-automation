# Amadeus Rest Assured API Test Automation

[![Latest Test Execution](https://github.com/NayeemJohnY/amadeus-rest-assured-api-test-automation/actions/workflows/test-execution.yml/badge.svg)](https://github.com/NayeemJohnY/amadeus-rest-assured-api-test-automation/actions/workflows/test-execution.yml)

## Quick Links

:link: [Framework Q&A (Story & Design)](https://nayeemjohny.github.io/amadeus-rest-assured-api-test-automation/index.html)

:link: [Sample Allure Report](https://nayeemjohny.github.io/amadeus-rest-assured-api-test-automation/allure-report/index.html)

:link: [Java APIDocs](https://nayeemjohny.github.io/amadeus-rest-assured-api-test-automation/javadocs/apidocs/index.html)

:link: [Test APIDocs](https://nayeemjohny.github.io/amadeus-rest-assured-api-test-automation/javadocs/testapidocs/index.html)


## Overview
This project is an API Test Automation framework built using Java, Rest Assured, and TestNG. It is designed to automate the testing of Amadeus APIs, providing robust reporting, logging, and maintainable test structure. The framework supports parameterized tests, data-driven testing, and integrates with Allure for rich test reports.

## Tech Stack

| Label          | Tool                                    |
| -------------- | --------------------------------------- |
| Language       | Java 21+                                |
| API Testing    | Rest Assured                            |
| Test Framework | TestNG                                  |
| Reporting      | Allure                                  |
| Logging        | Log4j2                                  |
| JSON           | Jackson (serialization/deserialization) |
| Assertions     | Hamcrest                                |
| Build Tool     | Maven                                   |


## Key Functionalities & Custom Implementations

- **Modular Test Structure:**
  - Tests are organized by API domain (airports, flights, destination experiences, car and transfers) for clarity and maintainability.

- **Custom Assertion Utilities:**
  - The `Assertion` class extends TestNG assertions with Log4j2-powered logging, providing clear pass/fail messages in logs and reports.
  - The `LoggingMatcher` class integrates Hamcrest matchers with logging for expressive, traceable assertions.

- **Reusable Test Data Providers:**
  - Data-driven testing is enabled via TestNG `@DataProvider` methods, allowing parameterized tests for both valid and invalid scenarios.

- **Secure Secret Handling:**
  - The `EncryptionUtils` utility provides AES-based encryption/decryption for sensitive credentials, ensuring secrets are not stored in plain text.

- **Token Management & Authentication:**
  - The `TokenManager` utility handles OAuth2 token lifecycle with automatic caching, expiry detection, and thread-safe refresh mechanisms. It can be overridden for testing by setting the `TOKEN_EXPIRY_SECONDS` environment variable or system property.

- **Configurable Properties:**
  - The `ReadProperties` utility loads configuration from a properties file, supporting easy environment and credential management.

- **Comprehensive Logging:**
  - All HTTP requests and responses are logged using a custom Rest Assured filter (`RestAssuredLoggerFilter`) and Log4j2, aiding in debugging and traceability.

- **Allure Reporting Integration:**
  - All tests are annotated for Allure, and the framework is pre-configured to generate rich, interactive test reports.

- **Extensible Utilities:**
  - Utilities for JSON serialization/deserialization (`JsonUtils`) and record-based data models simplify test data handling and validation.

- **CI/CD Ready:**
  - Includes a sample GitHub Actions workflow for automated test execution and reporting.

- **Parallel Test Execution:**
  - Framework supports parallel test execution at both method and class levels through TestNG configuration, with thread-safe token management ensuring no conflicts during concurrent runs.

- **Test Groups & Categorization:**
  - Tests are organized into logical groups using TestNG's `@Test(groups = {...})` annotation for flexible test execution:
    - **smoke**: Critical test cases that validate core API functionality and essential business flows
    - **regression**: Comprehensive test coverage including both positive and negative scenarios for thorough validation

- **Test Results Mapping & Collection:**
  - Automated test results collector that maps test methods to test case IDs and generates structured JSON reports for external test management systems integration.
  - The `TestResultsReporter` implements TestNG's `IReporter` interface to capture comprehensive test execution data including outcomes, durations, and iteration details for parameterized tests.
  - Results are exported in JSON format to `test-results/test-results-report.json` with support for both single execution and multi-iteration test scenarios.


## About the Framework

This framework is designed for scalable, maintainable, and robust API test automation. It follows best practices for test organization, code reuse, and reporting, making it suitable for both small and large API projects. Key aspects include:

- **Separation of Concerns:**
  - Test logic, data models, utilities, and configuration are clearly separated, making the codebase easy to navigate and extend.

- **Extensibility:**
  - Easily add new API domains, utilities, or data providers without impacting existing tests.
  - Plug in additional reporting, logging, or assertion libraries as needed.

- **Maintainability:**
  - Consistent use of Java records and utility classes reduces boilerplate and duplication.
  - Centralized configuration and logging make updates and debugging straightforward.

- **Best Practices:**
  - Uses TestNG for flexible test execution and grouping.
  - Allure for actionable, visual test reports.
  - Log4j2 for granular, performant logging.

## Project Structure

```
amadeus-rest-assured/
├── pom.xml                                                 # Maven project configuration
├── README.md                                               # Project documentation
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── records/                                    # Java records for API data models
│   │   │   │   ├── Location.java                           # Model for location data
│   │   │   │   ├── Address.java                            # Model for address data
│   │   │   │   └── AirportQueriesWrapper.java              # Wrapper for airport query test data
│   │   │   │
│   │   │   └── utils/                                      # Utility classes
│   │   │       ├── EncryptionUtils.java                    # AES encryption/decryption utilities
│   │   │       ├── JsonUtils.java                          # JSON serialization/deserialization helpers
│   │   │       ├── ReadProperties.java                     # Loads config properties
│   │   │       └── RestAssuredLoggerFilter.java            # Logs HTTP requests/responses
│   │   │
│   │   └── resources/
│   │       ├── config.properties                           # API credentials and endpoints
│   │       └── log4j2.xml                                  # Log4j2 logging configuration
|   |
│   └── test/
│       ├── java/
│       │   ├── testUtils/                                  # Test utility classes
│       │   │   ├── Assertion.java                          # Custom assertions with logging
│       │   │   ├── LoggingMatcher.java                     # Hamcrest matcher with logging
│       │   │   ├── TestResultLoggerListener.java           # TestNG listener for logging test events
│       │   │   ├── TestResultsReporter.java                 # TestNG IReporter for JSON test results export
│       │   │   └── TestResultsRecords.java                  # Java records for test result data models
│       │   │ 
│       │   └── tests/                                      # Test classes by API domain
│       │       ├── airports/
│       │       │   └── AirportReferenceLocationTest.java   # Airport location API tests
│       │       │
│       │       ├── carAndTransfers/
│       │       │   └── TransferOffersTest.java             # Car/transfer offer API tests
│       │       │
│       │       ├── destinationExperiences/
│       │       │   └── ToursAndActivitiesTest.java          # Destination experience API tests
│       │       │
│       │       ├── flights/
│       │       │   ├── FlightOffersTest.java               # Flight offer API tests
│       │       │   └── FlightInspirationTest.java          # Flight inspiration API tests
│       │       │
│       │       └── hotels/
│       │           └── SearchHotelsTest.java               # Hotel search API tests
│       │
│       └── resources/
│           └── airport_locations_test_data.json            # Test data for airport locations
│  
│  
├── testng.xml                                              # TestNG suite configuration
│  
├── target/                                                 # Maven build output
└── test-results/                                           # Test logs and results
```

## TokenManager Mechanism

The `TokenManager` class provides a robust OAuth2 authentication mechanism with the following features:

- **Automatic Token Caching:** Stores access tokens in memory to avoid unnecessary API calls
- **Expiry Management:** Refreshes tokens 30 seconds before expiration to prevent authentication failures
- **Thread Safety:** Uses synchronized blocks to ensure safe token generation during parallel test execution
- **Secure Credential Handling:** Decrypts encrypted client credentials using AES encryption
- **Isolation:** Token generation requests are isolated from global RestAssured specifications to prevent conflicts

### Configuration Override
You can override the encryption key for testing purposes by setting:
- Environment variable: `AMADEUS_TEST_SECRET`
- System property: `-DAMADEUS_TEST_SECRET=your_key`

## Getting Started
1. **Clone the repository:**
   ```sh
   git clone <repo-url>
   cd amadeus-rest-assured
   ```
2. **Configure properties:**
   - Update `src/main/resources/config.properties` with your Amadeus API credentials and endpoints.
3. **Build the project:**
   ```sh
   mvn clean install
   ```
4. **Run tests:**
   ```sh
   # Run all tests
   mvn test
   
   # Run only smoke tests (critical functionality)
   mvn test -Dgroups=smoke
   
   # Run only regression tests (comprehensive coverage)
   mvn test -Dgroups=regression
   
   # Run with parallel execution
   mvn test -DthreadCount=4 -Dparallel=methods
   ```
5. **View Allure Report:**
   ```sh
   allure serve ./test-results/allure-results
   ```

## Example Test
See [`src/test/java/tests/flights/FlightOffersTest.java`](src/test/java/tests/flights/FlightOffersTest.java) for a sample test that validates flight offer search functionality.

---

<footer align="center">
  <a href="https://www.linkedin.com/in/nayeemjohny/" target="_blank">Connect with me on LinkedIn</a>
</footer>
