package `object`

data class Coordinates(
    val x: Float, // Maximum value: 755
    val y: Float
) {
    init {
        require(x <= 755) { "X coordinate cannot exceed 755" }
    }
}