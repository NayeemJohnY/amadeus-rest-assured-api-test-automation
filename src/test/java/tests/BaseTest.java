package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.EncryptionUtils;
import utils.ReadProperties;
import utils.RestAssuredLoggerFilter;

public class BaseTest {

    protected static final ReadProperties readProperties = new ReadProperties();
    protected static final String secretKey = System.getProperty("AMADEUS_TEST_SECRET",
            System.getenv("AMADEUS_TEST_SECRET"));
    protected Logger logger = LogManager.getLogger(getClass());

    public String getOauth2Token() {

        String clientId = EncryptionUtils.decrypt(readProperties.getProperty("clientId"), secretKey);
        String clientSecret = EncryptionUtils.decrypt(readProperties.getProperty("clientSecret"), secretKey);
        String grantType = readProperties.getProperty("grantType");
        String tokenURI = readProperties.getProperty("tokenURI");

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .formParam("grant_type", grantType)
                .when()
                .post(tokenURI);

        return response.jsonPath().getString("access_token");
    }

    @BeforeSuite
    public void setUpSuite() {
        RestAssured.filters(new RestAssuredLoggerFilter(), new AllureRestAssured());
        RestAssured.baseURI = readProperties.getProperty("baseURI");
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAuth(RestAssured.oauth2(getOauth2Token())).build();
    }
}
