package com.example.weatherapp.data

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ReactiveWeatherRepository : ReactiveCrudRepository<Weather, WeatherPK> {}


