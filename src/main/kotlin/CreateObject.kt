import Collection.City;
import Collection.Climate
import Collection.Coordinates
import Collection.Human
import Collection.StandardOfLiving
import java.math.BigInteger
import java.time.LocalDate

import java.time.LocalDateTime;
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
                } catch (e: NumberFormatException) {
                    println("Error: invalid number")
                    continue
                }
            }
            print("Enter y: ")
            val inputY = readlnOrNull()?.trim()
            if (!inputY.isNullOrEmpty()) {
                try {
                    y = inputY.toFloat()
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
                    return area
                } catch (e: NumberFormatException) {
                    println("Error: invalid number")
                }
            }
        }
    }

    private fun populationRead(): BigInteger {
        while (true) {
            print("Enter population: ")
            val input = readlnOrNull()?.trim()
            if (!input.isNullOrEmpty()) {
                try {
                    var population = input.toBigInteger()
                    return population
                } catch (e: NumberFormatException) {
                    println("Error: invalid number")
                }
            }
        }
    }

    private fun metersAboveSeaLevelRead(): Float? {
        while (true) {
            print("Enter meters: ")
            val input = readlnOrNull()?.trim()
            if (input == null || input == "") {
                return null
            } else if (!input.isNullOrEmpty()) {
                try {
                    var metersAboveSeaLevel = input.toFloat()
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
                return date
            } catch (e: DateTimeParseException) {
                println("Error: invalid date format. Use yyyy-mm-dd")
                establishmentDateRead()
            }
        }
    }

    private fun climateRead(): Climate? {
        println("Available climate types:")
        Climate.values().forEachIndexed { index, climate ->
            println("${index}. ${climate.name}")
        }
        print("Select climate (number or leave empty): ")
        var input = readlnOrNull()?.trim()
        if (input.isNullOrEmpty()) {
            return null
        }
        try {
            val index = input.toInt()
            if (index < Climate.values().size) {
                return Climate.values()[index]
            } else {
                println("Error: enter a number")
                climateRead()
            }
        } catch (e: NumberFormatException) {
            println("Error: enter a number")
            climateRead()
        }
        return null
    }

    private fun standardOfLivingRead(): StandardOfLiving? {
        println("Available standardOfLiving types:")
        StandardOfLiving.values().forEachIndexed { index, standart ->
            println("${index}. ${standart.name}")
        }
        print("Select standart (number or leave empty): ")
        var input = readlnOrNull()?.trim()
        if (input.isNullOrEmpty()) {
            return null
        }
        try {
            val index = input.toInt()
            if (index < StandardOfLiving.values().size) {
                return StandardOfLiving.values()[index]
            } else {
                println("Error: enter a number")
                standardOfLivingRead()
            }
        } catch (e: NumberFormatException) {
            println("Error: enter a number")
            standardOfLivingRead()
        }
        return null
    }

    private fun governorRead(): Human? {
        print("Enter governor height (must be null): ")
        val input = readlnOrNull()?.trim()
        if (input.isNullOrEmpty()) {
            return null
        }
        try {
            var height = input.toDouble()
            return Human(height = height)
        } catch (e: NumberFormatException) {
            println("Error: enter a number")
            governorRead()
        }
        return null
    }
}