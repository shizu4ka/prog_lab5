package `object`

import java.time.LocalDate

data class SpaceMarine(
    val id: Long,
    val name: String,
    val coordinates: Coordinates,
    val creationDate: LocalDate,
    val health: Long?,
    val heartCount: Long?,
    val weaponType: Weapon,
    val meleeWeapon: MeleeWeapon,
    val chapter: Chapter
) {
    init {
        require(id > 0) { "ID must be greater than 0" }
        require(name.isNotBlank()) { "Name cannot be null or empty" }
        health?.let {
            require(it > 0) { "Health must be greater than 0" }
        }
        heartCount?.let {
            require(it > 0) { "Heart count must be greater than 0" }
            require(it <= 3) { "Heart count cannot exceed 3" }
        }
    }
}