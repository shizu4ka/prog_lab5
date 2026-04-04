package common

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Data class representing a City
 *
 * @property id Unique identifier, must be > 0, auto-generated
 * @property name Name of the city, non-null and non-empty
 * @property coordinates Geographic coordinates, non-null
 * @property creationDate Date of creation, auto-generated
 * @property area Area of the city, must be > 0
 * @property population Population of the city, must be > 0
 * @property metersAboveSeaLevel Height above sea level
 * @property establishmentDate Date when city was established
 * @property climate Climate type, nullable
 * @property standardOfLiving Standard of living, nullable
 * @property governor City governor (Human), nullable
 */
data class City(
    val id: Long,
    val name: String,
    val coordinates: Coordinates,
    val creationDate: LocalDateTime,
    val area: Double,
    val population: Long,
    val metersAboveSeaLevel: Float?,
    val establishmentDate: LocalDate?,
    val climate: Climate?,
    val standardOfLiving: StandardOfLiving?,
    val governor: Human?
) : Comparable<City>, Serializable {
    init {
        require(id > 0) { "ID must be greater than 0" }
        require(name.isNotEmpty()) { "Name cannot be empty" }
        require(area > 0) { "Area must be greater than 0" }
        require(population > 0) { "Population must be greater than 0" }
    }

    /**
     * Compare cities by area (for sorting)
     */
    override fun compareTo(other: City): Int = this.area.compareTo(other.area)

    override fun toString(): String {
        return """
            City(
                id=$id,
                name='$name',
                coordinates=$coordinates,
                creationDate=$creationDate,
                area=$area,
                population=$population,
                metersAboveSeaLevel=$metersAboveSeaLevel,
                establishmentDate=$establishmentDate,
                climate=$climate,
                standardOfLiving=$standardOfLiving,
                governor=$governor
            )
        """.trimIndent()
    }
}