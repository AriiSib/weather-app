package com.khokhlov.weather.service;

import com.khokhlov.weather.model.apiweather.OpenWeatherResponse;
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

        OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);

        if (response == null) {
            throw new RuntimeException("OpenWeatherResponse is null");
        }

        return WeatherDTO.builder()
                .cityName(response.getName())
                .countryName(response.getSys().getCountry())
                .temperature((byte) Math.round(response.getMain().getTemp()))
                .feelsLike(response.getMain().getFeelsLike())
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

        OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);

        if (response == null) {
            throw new RuntimeException("OpenWeatherResponse is null");
        }

        return WeatherDTO.builder()
                .cityName(response.getName())
                .countryName(response.getSys().getCountry())
                .temperature((byte) Math.round(response.getMain().getTemp()))
                .feelsLike(response.getMain().getFeelsLike())
                .description(response.getWeather().getFirst().getDescription())
                .humidity(response.getMain().getHumidity())
                .icon(response.getWeather().getFirst().getIcon())
                .build();
    }

}
