package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RestAssuredLoggerFilter implements Filter {

        private static final Logger logger = LogManager.getLogger(RestAssuredLoggerFilter.class);

        public RestAssuredLoggerFilter() {
        }

        @Override
        public Response filter(FilterableRequestSpecification requestSpec,
                        FilterableResponseSpecification responseSpec,
                        FilterContext filterContext) {

                String requestBody = requestSpec.getBody() == null ? "No Payload" : requestSpec.getBody().toString();

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
