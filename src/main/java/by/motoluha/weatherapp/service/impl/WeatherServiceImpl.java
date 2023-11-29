package by.motoluha.weatherapp.service.impl;

import by.motoluha.weatherapp.client.WeatherClient;
import by.motoluha.weatherapp.dto.WeatherDTO;
import by.motoluha.weatherapp.entity.Weather;
import by.motoluha.weatherapp.mapper.WeatherMapper;
import by.motoluha.weatherapp.repository.WeatherRepository;
import by.motoluha.weatherapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
        log.info("Get latest weather info: {}", firstByOrderByIdDesc);
        return weatherMapper.toDto(firstByOrderByIdDesc);
    }

    @Override
    public WeatherDTO getAverageDailyWeather(LocalDate from, LocalDate to) {
        List<Weather> dailyAverage = new ArrayList<>();

        weatherRepository.findByLastUpdatedBetween(from, to)
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
            Weather firstByOrderByIdDesc = weatherRepository.findFirstByOrderByIdDesc();
            if (firstByOrderByIdDesc == null) {
                weatherRepository.save(currentWeather);
                log.info("Save new weather info " + currentWeather);
            } else {
                currentWeather.setId(firstByOrderByIdDesc.getId());
                if (!currentWeather.equals(firstByOrderByIdDesc)) {
                    weatherRepository.save(currentWeather);
                    log.info("Save new weather info " + currentWeather);
                }
            }
        } catch (Exception e) {
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
