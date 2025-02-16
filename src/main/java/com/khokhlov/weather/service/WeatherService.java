package com.khokhlov.weather.service;

import com.khokhlov.weather.model.apiweather.WeatherResponse;
import com.khokhlov.weather.model.dto.WeatherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String API_KEY;

    public WeatherDTO getWeatherForCity(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "?q=" + city
                + "&appid=" + API_KEY
                + "&units=metric";

        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

        if (response == null) {
            throw new RuntimeException("WeatherResponse is null");
        }

        return WeatherDTO.builder()
                .cityName(response.getLocationName())
                .countryName(response.getSys().getCountry())
                .temperature((byte) Math.round(response.getMain().getTemp()))
                .feelsLike((byte) Math.round(response.getMain().getFeelsLike()))
                .description(response.getWeather().getFirst().getDescription())
                .humidity(response.getMain().getHumidity())
                .icon(response.getWeather().getFirst().getIcon())
                .build();
    }

    public WeatherDTO getWeatherByCoordinate(Double lat, Double lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "&lat=" + lat
                + "&lon=" + lon
                + "&appid=" + API_KEY
                + "&units=metric";

        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

        if (response == null) {
            throw new RuntimeException("WeatherResponse is null");
        }

        return WeatherDTO.builder()
                .cityName(response.getLocationName())
                .countryName(response.getSys().getCountry())
                .temperature((byte) Math.round(response.getMain().getTemp()))
                .feelsLike((byte) Math.round(response.getMain().getFeelsLike()))
                .description(response.getWeather().getFirst().getDescription())
                .humidity(response.getMain().getHumidity())
                .icon(response.getWeather().getFirst().getIcon())
                .build();
    }

}
