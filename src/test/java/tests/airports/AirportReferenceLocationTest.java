package tests.airports;

import static testUtils.LoggingMatcher.log;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import records.AirportQueriesWrapper;
import records.AirportQueriesWrapper.AirportQuery;
import records.LocationWrapper;
import testUtils.Assertion;
import tests.BaseTest;
import utils.JsonUtils;

/**
 * Test class for Airport and City Reference Location API endpoints. Tests querying airport and city
 * locations by various criteria.
 */
@Epic("Airport & City Reference Locations Query")
@Feature("Airport and City Locations")
@Severity(SeverityLevel.NORMAL)
public class AirportReferenceLocationTest extends BaseTest {

  public static final String AIRPORT_LOCATIONS = readProperties.getProperty("airportLocations");

  /**
   * Tests the airport reference data locations API endpoint.
   * Verifies that the API returns correct location data for given search criteria.
   *
   * @param airportQuery the query parameters and expected results for the test
   */
  @Test(dataProvider = "Airport Location Query")
  @Description("Test Airport Reference Data Locations")
  public void testAirportReferenceDataLocations(AirportQuery airportQuery) {
    Response response =
        RestAssured.given()
            .queryParam("subType", "CITY,AIRPORT")
            .queryParam("keyword", (String) airportQuery.keyword())
            .queryParam("countryCode", (String) airportQuery.countryCode())
            .get(AIRPORT_LOCATIONS)
            .then()
            .log()
            .ifValidationFails()
            .statusCode(200)
            .body(
                "meta.count",
                log(logger, Matchers.equalTo(airportQuery.expected().size()), "Response Count"))
            .extract()
            .response();

    LocationWrapper locationWrapper =
        JsonUtils.fromJson(response.asString(), LocationWrapper.class);
    Assertion.assertEquals(
        locationWrapper.data(), airportQuery.expected(), "Airport Location data");
  }

  /**
   * Data provider for airport location tests.
   * Loads test data from a JSON file containing various query scenarios and expected results.
   *
   * @return an array of test data objects containing query parameters and expected results
   */
  @DataProvider(name = "Airport Location Query")
  public Object[][] getAirportLocationQuery() {
    AirportQueriesWrapper airportQueriesWrapper =
        JsonUtils.fromJson("airport_locations_test_data.json", AirportQueriesWrapper.class, true);
    return airportQueriesWrapper.airportQueries().stream()
        .map(query -> new Object[] {query})
        .toArray(Object[][]::new);
  }
}
