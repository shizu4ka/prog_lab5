package server.collection

import common.City
import java.util.*

class SynchronizedCityCollection {
    private val cities = Collections.synchronizedSortedMap(sortedMapOf<Long, City>())

    fun add(city: City): Boolean {
        return cities.put(city.id, city) == null
    }

    fun remove(id: Long): Boolean {
        return cities.remove(id) != null
    }

    fun update(city: City): Boolean {
        return if (cities.containsKey(city.id)) {
            cities[city.id] = city
            true
        } else {
            false
        }
    }

    fun getById(id: Long): City? {
        return cities[id]
    }

    fun getAll(): List<City> {
        return cities.values.toList()
    }

    fun clear() {
        cities.clear()
    }

    fun size(): Int {
        return cities.size
    }

    fun getUserCities(userId: Int): List<City> {
        return cities.values.filter { it.owner == userId }
    }

    fun removeUserCities(userId: Int) {
        val userCityIds = cities.values.filter { it.owner == userId }.map { it.id }
        userCityIds.forEach { cities.remove(it) }
    }
}
