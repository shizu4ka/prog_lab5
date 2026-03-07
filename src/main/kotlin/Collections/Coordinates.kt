package Collections

data class Coordinates(
    val x: Long, // Must be > -256, non-null
    val y: Float // Must be ≤ 740, non-null
) {
    init {
        require(x > -256) { "x must be greater than -256" }
        require(y <= 740) { "y must be less than or equal to 740" }
    }
}