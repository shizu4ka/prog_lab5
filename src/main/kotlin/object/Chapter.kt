package `object`

data class Chapter(
    val name: String, // Cannot be null or empty
    val parentLegion: String?,
    val marinesCount: Long // Cannot be null, must be > 0, max 1000
) {
    init {
        require(name.isNotBlank()) { "Chapter name cannot be null or empty" }
        require(marinesCount > 0 && marinesCount <= 1000) {
            "Marines count must be between 1 and 1000"
        }
    }
}