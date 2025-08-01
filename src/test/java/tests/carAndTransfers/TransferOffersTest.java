package tests.carAndTransfers;

import static testUtils.LoggingMatcher.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import testUtils.Assertion;
import tests.BaseTest;

@Epic("Car and Transfer Offers")
@Feature("Search Car and Transfer offers")
@Severity(SeverityLevel.NORMAL)
public class TransferOffersTest extends BaseTest {

    public static final String TRANSFER_OFFERS = readProperties.getProperty("transferOffers");

    @Test
    @Description("Test Search Transfer Offers")
    public void testSearchTransferOffers() {

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(10);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("startLocationCode", "BLR");
        payload.put("endAddressLine", "MTP");
        payload.put("endCityName", "BENGALURU");
        payload.put("endCountryCode", "IN");
        payload.put("endGeoCode", "13.045484,77.606354");
        payload.put("startDateTime", startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        payload.put("passengers", 2);

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(TRANSFER_OFFERS)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("data.type.unique()",
                        log(logger, Matchers.hasItem("transfer-offer"), "Data Type"))
                .body("data.transferType.unique()",
                        log(logger, Matchers.hasItem("PRIVATE"), "Atleast 1 Private transfer type"))
                .extract().response();

        List<String> startTimeList = response.jsonPath().getList("data.start.dateTime");
        Assertion.assertTrue(
                startTimeList.stream().map(LocalDateTime::parse).allMatch(st -> !st.isBefore(startDateTime)),
                "Start time of transfer should not be before the given start time");

    }

}
