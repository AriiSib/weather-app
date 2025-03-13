package com.khokhlov.weather.consts;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Consts {
    public static final String WEATHER_BY_COORDINATES_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric";
    public static final String LOCATION_SEARCH_URL = "https://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s&limit=10";
}
