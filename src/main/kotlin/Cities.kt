import Collection.City
import Collection.Climate
import Collection.StandardOfLiving
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Vector

object Cities {
    var cities = Vector<City>()
    var maxPopulation: BigInteger = BigInteger.ZERO
    val initializationDate: LocalDateTime = LocalDateTime.now()
    val format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    fun getInfo(): PrintResult {
        var result: PrintResult = PrintResult(
            String = "Collection type: ${cities::class}\n" +
                    "vector initialization date: ${initializationDate.format(format)}\n" +
                    "Cities: ${cities.size}"
        )
        return result
    }

    fun getCollection(): PrintResult {
        var collectionInfo: String = ""
        for (c in cities) {
            collectionInfo = collectionInfo + c.toString() + "\n"
        }
        var result: PrintResult = PrintResult(String = collectionInfo)
        if (result.String != "") return result
        else {
            result.String = "Collection is empty"
            return result
        }
    }

    fun clearCollection() {
        cities.clear()
    }

    fun addCity(city: City) {
        cities.add(city)
    }

    fun updateElement(id: Long) {
        var CreateObject = CreateObject()
        try {
            var flag = false
            for (c in cities) {
                if (id == c.id) {
                    var index: Int = cities.indexOf(c)
                    cities[index] = CreateObject.createObject()
                    println("Ubdate element by id: $id")
                    flag = true
                }
            }
            if (!flag) println("There is no item with this id in the collection.")
        } catch (e: Exception) {
            println("There is no item with this id in the collection.")
        }
    }

    fun remove_by_id(id: Long) {
        try {
            var flag = false
            for (c in cities) {
                if (id == c.id) {
                    cities.remove(c)
                    println("Remove element by id: $id")
                    flag = true
                    break
                }
            }
            if (!flag) println("There is no item with this id in the collection.")
        } catch (e: Exception) {
            println("There is no item with this id in the collection.")
        }
    }

    fun remove_at(index: Int) {
        try {
            var flag = false
            if (index < cities.size) {
                cities.removeAt(index)
                flag = true
            }
            if (!flag) println("There is no item with this index in the collection.")
        } catch (e: Exception) {
            println("There is no item with this index in the collection.")
        }
    }

    fun removeLastElement() {
        if (cities.isNotEmpty()) {
            cities.removeLast()
        } else println("Collection is empty")
    }

    fun add_if_max() {
        for (c in cities) {
            if (maxPopulation < c.population) {
                maxPopulation = c.population
            }
        }
        var CreateObject = CreateObject()
        var newCity = CreateObject.createObject()
        if (newCity.population <= maxPopulation) {
            println("Population is too small")
        } else {
            cities.add(newCity)
            println("City is created")
        }
    }

    fun filter_by_standard_of_living(standard: StandardOfLiving): Vector<City>? {
        var result = Vector<City>()
        for (c in cities) {
            if (c.standardOfLiving == standard) {
                result.add(c)
            }
        }
        if (result.isNotEmpty()) return result
        else return null
    }

    fun filter_starts_with_name(name: String): Vector<City>? {
        var result = Vector<City>()
        for (c in cities) {
            if (c.name.startsWith(name)) {
                result.add(c)
            }
        }
        if (result.isNotEmpty()) return result
        else return null
    }

    fun filter_greater_than_climate(climate: Climate): Vector<City>? {
        var result = Vector<City>()
        for (c in cities) {
            if(c.climate != null) {
                if (c.climate!!.ordinal > climate.ordinal) {
                    result.add(c)
                }
            }
        }
        if (result.isNotEmpty()) return result
        else return null
    }
}
