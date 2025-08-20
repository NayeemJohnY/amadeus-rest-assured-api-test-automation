package tests.hotels;

import static testUtils.LoggingMatcher.log;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.BaseTest;

/**
 * Test class for searching hotels by city code using Amadeus API. Demonstrates data-driven testing
 * and custom assertion logging.
 */
@Epic("Hotels Search and Booking")
@Feature("Hotels Search")
@Severity(SeverityLevel.NORMAL)
public class SearchHotelsTest extends BaseTest {

  private static final String HOTELS_BY_CITY = readProperties.getProperty("hotelsByCity");

  /**
   * Searches hotels by city code and validates the IATA code in the response. Uses data-driven
   * testing with a city code data provider.
   *
   * @param cityCode the IATA city code to search hotels for
   */
  @Test(dataProvider = "CityCodes")
  @Description("Test Search Hotels By City Code")
  public void searchHotelsByCityCode(String cityCode) {
    RestAssured.given()
        .queryParam("cityCode", cityCode)
        .when()
        .get(HOTELS_BY_CITY)
        .then()
        .log()
        .ifValidationFails()
        .statusCode(200)
        .body(
            "data.iataCode",
            log(logger, Matchers.everyItem(Matchers.equalTo(cityCode)), "IATA CODE In Response"));
  }

  /**
   * Provides a list of city codes for data-driven hotel search tests.
   *
   * @return array of IATA city codes
   */
  @DataProvider(name = "CityCodes")
  public Object[] getCityCodes() {
    return new Object[] {"BLR", "DEL", "MAA"};
  }
}
