package command

import Collections.City
import Collections.Climate
import Collections.Coordinates
import Collections.Human
import Collections.StandardOfLiving
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

public class CreateObject {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val nextId = if (Cities.cities.isEmpty()) 1L else Cities.cities.last().id + 1

    fun createObject(): City {
        var id = nextId
        var name = nameRead()
        var coordinates = coordinatesRead()
        var creationDate = LocalDateTime.now()
        var area = readArea()
        var population: BigInteger = populationRead()
        var metersAboveSeaLevel: Float? = metersAboveSeaLevelRead()
        var establishmentDate = establishmentDateRead()
        var climate = climateRead()
        var standardOfLiving = standardOfLivingRead()
        var governor = governorRead()
        return City(
            id = id,
            name = name,
            coordinates = coordinates,
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

    private fun nameRead(): String {
        while (true) {
            print("Enter city name: ")
            val input = readlnOrNull()?.trim()
            if (!input.isNullOrEmpty()) {
                print("\n")
                return input
            }
            println("Error: city name is not available")
        }
    }

    private fun coordinatesRead(): Coordinates {
        print("Enter city coordinates:\n")
        while (true) {
            var x: Long? = null
            var y: Float? = null
            print("Enter x: ")
            val inputX = readlnOrNull()?.trim()
            if (!inputX.isNullOrEmpty()) {
                try {
                    x = inputX.toLong()
                    print("\n")
                } catch (e: NumberFormatException) {
                    println("Error: invalid number")
                    continue
                }
            }
            print("Enter y: ")
            val str = readlnOrNull()?.trim()
            if (!str.isNullOrEmpty()) {
                try {
                    val inputY = str.replace(',', '.')
                    y = inputY.toFloat()
                    print("\n")
                } catch (e: NumberFormatException) {
                    println("Error: invalid number")
                    continue
                }
            }
            if (x != null && y != null) {
                var cordinates = Coordinates(x = x, y = y)
                return cordinates
            } else {
                println("Error: invalid coordinates")
            }
        }
    }

    private fun readArea(): Double {
        while (true) {
            print("Enter area: ")
            val input = readlnOrNull()?.trim()
            if (!input.isNullOrEmpty()) {
                try {
                    var area = input.toDouble()
                    print("\n")
                    return area
                } catch (e: NumberFormatException) {
                    println("Error: invalid number")
                }
            } else println("Error: invalid input")
        }
    }

    private fun populationRead(): BigInteger {
        while (true) {
            print("Enter population: ")
            val input = readlnOrNull()?.trim()
            if (!input.isNullOrEmpty()) {
                try {
                    var population = input.toBigInteger()
                    print("\n")
                    return population
                } catch (e: NumberFormatException) {
                    println("Error: invalid number")
                }
            } else println("Error: invalid input")
        }
    }

    private fun metersAboveSeaLevelRead(): Float? {
        while (true) {
            print("Enter meters: ")
            val input = readlnOrNull()?.trim()
            if (input == null || input == "") {
                print("\n")
                return null
            } else if (!input.isNullOrEmpty()) {
                try {
                    var metersAboveSeaLevel = input.toFloat()
                    print("\n")
                    return metersAboveSeaLevel
                } catch (e: NumberFormatException) {
                    println("Error: invalid number")
                }
            }
        }
    }

    private fun establishmentDateRead(): LocalDate? {
        print("Enter establishment date (yyyy-mm-dd) or leave empty: ")
        val input = readlnOrNull()?.trim()
        return if (input.isNullOrEmpty()) {
            null
        } else {
            try {
                var date = LocalDate.parse(input, dateFormatter)
                print("\n")
                return date
            } catch (e: DateTimeParseException) {
                println("Error: invalid date format. Use yyyy-mm-dd")
                establishmentDateRead()
            }
        }
    }

    private fun climateRead(): Climate? {
        println("Available climate types:")
        Collections.Climate.values().forEachIndexed { index, climate ->
            println("${index}. ${climate.name}")
        }
        while (true) {
            print("Select climate (name or leave empty): ")
            var input = readlnOrNull()?.trim()
            if (input.isNullOrEmpty()) {
                print("\n")
                return null
            }
            Collections.Climate.values().forEach { climate ->
                if (climate.toString().equals(input)) {
                    print("\n")
                    return climate
                }
            }
            println("Error: invalid climate")
        }
    }

    private fun standardOfLivingRead(): StandardOfLiving? {
        println("Available standardOfLiving types:")
        Collections.StandardOfLiving.values().forEachIndexed { index, standart ->
            println("${index}. ${standart.name}")
        }
        while (true) {
            print("Select standart (print name or leave empty): ")
            var input = readlnOrNull()?.trim()
            if (input.isNullOrEmpty()) {
                print("\n")
                return null
            }
            Collections.StandardOfLiving.values().forEach { standart ->
                if (standart.toString().equals(input)) {
                    print("\n")
                    return standart
                }
            }
            println("Invalid standardOfLiving")
        }
    }

    private fun governorRead(): Human? {
        print("Enter governor height (must be null): ")
        val input = readlnOrNull()?.trim()
        if (input.isNullOrEmpty()) {
            print("\n")
            return null
        }
        try {
            var height = input.toDouble()
            print("\n")
            return Human(height = height)
        } catch (e: NumberFormatException) {
            println("Error: enter a number")
            governorRead()
        }
        return null
    }
}