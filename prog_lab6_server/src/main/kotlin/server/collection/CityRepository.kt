package server.collection

import common.City
import common.Coordinates
import common.Climate
import common.StandardOfLiving
import common.Human
import server.db.DatabaseManager
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime

object CityRepository {
    fun loadAllCities(): Map<Long, City> {
        val cities = mutableMapOf<Long, City>()
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.createStatement().use { stmt ->
                    val rs = stmt.executeQuery("""
                        SELECT id, owner_id, name, x, y, creation_date, area, population,
                               meters_above_sea_level, establishment_date, climate, 
                               standard_of_living, governor_name, governor_height, governor_birth_date
                        FROM cities
                        ORDER BY id
                    """)
                    
                    while (rs.next()) {
                        val id = rs.getLong("id")
                        val ownerId = rs.getInt("owner_id")
                        val name = rs.getString("name")
                        val x = rs.getDouble("x")
                        val y = rs.getFloat("y")
                        val creationDate = rs.getTimestamp("creation_date")?.toLocalDateTime() 
                            ?: LocalDateTime.now()
                        val area = rs.getDouble("area")
                        val population = BigInteger.valueOf(rs.getLong("population"))
                        val metersAboveSeaLevel = rs.getFloat("meters_above_sea_level")
                            .takeIf { rs.wasNull().not() }
                        val establishmentDate = rs.getDate("establishment_date")?.toLocalDate()
                        val climate = rs.getString("climate")?.let { Climate.valueOf(it) }
                        val standardOfLiving = rs.getString("standard_of_living")?.let { StandardOfLiving.valueOf(it) }
                        
                        val governorName = rs.getString("governor_name")
                        val governor = if (governorName != null) {
                            Human(
                                name = governorName,
                                height = rs.getInt("governor_height"),
                                birthday = rs.getDate("governor_birth_date")?.toLocalDate()
                            )
                        } else null
                        
                        val city = City(
                            id = id,
                            owner = ownerId,
                            name = name,
                            coordinates = Coordinates(x, y),
                            creationDate = creationDate,
                            area = area,
                            population = population,
                            metersAboveSeaLevel = metersAboveSeaLevel,
                            establishmentDate = establishmentDate,
                            climate = climate,
                            standardOfLiving = standardOfLiving,
                            governor = governor
                        )
                        cities[id] = city
                    }
                }
            }
        } catch (e: Exception) {
            println("Error loading cities: ${e.message}")
        }
        return cities
    }

    fun saveCityToDatabase(city: City): Long {
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(
                    """INSERT INTO cities (owner_id, name, x, y, creation_date, area, population,
                       meters_above_sea_level, establishment_date, climate, standard_of_living,
                       governor_name, governor_height, governor_birth_date) 
                       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id""",
                    java.sql.Statement.RETURN_GENERATED_KEYS
                ).use { stmt ->
                    stmt.setInt(1, city.owner)
                    stmt.setString(2, city.name)
                    stmt.setDouble(3, city.coordinates.x)
                    stmt.setFloat(4, city.coordinates.y)
                    stmt.setTimestamp(5, java.sql.Timestamp.valueOf(city.creationDate))
                    stmt.setDouble(6, city.area)
                    stmt.setLong(7, city.population.toLong())
                    stmt.setObject(8, city.metersAboveSeaLevel)
                    stmt.setObject(9, city.establishmentDate?.let { java.sql.Date.valueOf(it) })
                    stmt.setObject(10, city.climate?.name)
                    stmt.setObject(11, city.standardOfLiving?.name)
                    stmt.setObject(12, city.governor?.name)
                    stmt.setObject(13, city.governor?.height)
                    stmt.setObject(14, city.governor?.birthday?.let { java.sql.Date.valueOf(it) })

                    stmt.executeUpdate()
                    val rs = stmt.generatedKeys
                    if (rs.next()) {
                        rs.getLong(1)
                    } else {
                        -1
                    }
                }
            }
        } catch (e: Exception) {
            println("Database error: ${e.message}")
            e.printStackTrace()
            -1
        }
    }

    fun updateCityInDatabase(city: City): Boolean {
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(
                    """UPDATE cities SET name = ?, x = ?, y = ?, area = ?, population = ?,
                       meters_above_sea_level = ?, establishment_date = ?, climate = ?,
                       standard_of_living = ?, governor_name = ?, governor_height = ?,
                       governor_birth_date = ? WHERE id = ? AND owner_id = ?"""
                ).use { stmt ->
                    stmt.setString(1, city.name)
                    stmt.setDouble(2, city.coordinates.x)
                    stmt.setFloat(3, city.coordinates.y)
                    stmt.setDouble(4, city.area)
                    stmt.setLong(5, city.population.toLong())
                    stmt.setObject(6, city.metersAboveSeaLevel)
                    stmt.setObject(7, city.establishmentDate?.let { java.sql.Date.valueOf(it) })
                    stmt.setObject(8, city.climate?.name)
                    stmt.setObject(9, city.standardOfLiving?.name)
                    stmt.setObject(10, city.governor?.name)
                    stmt.setObject(11, city.governor?.height)
                    stmt.setObject(12, city.governor?.birthday?.let { java.sql.Date.valueOf(it) })
                    stmt.setLong(13, city.id)
                    stmt.setInt(14, city.owner)

                    stmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            println("Database error: ${e.message}")
            false
        }
    }

    fun deleteCityFromDatabase(id: Long, userId: Int): Boolean {
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(
                    "DELETE FROM cities WHERE id = ? AND owner_id = ?"
                ).use { stmt ->
                    stmt.setLong(1, id)
                    stmt.setInt(2, userId)
                    stmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            println("Database error: ${e.message}")
            false
        }
    }

    fun deleteAllUserCities(userId: Int): Int {
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(
                    "DELETE FROM cities WHERE owner_id = ?"
                ).use { stmt ->
                    stmt.setInt(1, userId)
                    stmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            println("Database error: ${e.message}")
            0
        }
    }

    fun getCityById(id: Long): City? {
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(
                    """SELECT id, owner_id, name, x, y, creation_date, area, population,
                       meters_above_sea_level, establishment_date, climate, 
                       standard_of_living, governor_name, governor_height, governor_birth_date
                       FROM cities WHERE id = ?"""
                ).use { stmt ->
                    stmt.setLong(1, id)
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        buildCityFromResultSet(rs)
                    } else null
                }
            }
        } catch (e: Exception) {
            println("Database error: ${e.message}")
            null
        }
    }

    private fun buildCityFromResultSet(rs: java.sql.ResultSet): City {
        val id = rs.getLong("id")
        val ownerId = rs.getInt("owner_id")
        val name = rs.getString("name")
        val x = rs.getDouble("x")
        val y = rs.getFloat("y")
        val creationDate = rs.getTimestamp("creation_date")?.toLocalDateTime() ?: LocalDateTime.now()
        val area = rs.getDouble("area")
        val population = BigInteger.valueOf(rs.getLong("population"))
        val metersAboveSeaLevel = rs.getFloat("meters_above_sea_level")
            .takeIf { rs.wasNull().not() }
        val establishmentDate = rs.getDate("establishment_date")?.toLocalDate()
        val climate = rs.getString("climate")?.let { Climate.valueOf(it) }
        val standardOfLiving = rs.getString("standard_of_living")?.let { StandardOfLiving.valueOf(it) }
        
        val governorName = rs.getString("governor_name")
        val governor = if (governorName != null) {
            Human(
                name = governorName,
                height = rs.getInt("governor_height"),
                birthday = rs.getDate("governor_birth_date")?.toLocalDate()
            )
        } else null

        return City(
            id = id,
            owner = ownerId,
            name = name,
            coordinates = Coordinates(x, y),
            creationDate = creationDate,
            area = area,
            population = population,
            metersAboveSeaLevel = metersAboveSeaLevel,
            establishmentDate = establishmentDate,
            climate = climate,
            standardOfLiving = standardOfLiving,
            governor = governor
        )
    }
}
