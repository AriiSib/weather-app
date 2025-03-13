package com.khokhlov.weather.service;

import com.khokhlov.weather.config.ApplicationConfig;
import com.khokhlov.weather.exception.WeatherApiException;
import com.khokhlov.weather.model.dto.WeatherDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "test")
@TestPropertySource(properties = "spring.profiles.active=test")
@ContextConfiguration(classes = {ApplicationConfig.class})
class WeatherServiceTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WeatherService weatherService;

    @Test
    void Should_ReturnValidWeatherDTO_When_CallWeatherService() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String jsonResponse = "{" +
                "\"coord\":{\"lon\":-118.243,\"lat\":34.0549}," +
                "\"weather\":[{\"description\":\"clear sky\",\"icon\":\"01d\"}]," +
                "\"main\":{\"temp\":9,\"feels_like\":7.75,\"humidity\":67}," +
                "\"sys\":{\"country\":\"US\"}," +
                "\"name\":\"Los Angeles\"" +
                "}";

        mockServer.expect(ExpectedCount.once(), requestTo(matchesPattern("^https://api\\.openweathermap\\.org/data/2\\.5/weather.*$")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        WeatherDTO weatherDTO = weatherService.getWeatherByCoordinates(34.0549, -118.243);

        assertAll(
                () -> assertEquals("Los Angeles", weatherDTO.getCityName()),
                () -> assertEquals("US", weatherDTO.getCountryName()),
                () -> assertEquals(9, weatherDTO.getTemperature().intValue()),
                () -> assertEquals(8, weatherDTO.getFeelsLike().intValue()),
                () -> assertEquals("clear sky", weatherDTO.getDescription()),
                () -> assertEquals(67, weatherDTO.getHumidity().intValue()),
                () -> assertEquals("01d", weatherDTO.getIcon())
        );

        mockServer.verify();
    }

    @Test
    void Should_ThrowWeatherApiException_When_ApiReturn404() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServer.expect(requestTo(matchesPattern("^https://api\\.openweathermap\\.org/data/2\\.5/weather.*$")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        WeatherApiException exception =
                assertThrows(WeatherApiException.class, () -> weatherService.getWeatherByCoordinates(34.0549, -118.243));

        assertTrue(exception.getMessage().contains("404"));
    }

    @Test
    void Should_ThrowWeatherApiException_When_ApiReturn500() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServer.expect(requestTo(matchesPattern("^https://api\\.openweathermap\\.org/data/2\\.5/weather.*$")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        WeatherApiException exception =
                assertThrows(WeatherApiException.class, () -> weatherService.getWeatherByCoordinates(34.0549, -118.243));

        assertTrue(exception.getMessage().contains("500"));
    }
}