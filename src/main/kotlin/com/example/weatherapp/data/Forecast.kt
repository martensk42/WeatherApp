package com.example.weatherapp.data

import java.util.Calendar


data class Forecast (val properties: Properties)

data class Properties (
        val updateTime: Calendar,
        val generatedAt: Calendar,
        val periods: Array<Period>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Properties

        if (updateTime != other.updateTime) return false
        if (generatedAt != other.generatedAt) return false
        if (!periods.contentEquals(other.periods)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = updateTime.hashCode()
        result = 31 * result + generatedAt.hashCode()
        result = 31 * result + periods.contentHashCode()
        return result
    }
}

data class Period (
        val temperature: Int,
        val name: String,
        val startTime: Calendar,
        val endTime: Calendar,
        val shortForecast: String,
)


