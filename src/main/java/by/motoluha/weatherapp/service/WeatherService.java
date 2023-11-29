package by.motoluha.weatherapp.service;

import by.motoluha.weatherapp.dto.WeatherDTO;

import java.time.LocalDate;

/**
 * Interface representing a Weather Service providing weather-related functionalities.
 */
public interface WeatherService {

    /**
     * Retrieves the last weather update.
     *
     * @return WeatherDTO containing the latest weather information.
     */
    WeatherDTO getLastWeather();

    /**
     * Retrieves the average daily weather information within a specified date range.
     *
     * @param from Start date of the interval.
     * @param to   End date of the interval.
     * @return WeatherDTO containing the average daily weather information within the specified date range.
     */
    WeatherDTO getAverageDailyWeather(LocalDate from, LocalDate to);
}
