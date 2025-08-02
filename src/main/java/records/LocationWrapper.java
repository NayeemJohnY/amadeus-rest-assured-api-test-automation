package records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Record for wrapping location data from API responses.
 * Ignores unknown JSON properties during deserialization.
 *
 * @param data list of location objects from the API response
 */
public record LocationWrapper(List<Location> data) {}
