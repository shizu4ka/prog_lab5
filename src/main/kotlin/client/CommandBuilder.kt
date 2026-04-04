package client

import common.*
import java.time.LocalDateTime
import java.util.Scanner

/**
 * Builds Command objects from user input
 */
class CommandBuilder(private val scanner: Scanner) {

    /**
     * Build Add command
     */
    fun buildAddCommand(): Command.Add {
        val city = buildCity()
        return Command.Add(city)
    }

    /**
     * Build Update command
     */
    fun buildUpdateCommand(id: Long): Command.Update {
        val city = buildCity()
        return Command.Update(id, city)
    }

    /**
     * Build City from user input
     */
    private fun buildCity(): City {
        val name = InputValidator.readString("Enter city name: ")
        val x = InputValidator.readLong("Enter X coordinate (> -256): ", -256)
        val y = InputValidator.readFloat("Enter Y coordinate (<= 740): ")
        val coordinates = Coordinates(x, y)

        val area = InputValidator.readDouble("Enter area (> 0): ", 0.0)
        val population = InputValidator.readLong("Enter population (> 0): ", 0)

        val metersAboveSeaLevel: Float? = null
        val establishmentDate: java.time.LocalDate? = null

        val climate = readClimate()
        val standardOfLiving = readStandardOfLiving()
        val governor = readGovernor()

        return City(
            id = 0,
            name = name,
            coordinates = coordinates,
            creationDate = LocalDateTime.now(),
            area = area,
            population = population,
            metersAboveSeaLevel = metersAboveSeaLevel,
            establishmentDate = establishmentDate,
            climate = climate,
            standardOfLiving = standardOfLiving,
            governor = governor
        )
    }

    /**
     * Read Climate from user input
     */
    private fun readClimate(): Climate? {
        println("Available climates: ${Climate.values().joinToString(", ")}")
        val input = InputValidator.readString("Enter climate (or leave empty): ", true)
        return if (input.isEmpty()) null else try { Climate.valueOf(input) } catch (e: Exception) { null }
    }

    /**
     * Read StandardOfLiving from user input
     */
    private fun readStandardOfLiving(): StandardOfLiving? {
        println("Available standards: ${StandardOfLiving.values().joinToString(", ")}")
        val input = InputValidator.readString("Enter standard of living (or leave empty): ", true)
        return if (input.isEmpty()) null else try { StandardOfLiving.valueOf(input) } catch (e: Exception) { null }
    }

    /**
     * Read Human (governor) from user input
     */
    private fun readGovernor(): Human? {
        val input = InputValidator.readString("Enter governor height (or leave empty): ", true)
        return if (input.isEmpty()) null else try { Human(input.toDouble()) } catch (e: Exception) { null }
    }
}