package Collections

data class Human(
    val height: Double // Must be > 0
) {
    init {
        require(height > 0) { "height must be greater than 0" }
    }
}