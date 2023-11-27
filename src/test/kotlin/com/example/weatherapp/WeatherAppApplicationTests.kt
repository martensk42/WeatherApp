package com.example.weatherapp

import com.example.weatherapp.data.Forecast
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono
import java.util.Properties
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


@SpringBootTest
class WeatherAppApplicationTests {


	val webClient: WebClient = WebClient.create("https://api.weather.gov")

	@Test
	fun contextLoads() {
	}

}
