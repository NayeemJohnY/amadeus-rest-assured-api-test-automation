package tests.flights;

import static testUtils.LoggingMatcher.log;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testUtils.Assertion;
import tests.BaseTest;

/**
 * Test class for Flight Offers API endpoints. Tests functionality for searching available flights
 * with various parameters.
 */
@Epic("Fligts Offer Search and Booking")
@Feature("Fligths Offer Search")
@Severity(SeverityLevel.CRITICAL)
public class FlightOffersTest extends BaseTest {

  public static final String FLIGHT_OFFERS = readProperties.getProperty("flightOffers");

  /**
   * Builds a map of query parameters for flight search.
   *
   * @param originLocationCode IATA code of the departure airport
   * @param destinationLocationCode IATA code of the arrival airport
   * @param departureDate departure date in YYYY-MM-DD format
   * @param returnDate return date in YYYY-MM-DD format (optional)
   * @param adults number of adult passengers (optional)
   * @return a map of query parameters for the API request
   */
  public Map<String, Object> getQueryParams(
      String originLocationCode,
      String destinationLocationCode,
      String departureDate,
      String returnDate,
      Integer adults) {

    Map<String, Object> queryParams = new LinkedHashMap<>();
    queryParams.put("originLocationCode", originLocationCode);
    queryParams.put("destinationLocationCode", destinationLocationCode);

    if (departureDate != null) queryParams.put("departureDate", departureDate);

    if (returnDate != null) queryParams.put("returnDate", returnDate);

    if (adults != null) queryParams.put("adults", adults);
    return queryParams;
  }

  /**
   * Tests searching for flight offers with valid parameters. Verifies the response contains valid
   * flight offers matching the search criteria.
   *
   * @param originLocationCode IATA code of the departure airport
   * @param destinationLocationCode IATA code of the arrival airport
   * @param departureDate departure date in YYYY-MM-DD format
   * @param returnDate return date in YYYY-MM-DD format
   * @param adults number of adult passengers
   */
  @Test(dataProvider = "Valid AirLine Query")
  @Description("Test Search Flight Offers With Valid Data")
  public void testValidSearchFlightOffers(
      String originLocationCode,
      String destinationLocationCode,
      String departureDate,
      String returnDate,
      Integer adults) {

    Map<String, Object> queryParams =
        getQueryParams(
            originLocationCode, destinationLocationCode, departureDate, returnDate, adults);
    Response response =
        RestAssured.given()
            .queryParams(queryParams)
            .when()
            .get(FLIGHT_OFFERS)
            .then()
            .log()
            .ifValidationFails()
            .statusCode(200)
            .body(
                "data.type.unique()",
                log(logger, Matchers.everyItem(Matchers.equalTo("flight-offer")), "Data Type"))
            .body(
                "data.numberOfBookableSeats.unique()",
                log(
                    logger,
                    Matchers.everyItem(Matchers.greaterThanOrEqualTo(adults)),
                    "numberOfBookableSeats"))
            .body(
                "data.itineraries.segments.arrival.iataCode.flatten().unique()",
                log(
                    logger,
                    Matchers.hasItem(destinationLocationCode),
                    "Atleast One Arrival IATACode matching with destination"))
            .body(
                "data.itineraries.segments.departure.iataCode.flatten().unique()",
                log(
                    logger,
                    Matchers.hasItem(originLocationCode),
                    "Atleast One Depature IATACode matching with Origin"))
            .extract()
            .response();

    LocalDate depDate = LocalDate.parse(departureDate);
    Assertion.assertTrue(
        new HashSet<>(
                response.jsonPath().getList("data.itineraries.segments.departure.at.flatten()"))
            .stream()
                .map(ts -> LocalDateTime.parse((String) ts).toLocalDate())
                .allMatch(date -> !date.isBefore(depDate)),
        "All Flights departure should on or after departure date");
  }

  /**
   * Data provider for valid airline queries. Supplies test cases with valid parameters to verify
   * successful flight offer searches.
   *
   * @return an array of test data containing valid query parameters
   */
  @DataProvider(name = "Valid AirLine Query")
  public Object[][] getvalidAirLineQuery() {
    LocalDateTime todaysDateTime = LocalDateTime.now();
    LocalDate date =
        todaysDateTime.getHour() < 16
            ? todaysDateTime.toLocalDate()
            : todaysDateTime.plusHours(10).toLocalDate();
    return new Object[][] {
      {"BLR", "MAA", date.toString(), null, 3},
      {"BLR", "MAA", date.toString(), date.toString(), 3},
      {"BLR", "DEL", date.toString(), date.plusDays(3).toString(), 1},
      {"DEL", "MAA", date.plusDays(5).toString(), date.plusDays(10).toString(), 1}
    };
  }

  /**
   * Tests searching for flight offers with invalid parameters. Verifies the API returns appropriate
   * error messages and status codes for invalid input.
   *
   * @param originLocationCode IATA code of the departure airport
   * @param destinationLocationCode IATA code of the arrival airport
   * @param departureDate departure date in YYYY-MM-DD format (may be invalid or null)
   * @param returnDate return date in YYYY-MM-DD format (may be invalid or null)
   * @param adults number of adult passengers (may be invalid or null)
   * @param errorTtile expected error title in the response
   * @param errorDetail expected error detail in the response
   */
  @Test(dataProvider = "Invalid AirLine Query")
  @Description("Test Search Flight Offers With Invalid Data")
  public void testInvalidSearchFlightOffers(
      String originLocationCode,
      String destinationLocationCode,
      String departureDate,
      String returnDate,
      Integer adults,
      String errorTtile,
      String errorDetail) {

    Map<String, Object> queryParams =
        getQueryParams(
            originLocationCode, destinationLocationCode, departureDate, returnDate, adults);
    RestAssured.given()
        .queryParams(queryParams)
        .when()
        .get(FLIGHT_OFFERS)
        .then()
        .log()
        .ifValidationFails()
        .statusCode(400)
        .body("errors[0].title", log(logger, Matchers.equalTo(errorTtile), "Error Title"))
        .body("errors[0].detail", log(logger, Matchers.equalTo(errorDetail), "Error Detail"));
  }

  /**
   * Data provider for invalid airline queries. Supplies test cases with invalid or missing
   * parameters to validate error handling in the API.
   *
   * @return an array of test data containing invalid query parameters and expected error messages
   */
  @DataProvider(name = "Invalid AirLine Query")
  public Object[][] getInvalidAirLineQuery() {
    LocalDateTime todaysDateTime = LocalDateTime.now();
    LocalDate date =
        todaysDateTime.getHour() < 16
            ? todaysDateTime.toLocalDate()
            : todaysDateTime.plusHours(10).toLocalDate();
    return new Object[][] {
      {
        "BLR",
        "BLR",
        date.plusDays(1).toString(),
        null,
        2,
        "INVALID DATA RECEIVED",
        "The multiple O/D overlap"
      },
      {
        "BLR",
        "NHH",
        date.plusDays(1).toString(),
        null,
        3,
        "INVALID DATA RECEIVED",
        "This location code is unknown"
      },
      {
        "BLR",
        "MAA",
        date.minusDays(2).toString(),
        null,
        3,
        "INVALID DATE",
        "Date/Time is in the past"
      },
      {"BLR", "DEL", null, null, 2, "MANDATORY DATA MISSING", "departureDate needed"},
      {"MAA", "DEL", date.toString(), null, null, "MANDATORY DATA MISSING", "adults needed"},
      {
        "MAA",
        "DEL",
        date.toString(),
        null,
        0,
        "INVALID DATA RECEIVED",
        "adults must be between 1 and 9"
      },
      {
        "BLR",
        "DEL",
        date.toString(),
        date.minusDays(3).toString(),
        4,
        "INVALID DATA RECEIVED",
        "The date/time of OriginDestination are not in chronological order"
      }
    };
  }
}
