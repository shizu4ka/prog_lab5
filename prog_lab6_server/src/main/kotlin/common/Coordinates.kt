package common

import java.io.Serializable

/**
 * Data class representing geographical coordinates
 * @property x X coordinate, must be > -256
 * @property y Y coordinate, must be <= 740
 */
data class Coordinates(
    val x: Long,
    val y: Float
) : Comparable<Coordinates>, Serializable {
    init {
        require(x > -256) { "X must be greater than -256" }
        require(y <= 740) { "Y must be less than or equal to 740" }
    }

    override fun compareTo(other: Coordinates): Int {
        return when {
            this.x != other.x -> this.x.compareTo(other.x)
            else -> this.y.compareTo(other.y)
        }
    }
}