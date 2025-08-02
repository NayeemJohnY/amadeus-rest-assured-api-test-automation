package records;

import java.util.List;

/**
 * Record for wrapping a list of airport query test data.
 *
 * @param airportQueries list of airport queries with their expected results
 */
public record AirportQueriesWrapper(List<AirportQuery> airportQueries) {

  /**
   * Record representing an airport query test case.
   *
   * @param keyword the search keyword for the airport query
   * @param countryCode the country code to filter results
   * @param expected the list of expected location results
   */
  public static record AirportQuery(String keyword, String countryCode, List<Location> expected) {}
}
