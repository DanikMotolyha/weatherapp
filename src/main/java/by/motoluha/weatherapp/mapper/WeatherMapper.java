package by.motoluha.weatherapp.mapper;

import by.motoluha.weatherapp.dto.WeatherDTO;
import by.motoluha.weatherapp.entity.Weather;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
    WeatherDTO toDto(Weather weather);
    Weather toModel(WeatherDTO weatherDTO);
}
