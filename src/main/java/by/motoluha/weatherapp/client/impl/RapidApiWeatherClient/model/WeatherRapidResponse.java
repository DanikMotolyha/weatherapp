package by.motoluha.weatherapp.client.impl.RapidApiWeatherClient.model;

import by.motoluha.weatherapp.client.impl.RapidApiWeatherClient.model.Current;
import by.motoluha.weatherapp.client.impl.RapidApiWeatherClient.model.Location;

public record WeatherRapidResponse(
        Location location,
        Current current
) {
}
