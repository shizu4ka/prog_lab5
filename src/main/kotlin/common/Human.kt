package common

import java.io.Serializable

/**
 * Data class representing a human (governor)
 * @property height Height of the human, must be > 0
 */
data class Human(
    val height: Double
) : Serializable {
    init {
        require(height > 0) { "Height must be greater than 0" }
    }
}