package tests.flights;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import testUtils.Assertion;
import tests.BaseTest;

@Epic("Chepeast Flight Date Search")
@Feature("Chepeast Flight Date Search")
@Severity(SeverityLevel.NORMAL)
public class FlightInspirationTest extends BaseTest {

    public static final String FLIGHT_DATES = readProperties.getProperty("flightDates");

    @Test
    @Description("Test Cheapest Flight Date Search is Sorted by Price")
    public void testChepestFlightDateSearchIsSortedbyProce() {

        Response response = RestAssured
                .given()
                .queryParam("origin", "MAD")
                .queryParam("destination", "LON")
                .when()
                .get(FLIGHT_DATES)
                .then().log().ifValidationFails()
                .statusCode(200)
                .extract().response();

        List<String> priceList = response.jsonPath().getList("data.price.total");
        Assertion.assertTrue(priceList != null && !priceList.isEmpty(), "Price list should not be empty");
        List<Double> priceValues = priceList.stream().mapToDouble(Double::parseDouble).boxed().toList();
        List<Double> priceValuesSorted = new ArrayList<>(priceValues);
        Collections.sort(priceValuesSorted);
        Assertion.assertEquals(priceValuesSorted, priceValues, "Flight Prices Cheapest to Highest");

    }
}
