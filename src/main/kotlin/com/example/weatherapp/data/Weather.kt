package com.example.weatherapp.data

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table


@Table
class Weather(
        @PrimaryKey val weatherPk: WeatherPK,
        val name: String,
        val tempHigh: Float,
        val forecastString: String
)

@PrimaryKeyClass
data class WeatherPK (
    // The grid location is the partition key because the weather is unique to each location
    @PrimaryKeyColumn(name = "location", type = PrimaryKeyType.PARTITIONED)
    val loc: String,
    // Also storing each day as part of the key to have a record of future weather data
    // assuming the need for multiple days will be required
    @PrimaryKeyColumn(name = "day", ordinal = 1)
    val day: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeatherPK

        if (loc != other.loc) return false
        if (day != other.day) return false

        return true
    }

    override fun hashCode(): Int {
        var result = loc.hashCode()
        result = 31 * result + day
        return result
    }
}



