package by.motoluha.weatherapp.mapper;

import by.motoluha.weatherapp.dto.WeatherDTO;
import by.motoluha.weatherapp.entity.Weather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
    WeatherDTO toDto(Weather weather);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    Weather toModel(WeatherDTO weatherDTO);
}
