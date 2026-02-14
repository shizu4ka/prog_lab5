package Collection

import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.require

data class City(
    val id: Long, // Must be > 0, unique, auto-generated
    var name: String, // Non-null, non-empty
    var coordinates: Coordinates, // Non-null
    var creationDate: LocalDateTime, // Non-null, auto-generated
    var area: Double, // Must be > 0
    var population: BigInteger, // Must be > 0, non-null
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