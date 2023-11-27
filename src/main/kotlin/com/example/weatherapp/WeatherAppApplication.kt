package com.example.weatherapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class WeatherAppApplication

fun main(args: Array<String>) {
	runApplication<WeatherAppApplication>(*args)
}
