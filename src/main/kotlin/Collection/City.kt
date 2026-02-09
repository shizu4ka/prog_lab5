package Collection

import java.time.LocalDate
import java.time.LocalDateTime

data class City(
    val id: Long, // Must be > 0, unique, auto-generated
    val name: String, // Non-null, non-empty
    val coordinates: Coordinates, // Non-null
    val creationDate: LocalDateTime, // Non-null, auto-generated
    val area: Double, // Must be > 0
    val population: Int, // Must be > 0, non-null
    val metersAboveSeaLevel: Float?,
    val establishmentDate: LocalDate?,
    val climate: Climate?,
    val standardOfLiving: StandardOfLiving?,
    val governor: Human?
) {
    init {
        require(id > 0) { "id must be greater than 0" }
        require(name.isNotEmpty()) { "name cannot be empty" }
        require(area > 0) { "area must be greater than 0" }
        require(population > 0) { "population must be greater than 0" }
    }
}