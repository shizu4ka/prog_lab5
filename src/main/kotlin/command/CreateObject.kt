package command

import Collections.*
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Scanner

public class CreateObject(
    private val scanner: Scanner,
    private val nextId: Long
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun createObject(): City {
        return City(
            id = nextId,
            name = nameRead(),
            coordinates = coordinatesRead(),
            creationDate = LocalDateTime.now(),
            area = readArea(),
            population = populationRead(),
            metersAboveSeaLevel = metersAboveSeaLevelRead(),
            establishmentDate = establishmentDateRead(),
            climate = readEnum<Climate>("climate"),
            standardOfLiving = readEnum<StandardOfLiving>("standard of living"),
            governor = governorRead()
        )
    }

    private fun nameRead(): String {
        while (true) {
            print("Enter city name: ")
            val input = if (scanner.hasNextLine()) scanner.nextLine().trim() else ""
            if (input.isNotEmpty()) return input
            println("Error: Name cannot be empty.")
        }
    }

    private fun coordinatesRead(): Coordinates {
        println("Enter city coordinates:")
        var x: Long? = null
        while (x == null) {
            print("Enter x (Long): ")
            val input = if (scanner.hasNextLine()) scanner.nextLine().trim() else ""
            x = input.toLongOrNull()
            if (x == null) println("Error: Invalid number format for x.")
        }

        var y: Float? = null
        while (y == null) {
            print("Enter y (Float): ")
            val input = if (scanner.hasNextLine()) scanner.nextLine().trim().replace(',', '.') else ""
            y = input.toFloatOrNull()
            if (y == null) println("Error: Invalid number format for y.")
        }
        return Coordinates(x = x, y = y)
    }

    private fun readArea(): Double {
        while (true) {
            print("Enter area (Double > 0): ")
            val input = if (scanner.hasNextLine()) scanner.nextLine().trim() else ""
            val area = input.toDoubleOrNull()
            if (area != null && area > 0) return area
            println("Error: Area must be a positive number.")
        }
    }

    private fun populationRead(): BigInteger {
        while (true) {
            print("Enter population (BigInteger > 0): ")
            val input = if (scanner.hasNextLine()) scanner.nextLine().trim() else ""
            val pop = try { BigInteger(input) } catch (e: Exception) { null }
            if (pop != null && pop > BigInteger.ZERO) return pop
            println("Error: Population must be a positive integer.")
        }
    }

    private fun metersAboveSeaLevelRead(): Float? {
        while (true) {
            print("Enter meters above sea level (or leave empty for null): ")
            val input = if (scanner.hasNextLine()) scanner.nextLine().trim() else ""
            if (input.isEmpty()) return null
            val meters = input.toFloatOrNull()
            if (meters != null) return meters
            println("Error: Invalid format. Enter a number or leave empty.")
        }
    }

    private fun establishmentDateRead(): LocalDate? {
        while (true) {
            print("Enter establishment date (yyyy-MM-dd) or leave empty: ")
            val input = if (scanner.hasNextLine()) scanner.nextLine().trim() else ""
            if (input.isEmpty()) return null
            try {
                return LocalDate.parse(input, dateFormatter)
            } catch (e: DateTimeParseException) {
                println("Error: Use yyyy-MM-dd format (e.g., 2023-10-25).")
            }
        }
    }

    private inline fun <reified T : Enum<T>> readEnum(typeName: String): T? {
        val values = enumValues<T>()
        println("Available $typeName types: ${values.joinToString { it.name }}")
        while (true) {
            print("Select $typeName (name or leave empty): ")
            val input = if (scanner.hasNextLine()) scanner.nextLine().trim().uppercase() else ""
            if (input.isEmpty()) return null
            val match = values.find { it.name == input }
            if (match != null) return match
            println("Error: '$input' is not a valid $typeName.")
        }
    }

    private fun governorRead(): Human? {
        while (true) {
            print("Enter governor height (Double > 0) or leave empty for null: ")
            val input = if (scanner.hasNextLine()) scanner.nextLine().trim() else ""
            if (input.isEmpty()) return null
            val height = input.toDoubleOrNull()
            if (height != null && height > 0) return Human(height = height)
            println("Error: Height must be a positive number.")
        }
    }
}
