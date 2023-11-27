package com.example.weatherapp.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.Builder

@Configuration
class BeanConfigs {

    @Bean
    fun externalWebClient(builder: Builder, configProperties: ConfigProperties): WebClient =
            builder.baseUrl(configProperties.url).build()
}
