package by.motoluha.weatherapp.client.impl.RapidApiWeatherClient.model;

public record WeatherRapidResponse(
        Location location,
        Current current
) {
}
