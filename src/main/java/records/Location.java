package records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Record representing a location in the Amadeus API.
 *
 * @param type the type of location
 * @param subType the subtype of location
 * @param name the short name of the location
 * @param detailedName the full name of the location
 * @param timeZoneOffset the timezone offset from UTC
 * @param iataCode the IATA code for the location
 * @param address the address details of the location
 */
public record Location(
    String type,
    String subType,
    String name,
    String detailedName,
    String timeZoneOffset,
    String iataCode,
    Address address) {}
