package by.motoluha.weatherapp.client;

import by.motoluha.weatherapp.entity.Weather;

/**
 * Interface representing a Weather Client responsible for fetching weather information.
 */
public interface WeatherClient {

    /**
     * Retrieves weather information.
     *
     * @return Weather object containing the weather information.
     */
    Weather getInformation();
}