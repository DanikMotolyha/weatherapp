package by.motoluha.weatherapp.client.impl.RapidApiWeatherClient;

import by.motoluha.weatherapp.client.WeatherClient;
import by.motoluha.weatherapp.client.impl.RapidApiWeatherClient.mapper.WeatherRapidMapper;
import by.motoluha.weatherapp.client.impl.RapidApiWeatherClient.model.WeatherRapidResponse;
import by.motoluha.weatherapp.entity.Weather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RapidWeatherClient implements WeatherClient {

    private final WeatherRapidMapper mapper;
    private final RestTemplate restTemplate;

    @Value("${rapid.api.key}")
    private String apiKey;

    @Value("${rapid.api.host}")
    private String apiHost;

    @Value("${rapid.api.url}")
    private String apiUrl;

    @Override
    public Weather getInformation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("X-RapidAPI-Key", apiKey);
        headers.set("X-RapidAPI-Host", apiHost);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<WeatherRapidResponse> responseEntity = restTemplate.exchange(
                apiUrl, HttpMethod.GET, entity, WeatherRapidResponse.class);

        WeatherRapidResponse body = responseEntity.getBody();
        log.info("Get weather info from Rapid API: {}", body);
        return mapper.toDto(responseEntity.getBody());
    }
}