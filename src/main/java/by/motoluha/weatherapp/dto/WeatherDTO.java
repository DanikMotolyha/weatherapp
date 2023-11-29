package by.motoluha.weatherapp.dto;

public record WeatherDTO(
        Double temperature,
        Double windSpeed,
        Double atmosphericPressure,
        Integer humidity,
        Integer weatherConditions,
        String location
) {
}
