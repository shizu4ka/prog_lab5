package common

import java.io.Serializable

data class Command(
    val type: String,
    val params: Map<String, Any> = emptyMap()
) : Serializable

data class Response(
    val success: Boolean,
    val message: String,
    val data: String = ""
) : Serializable