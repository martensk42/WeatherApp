package com.example.weatherapp.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * /weather API response information
 */
data class WeatherResponse(
        val daily :List<DailyWeather>
)

data class DailyWeather (
        @JsonProperty("day_name") val dayName: String,
        @JsonProperty("temp_high_celsius") val tempHighCelsius: Float,
        @JsonProperty("forecast_blurp") val forecastBlurp: String
)
