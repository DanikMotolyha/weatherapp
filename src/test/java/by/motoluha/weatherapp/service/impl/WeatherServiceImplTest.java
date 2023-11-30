package by.motoluha.weatherapp.service.impl;

import by.motoluha.weatherapp.client.WeatherClient;
import by.motoluha.weatherapp.dto.WeatherDTO;
import by.motoluha.weatherapp.entity.Weather;
import by.motoluha.weatherapp.mapper.WeatherMapper;
import by.motoluha.weatherapp.repository.WeatherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class WeatherServiceImplTest {

    @Mock
    private WeatherRepository weatherRepository;
    @Mock
    private WeatherClient weatherClient;

    @Mock
    private WeatherMapper weatherMapper;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLastWeatherPositive() {
        Weather weather = new Weather();
        when(weatherRepository.findFirstByOrderByIdDesc()).thenReturn(weather);
        WeatherDTO lastWeather = weatherService.getLastWeather();
        assertNull(lastWeather);
    }

    @Test
    void getLastWeatherPositiveNull() {
        when(weatherRepository.findFirstByOrderByIdDesc()).thenReturn(null);
        WeatherDTO lastWeather = weatherService.getLastWeather();
        assertNull(lastWeather);
    }

    @Test
    void getAverageDailyWeatherPositive() {
        LocalDate from = LocalDate.of(2023, 11, 29);
        LocalDate to = LocalDate.of(2023, 11, 30);

        List<Weather> weathers = List.of(
                new Weather(1L, LocalDate.of(2023, 11, 30),
                        7.0, 7.0, 7.0, 7.0, 7.0, "location"),
                new Weather(2L, LocalDate.of(2023, 11, 30),
                        5.0, 5.0, 5.0, 5.0, 5.0, "location"),
                new Weather(3L, LocalDate.of(2023, 11, 29),
                        10.0, 10.0, 10.0, 10.0, 10.0, "location"));
        Weather expected = new Weather(null, null, 8.0, 8.0, 8.0, 8.0, 8.0, "location");
        WeatherDTO expectedDto = new WeatherDTO(8.0, 8.0, 8.0, 8, 8, "location");

        when(weatherRepository.findByLastUpdatedBetween(from, to)).thenReturn(weathers);
        when(weatherMapper.toDto(expected)).thenReturn(expectedDto);
        WeatherDTO averageDailyWeather = weatherService.getAverageDailyWeather(from, to);
        Assertions.assertEquals(expectedDto, averageDailyWeather);
    }

    @Test
    void scheduleGetCurrentWeatherException() {
        Weather weather = new Weather();
        when(weatherClient.getInformation()).thenReturn(weather);

        weatherService.scheduleGetCurrentWeather();

        verify(weatherClient, times(1)).getInformation();
        verify(weatherRepository, times(1)).save(weather);

    }
}
