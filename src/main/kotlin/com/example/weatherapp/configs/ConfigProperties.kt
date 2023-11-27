package com.example.weatherapp.configs

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "weather.app")
class ConfigProperties (
        /** url used to retrieve current weather data **/
        val url: String,

        /** default grid location to query weather data for **/
        val defaultLocation: String,

        /** TimeToLive attribute used to evict data as it becomes stale **/
        val savedDbTtl: Int
)
