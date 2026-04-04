package server

import common.*
import java.time.LocalDateTime
import java.util.*

/**
 * Manages the collection of cities
 * Provides CRUD operations and filtering using Stream API
 */
class CollectionManager {
    private val cities = Vector<City>()
    private var nextId = 1L
    val initializationDate: LocalDateTime = LocalDateTime.now()

    /**
     * Get next available ID for new city
     */
    fun getNextId(): Long = nextId++

    /**
     * Add city to collection
     */
    fun addCity(city: City): Boolean {
        return cities.add(city)
    }

    /**
     * Get all cities sorted by area (using Stream API)
     */
    fun getAllCities(): Vector<City> {
        return cities.stream()
            .sorted()
            .collect({ Vector() }, { v, c -> v.add(c) }, { v1, v2 -> v1.addAll(v2) })
    }

    /**
     * Find city by ID
     */
    fun findById(id: Long): City? {
        return cities.stream()
            .filter { it.id == id }
            .findFirst()
            .orElse(null)
    }

    /**
     * Update city by ID
     */
    fun updateCity(id: Long, updatedCity: City): Boolean {
        val index = cities.indexOfFirst { it.id == id }
        return if (index >= 0) {
            cities[index] = updatedCity
            true
        } else {
            false
        }
    }

    /**
     * Remove city by ID (using Stream API)
     */
    fun removeById(id: Long): Boolean {
        val initialSize = cities.size
        cities.removeIf { it.id == id }
        return cities.size < initialSize
    }

    /**
     * Remove city at index
     */
    fun removeAt(index: Int): Boolean {
        return if (index >= 0 && index < cities.size) {
            cities.removeAt(index)
            true
        } else {
            false
        }
    }

    /**
     * Remove last city
     */
    fun removeLast(): Boolean {
        return if (cities.isNotEmpty()) {
            cities.removeLast()
            true
        } else {
            false
        }
    }

    /**
     * Clear collection
     */
    fun clear() {
        cities.clear()
    }

    /**
     * Get collection size
     */
    fun getSize(): Int = cities.size

    /**
     * Filter cities by standard of living (using Stream API with lambda)
     */
    fun filterByStandardOfLiving(standard: StandardOfLiving): Vector<City> {
        return cities.stream()
            .filter { it.standardOfLiving == standard }
            .sorted()
            .collect({ Vector() }, { v, c -> v.add(c) }, { v1, v2 -> v1.addAll(v2) })
    }

    /**
     * Filter cities by name prefix (using Stream API with lambda)
     */
    fun filterStartsWithName(prefix: String): Vector<City> {
        return cities.stream()
            .filter { it.name.startsWith(prefix) }
            .sorted()
            .collect({ Vector() }, { v, c -> v.add(c) }, { v1, v2 -> v1.addAll(v2) })
    }

    /**
     * Filter cities by climate greater than specified (using Stream API with lambda)
     */
    fun filterGreaterThanClimate(climate: Climate): Vector<City> {
        return cities.stream()
            .filter { it.climate != null && it.climate > climate }
            .sorted()
            .collect({ Vector() }, { v, c -> v.add(c) }, { v1, v2 -> v1.addAll(v2) })
    }

    /**
     * Add city if its population is maximum (using Stream API)
     */
    fun addIfMax(city: City): Boolean {
        if (cities.isEmpty()) {
            return addCity(city)
        }
        
        val maxPopulation = cities.stream()
            .map { it.population }
            .max(Long::compareTo)
            .orElse(0L)
        
        return if (city.population > maxPopulation) {
            addCity(city)
        } else {
            false
        }
    }

    /**
     * Get collection info
     */
    fun getInfo(): String {
        return """Collection Info:
            |Type: ${'$'}{cities::class.simpleName}
            |Initialization Date: $initializationDate
            |Elements: ${'$'}{cities.size}
            |""".trimMargin()
    }
}