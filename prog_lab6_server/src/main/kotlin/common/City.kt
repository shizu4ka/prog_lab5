package common

import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime

data class City(
    val id: Long,
    val owner: Int,
    var name: String,
    var coordinates: Coordinates,
    var creationDate: LocalDateTime,
    var area: Double,
    var population: BigInteger,
    var metersAboveSeaLevel: Float?,
    var establishmentDate: LocalDate?,
    var climate: Climate?,
    var standardOfLiving: StandardOfLiving?,
    var governor: Human?
) {
    init {
        require(id > 0) { "id must be greater than 0" }
        require(name.isNotEmpty()) { "name cannot be empty" }
        require(area > 0) { "area must be greater than 0" }
        require(population.toLong() > 0) { "population must be greater than 0" }
    }
}

data class Coordinates(
    val x: Double,
    val y: Float
)

data class Human(
    val name: String,
    val height: Int,
    val birthday: LocalDate?
)

enum class Climate {
    OCEANIC, CONTINENTAL, MODERATE, RAIN_FOREST, SNOW
}

enum class StandardOfLiving {
    ULTRA_HIGH, VERY_HIGH, HIGH, MEDIUM, LOW
}