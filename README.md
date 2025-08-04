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
│       │   │   └── TestResultLoggerListener.java           # TestNG listener for logging test events
│       │   │ 
│       │   └── tests/                                      # Test classes by API domain
│       │       ├── airports/
│       │       │   └── AirportReferenceLocationTest.java   # Airport location API tests
│       │       │
│       │       ├── carAndTransfers/
│       │       │   └── TransferOffersTest.java             # Car/transfer offer API tests
│       │       │
│       │       ├── destinationExperiences/
│       │       │   └── ToursAndActivitesTest.java          # Destination experience API tests
│       │       │
│       │       └── flights/
│       │           ├── FlightOffersTest.java               # Flight offer API tests
│       │           └── FlightInspirationTest.java          # Flight inspiration API tests
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
   mvn test
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
