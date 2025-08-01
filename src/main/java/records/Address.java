package records;

public record Address(
        String cityName,
        String cityCode,
        String countryName,
        String countryCode,
        String stateCode,
        String regionCode) {
}