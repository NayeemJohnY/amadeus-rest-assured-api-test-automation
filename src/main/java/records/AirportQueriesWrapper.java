package records;

import java.util.List;

public record AirportQueriesWrapper(List<AirportQuery> airportQueries) {
    public static record AirportQuery(
            String keyword,
            String countryCode,
            List<Location> expected) {
    }
}
