package utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A Rest-Assured filter that logs HTTP request and response details using Log4j2. This filter can
 * be added to Rest-Assured requests to log details about the request and response, including
 * method, URI, headers, and body content.
 */
public class RestAssuredLoggerFilter implements Filter {

  private static final Logger logger = LogManager.getLogger(RestAssuredLoggerFilter.class);

  /** Constructs a new RestAssuredLoggerFilter. */
  public RestAssuredLoggerFilter() {}

  /**
   * Filters the HTTP request and response, logging details at appropriate log levels. Request
   * method and URI are logged at INFO level, while headers and body content are logged at DEBUG
   * level.
   *
   * @param requestSpec the request specification
   * @param responseSpec the response specification
   * @param filterContext the filter context
   * @return the filtered response
   */
  @Override
  public Response filter(
      FilterableRequestSpecification requestSpec,
      FilterableResponseSpecification responseSpec,
      FilterContext filterContext) {

    String requestBody =
        requestSpec.getBody() == null ? "No Payload" : requestSpec.getBody().toString();

    // Use location-aware logging, passing FQCN of wrapper class
    logger.info("Request: {} {}", requestSpec.getMethod(), requestSpec.getURI());
    logger.debug("Headers: {}", requestSpec.getHeaders());
    logger.debug("Body: {}", requestBody);

    Response response = filterContext.next(requestSpec, responseSpec);

    logger.info("Response Status: {}", response.getStatusCode());
    logger.debug("Response Body: {}", response.getBody().asPrettyString());

    return response;
  }
}
