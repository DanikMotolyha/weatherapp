package by.motoluha.weatherapp.service.impl;

import by.motoluha.weatherapp.client.WeatherClient;
import by.motoluha.weatherapp.dto.WeatherDTO;
import by.motoluha.weatherapp.entity.Weather;
import by.motoluha.weatherapp.exception.WeatherNotFoundException;
import by.motoluha.weatherapp.mapper.WeatherMapper;
import by.motoluha.weatherapp.repository.WeatherRepository;
import by.motoluha.weatherapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;
    private final WeatherClient weatherClient;

    @Override
    public WeatherDTO getLastWeather() {
        Weather firstByOrderByIdDesc = weatherRepository.findFirstByOrderByIdDesc();
        if (firstByOrderByIdDesc == null) {
            throw new WeatherNotFoundException("Database is empty");
        }
        log.info("Get latest weather info: {}", firstByOrderByIdDesc);
        return weatherMapper.toDto(firstByOrderByIdDesc);
    }

    @Override
    public WeatherDTO getAverageDailyWeather(LocalDate from, LocalDate to) {
        List<Weather> dailyAverage = new ArrayList<>();

        List<Weather> byLastUpdatedBetween = weatherRepository.findByLastUpdatedBetween(from, to);
        if (CollectionUtils.isEmpty(byLastUpdatedBetween)) {
            String errorMessage = "No data on interval " + from + " : " + to;
            throw new WeatherNotFoundException(errorMessage);
        }
        byLastUpdatedBetween
                .stream()
                .collect(Collectors.groupingBy(Weather::getLastUpdated))
                .forEach((localDate, weathers) -> dailyAverage.add(buildAverage(weathers)));

        WeatherDTO averageDto = weatherMapper.toDto(buildAverage(dailyAverage));
        log.info("Get daily average weather: {} ", averageDto);
        return averageDto;
    }

    @Scheduled(cron = "${schedule.cron}")
    public void scheduleGetCurrentWeather() {
        try {
            Weather currentWeather = weatherClient.getInformation();
            weatherRepository.save(currentWeather);
            log.info("Save new weather info " + currentWeather);
        } catch (RuntimeException e) {
            log.error("Failed to retrieve or save weather information: {}", e.getMessage());
        }
    }


    private Weather buildAverage(List<Weather> weathers) {
        return Weather.builder()
                .temperature(averageFromList(weathers, Weather::getTemperature))
                .windSpeed(averageFromList(weathers, Weather::getWindSpeed))
                .atmosphericPressure(averageFromList(weathers, Weather::getAtmosphericPressure))
                .humidity(averageFromList(weathers, Weather::getHumidity))
                .weatherConditions(averageFromList(weathers, Weather::getWeatherConditions))
                .location(weathers.stream().map(Weather::getLocation).findAny().orElse(null))
                .build();
    }

    private Double averageFromList(List<Weather> weathers, ToDoubleFunction<? super Weather> mapper) {
        return weathers.stream().mapToDouble(mapper)
                .average()
                .orElse(-1);
    }
}
