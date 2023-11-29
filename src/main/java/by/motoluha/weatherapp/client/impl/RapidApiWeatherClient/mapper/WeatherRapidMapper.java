package by.motoluha.weatherapp.client.impl.RapidApiWeatherClient.mapper;

import by.motoluha.weatherapp.client.impl.RapidApiWeatherClient.model.WeatherRapidResponse;
import by.motoluha.weatherapp.entity.Weather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface WeatherRapidMapper {

    @Mapping(source = "location.name", target = "location")
    @Mapping(source = "current.lastUpdated", target = "lastUpdated", qualifiedByName = "stringToLocalDate")
    @Mapping(source = "current.temperature", target = "temperature")
    @Mapping(source = "current.windSpeed", target = "windSpeed")
    @Mapping(source = "current.atmosphericPressure", target = "atmosphericPressure")
    @Mapping(source = "current.humidity", target = "humidity")
    @Mapping(source = "current.weatherConditions", target = "weatherConditions")
    Weather toDto(WeatherRapidResponse weatherResponse);

    @Named("stringToLocalDate")
    static LocalDate stringToLocalDate(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        return dateTime.toLocalDate();
    }
}
