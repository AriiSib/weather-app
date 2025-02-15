package com.khokhlov.weather.consts;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Consts {
    public static final String GET_LOCATION_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
}
