package by.motoluha.weatherapp.controller;

import by.motoluha.weatherapp.dto.WeatherDTO;
import by.motoluha.weatherapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("weather/")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/current/")
    WeatherDTO getCurrentWeather() {
        return weatherService.getLastWeather();
    }

    @GetMapping("/average/")
    WeatherDTO getAverageWeather(@RequestParam() LocalDate from, @RequestParam() LocalDate to) {
        return weatherService.getAverageDailyWeather(from, to);
    }
}
