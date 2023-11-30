package by.motoluha.weatherapp.client.impl.rapidapiweatherclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Current(
        @JsonProperty("last_updated")
        String lastUpdated,
        @JsonProperty("temp_c")
        Double temperature,
        @JsonProperty("wind_mph")
        Double windSpeed,
        @JsonProperty("pressure_mb")
        Double atmosphericPressure,
        Double humidity,
        @JsonProperty("cloud")
        Double weatherConditions
) {
}
