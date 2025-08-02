package tests.destinationExperiences;

import static testUtils.LoggingMatcher.log;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.LinkedHashMap;
import org.hamcrest.Matchers;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import tests.BaseTest;

/**
 * Test class for Destination Experiences API endpoints. Tests functionality for searching tours and
 * activities at specific locations.
 */
@Epic("Destination Experiences")
@Feature("Search Destination Tour and Activities")
@Severity(SeverityLevel.NORMAL)
public class ToursAndActivitesTest extends BaseTest {

  public static final String ACTIVITIES_SEARCH = readProperties.getProperty("activitiesSearch");

  /**
   * Tests searching for destination activities by location. Searches for activities near Barcelona
   * (latitude 41.397158, longitude 2.160873) and stores a highly rated activity's details in the
   * test context.
   *
   * @param context the test context for sharing data between test methods
   */
  @Test
  @Description("Test Search Destination Activities")
  public void testDestinationToursAndActivities(ITestContext context) {

    Response response =
        RestAssured.given()
            .queryParam("latitude", "41.397158")
            .queryParam("longitude", "2.160873")
            .queryParam("radius", 1)
            .get(ACTIVITIES_SEARCH)
            .then()
            .log()
            .ifValidationFails()
            .statusCode(200)
            .body("data.type.unique()", log(logger, Matchers.hasItem("activity"), "Data Type"))
            .extract()
            .response();

    LinkedHashMap<String, Object> ratedActivityMap =
        response
            .jsonPath()
            .getJsonObject("data.find {it.rating != null && it.rating.toDouble() > 4.5}");
    context.setAttribute("ratedActivityID", ratedActivityMap.get("id"));
    context.setAttribute("ratedActivityName", ratedActivityMap.get("name"));
  }

  /**
   * Tests retrieving a specific activity by its ID. Uses the activity ID stored in the test context
   * from the previous test to verify detailed information about a highly rated activity.
   *
   * @param context the test context containing the activity ID and name
   */
  @Test(dependsOnMethods = "testDestinationToursAndActivities")
  @Description("Test Search Destination Activities by Activity ID")
  public void testDesintationTourAndActivityByID(ITestContext context) {
    RestAssured.given()
        .pathParam("activityID", context.getAttribute("ratedActivityID"))
        .when()
        .get(ACTIVITIES_SEARCH + "/{activityID}")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(200)
        .body(
            "data.name",
            log(
                logger,
                Matchers.equalTo(context.getAttribute("ratedActivityName")),
                "Rated Activity Name by ID"))
        .body(
            "data.geoCode",
            log(
                logger,
                Matchers.allOf(Matchers.hasKey("latitude"), Matchers.hasKey("longitude")),
                "Rated Activity should have Geocodes"));
  }
}
