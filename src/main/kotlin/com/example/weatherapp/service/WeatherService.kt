package com.example.weatherapp.service

import com.example.weatherapp.configs.ConfigProperties

import com.example.weatherapp.data.Forecast
import com.example.weatherapp.data.ReactiveWeatherRepository
import com.example.weatherapp.data.Weather
import com.example.weatherapp.data.WeatherPK
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.cassandra.core.InsertOptions
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class WeatherService (
        private val logger: Logger? = LoggerFactory.getLogger(WeatherService::class.java),
        private val externalWebClient: WebClient,
        private val weatherRepository: ReactiveWeatherRepository,
        private val weatherTemplate: ReactiveCassandraTemplate,
        private val configProperties: ConfigProperties
) {
    /**
     * Fetches the weather data for the current day at the supplied location.
     */
    public suspend fun fetchWeatherDataToday(location: String) : Weather? {
        val currentDay = Calendar.getInstance(TimeZone.getTimeZone("GMT")).get(Calendar.DAY_OF_WEEK)
        weatherRepository.findById(WeatherPK(location,currentDay))
                .onErrorComplete().awaitFirstOrNull()
                ?.let { return it }

        val forecast = callForecastEndpoint(location)

        val weatherData = mapFields(forecast, location)
        weatherData.collect { weather ->
            weatherTemplate.insert(
                    weather,
                    // Using a TimeToLive attribute in Cassandra will evict the data after a given amount of time
                    // this will ensure we are periodically updating our weather data, as time progresses
                    InsertOptions.builder().ttl(configProperties.savedDbTtl).build())
                    .awaitFirst()
        }

        return weatherData.toList().firstOrNull() { weather -> weather.weatherPk.day == currentDay }
    }

    /** Make a web client GET to the forecast api **/
    private suspend fun callForecastEndpoint(location: String): Forecast {
        return externalWebClient.get()
            .uri{builder -> builder.path("gridpoints/MLB/$location/forecast").
                queryParam("units", "si").build()}
            .accept(MediaType.APPLICATION_JSON)
            .awaitExchange {
                if (it.statusCode().is5xxServerError) {
                    val headers: ClientResponse.Headers = it.headers()
                    logger?.error("500 error calling api.weather.gov. " +
                            "X-Correlation-Id: ${headers.header("X-Correlation-Id")}, ")
                    throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                }
                it.awaitBody<Forecast>()
            }
    }

    /** Transforms the data from /forecast endpoint to a Weather object to save locally. **/
    private suspend fun mapFields(forecast: Forecast, location: String): Flow<Weather> {
        val dailyHigh = forecast.properties.periods.groupingBy { it.startTime.get(Calendar.DAY_OF_WEEK) }
                .aggregate { _, accumulator: Float?, element, _ ->
                    accumulator?.let { element.temperature.toFloat().coerceAtLeast(accumulator) }
                        ?: element.temperature.toFloat()

                }

        return forecast.properties.periods.asFlow()
                .filter { period ->
                    period.temperature.toFloat() == dailyHigh[period.startTime.get(Calendar.DAY_OF_WEEK)]
                }.map { period ->
                    val day = period.startTime.get(Calendar.DAY_OF_WEEK)
                    val tempHigh = dailyHigh[day] ?: Float.MIN_VALUE
                    Weather(
                            weatherPk = WeatherPK(
                                    loc = location,
                                    day = day),
                            name = period.startTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH),
                            tempHigh = tempHigh,
                            forecastString = period.shortForecast
                    )}
    }
}

