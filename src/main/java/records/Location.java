package records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Location(
        String type,
        String subType,
        String name,
        String detailedName,
        String timeZoneOffset,
        String iataCode,
        Address address) {
}
