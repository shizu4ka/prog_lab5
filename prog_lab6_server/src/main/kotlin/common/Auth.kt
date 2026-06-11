package common

data class AuthRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val userId: Int? = null,
    val message: String = ""
)