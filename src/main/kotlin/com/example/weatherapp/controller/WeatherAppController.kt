package com.example.weatherapp.controller

import com.example.weatherapp.configs.ConfigProperties

import com.example.weatherapp.data.DailyWeather
import com.example.weatherapp.data.Weather
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.service.WeatherService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
class WeatherAppController(
        val weatherService: WeatherService,
        val configProperties: ConfigProperties
) {

    /*
    *   Default get weather endpoint for MVP. A potential future would be to query
    *   the api.weather.gov/points api to gather data from other locations as well.
    */
    @GetMapping("/weather")
    suspend fun weather(): WeatherResponse =
        weatherService.fetchWeatherDataToday(configProperties.defaultLocation)
                ?.toResponse()
                ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)


    private fun Weather.toResponse(): WeatherResponse =
            WeatherResponse (
                    daily = listOf(DailyWeather(
                            dayName = this.name,
                            tempHighCelsius = this.tempHigh,
                            forecastBlurp = this.forecastString
                    ))
            )

}