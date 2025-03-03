package com.khokhlov.weather.service;

import com.khokhlov.weather.consts.Consts;
import com.khokhlov.weather.exception.WeatherApiException;
import com.khokhlov.weather.model.apiweather.WeatherResponse;
import com.khokhlov.weather.model.dto.WeatherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String API_KEY;

    public WeatherDTO getWeatherByCoordinates(Double lat, Double lon) {
        String url = String.format(Consts.WEATHER_BY_COORDINATES_URL, lat, lon, API_KEY);
        return getWeatherByUrl(url);
    }

    private WeatherDTO getWeatherByUrl(String url) {
        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            if (response == null)
                throw new RuntimeException("WeatherResponse is null");

            return WeatherDTO.builder()
                    .cityName(response.getLocationName())
                    .countryName(response.getSys().getCountry())
                    .temperature((byte) Math.round(response.getMain().getTemp()))
                    .feelsLike((byte) Math.round(response.getMain().getFeelsLike()))
                    .description(response.getWeather().getFirst().getDescription())
                    .humidity(response.getMain().getHumidity())
                    .icon(response.getWeather().getFirst().getIcon())
                    .build();

        } catch (HttpStatusCodeException e) {
            throw new WeatherApiException("Failed to get weather data. Status: " +
                    e.getStatusCode() + ", Body: " + e.getResponseBodyAsString());
        } catch (RestClientResponseException e) {
            throw new WeatherApiException("Weather API request failed: " + e.getMessage());
        }

    }

}
