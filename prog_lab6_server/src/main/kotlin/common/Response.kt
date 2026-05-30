package common

import java.io.Serializable
import java.util.*

/**
 * Response class representing server response to client
 * @property success whether command was executed successfully
 * @property message response message
 * @property data collection of cities (if applicable)
 */
data class Response(
    val success: Boolean,
    val message: String,
    val data: Vector<City>? = null
) : Serializable