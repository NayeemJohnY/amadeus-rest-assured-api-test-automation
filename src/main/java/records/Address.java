package records;

/**
 * Record representing an address in the Amadeus API.
 *
 * @param cityName the name of the city
 * @param cityCode the city code
 * @param countryName the name of the country
 * @param countryCode the ISO country code
 * @param stateCode the state/province code
 * @param regionCode the region code
 */
public record Address(
    String cityName,
    String cityCode,
    String countryName,
    String countryCode,
    String stateCode,
    String regionCode) {}
