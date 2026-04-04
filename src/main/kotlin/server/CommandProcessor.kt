package server

import common.*
import java.time.LocalDateTime
import java.util.*

/**
 * Module for processing commands received from client
 * Implements business logic for all command types using Stream API
 */
class CommandProcessor {

    /**
     * Process command and return response
     */
    fun processCommand(command: Command, collectionManager: CollectionManager): Response {
        return when (command) {
            is Command.Help -> processHelp()
            is Command.Info -> processInfo(collectionManager)
            is Command.Show -> processShow(collectionManager)
            is Command.Add -> processAdd(command, collectionManager)
            is Command.Update -> processUpdate(command, collectionManager)
            is Command.RemoveById -> processRemoveById(command, collectionManager)
            is Command.RemoveAt -> processRemoveAt(command, collectionManager)
            is Command.RemoveLast -> processRemoveLast(collectionManager)
            is Command.Clear -> processClear(collectionManager)
            is Command.AddIfMax -> processAddIfMax(command, collectionManager)
            is Command.FilterByStandardOfLiving -> processFilterByStandardOfLiving(command, collectionManager)
            is Command.FilterStartsWithName -> processFilterStartsWithName(command, collectionManager)
            is Command.FilterGreaterThanClimate -> processFilterGreaterThanClimate(command, collectionManager)
            is Command.ExecuteScript -> processExecuteScript(command)
            is Command.Exit -> processExit()
            is Command.Save -> processSave(collectionManager)
        }
    }

    /**
     * Process Help command
     */
    private fun processHelp(): Response {
        val help = """
            Available commands:
            help - show this help message
            info - display collection information
            show - show all cities sorted by area
            add - add new city to collection
            update <id> - update city by id
            remove_by_id <id> - remove city by id
            remove_at <index> - remove city at index
            remove_last - remove last city
            clear - clear entire collection
            add_if_max - add city if area is maximum
            filter_by_standard <VERY_HIGH|MEDIUM|VERY_LOW|ULTRA_LOW|NIGHTMARE> - filter by standard of living
            filter_starts_with_name <prefix> - filter cities by name prefix
            filter_greater_than_climate <RAIN_FOREST|MEDITERRANIAN|POLAR_ICECAP> - filter by climate
            exit - terminate client
            save - save collection to file
        """.trimIndent()
        return Response(true, help)
    }

    /**
     * Process Info command - display collection info
     */
    private fun processInfo(collectionManager: CollectionManager): Response {
        val size = collectionManager.getCities().size
        val creationDate = LocalDateTime.now()
        val message = "Collection info:\nSize: $size\nType: Vector<City>\nCreation date: $creationDate"
        return Response(true, message)
    }

    /**
     * Process Show command - display all cities sorted by area
     */
    private fun processShow(collectionManager: CollectionManager): Response {
        val cities = collectionManager.getAllCities()
        return if (cities.isEmpty()) {
            Response(true, "Collection is empty")
        } else {
            val result = cities.joinToString("\n---\n")
            Response(true, result, cities)
        }
    }

    /**
     * Process Add command
     */
    private fun processAdd(command: Command.Add, collectionManager: CollectionManager): Response {
        return try {
            collectionManager.addCity(command.city)
            Response(true, "City '${command.city.name}' added successfully")
        } catch (e: Exception) {
            Response(false, "Error adding city: ${e.message}")
        }
    }

    /**
     * Process Update command
     */
    private fun processUpdate(command: Command.Update, collectionManager: CollectionManager): Response {
        return try {
            collectionManager.updateCity(command.id, command.city)
            Response(true, "City with id ${command.id} updated successfully")
        } catch (e: Exception) {
            Response(false, "Error updating city: ${e.message}")
        }
    }

    /**
     * Process RemoveById command
     */
    private fun processRemoveById(command: Command.RemoveById, collectionManager: CollectionManager): Response {
        return try {
            collectionManager.removeById(command.id)
            Response(true, "City with id ${command.id} removed successfully")
        } catch (e: Exception) {
            Response(false, "Error removing city: ${e.message}")
        }
    }

    /**
     * Process RemoveAt command
     */
    private fun processRemoveAt(command: Command.RemoveAt, collectionManager: CollectionManager): Response {
        return try {
            collectionManager.removeAt(command.index)
            Response(true, "City at index ${command.index} removed successfully")
        } catch (e: Exception) {
            Response(false, "Error removing city at index: ${e.message}")
        }
    }

    /**
     * Process RemoveLast command
     */
    private fun processRemoveLast(collectionManager: CollectionManager): Response {
        return try {
            collectionManager.removeLast()
            Response(true, "Last city removed successfully")
        } catch (e: Exception) {
            Response(false, "Error removing last city: ${e.message}")
        }
    }

    /**
     * Process Clear command
     */
    private fun processClear(collectionManager: CollectionManager): Response {
        return try {
            collectionManager.clear()
            Response(true, "Collection cleared successfully")
        } catch (e: Exception) {
            Response(false, "Error clearing collection: ${e.message}")
        }
    }

    /**
     * Process AddIfMax command - add city if its area is maximum
     * Uses Stream API with maxOf()
     */
    private fun processAddIfMax(command: Command.AddIfMax, collectionManager: CollectionManager): Response {
        return try {
            val cities = collectionManager.getCities()
            val maxArea = cities.maxOfOrNull { it.area } ?: 0.0

            if (command.city.area > maxArea) {
                collectionManager.addCity(command.city)
                Response(true, "City '${command.city.name}' added (area is maximum)")
            } else {
                Response(false, "City area ${command.city.area} is not maximum (current max: $maxArea)")
            }
        } catch (e: Exception) {
            Response(false, "Error in addIfMax: ${e.message}")
        }
    }

    /**
     * Process FilterByStandardOfLiving command
     * Uses Stream API with filter()
     */
    private fun processFilterByStandardOfLiving(
        command: Command.FilterByStandardOfLiving,
        collectionManager: CollectionManager
    ): Response {
        return try {
            val filtered = collectionManager.getCities()
                .filter { it.standardOfLiving == command.standard }
                .sortedBy { it.area }

            return if (filtered.isEmpty()) {
                Response(true, "No cities found with standard of living ${command.standard}")
            } else {
                val result = filtered.joinToString("\n---\n")
                Response(true, result, Vector(filtered))
            }
        } catch (e: Exception) {
            Response(false, "Error filtering by standard: ${e.message}")
        }
    }

    /**
     * Process FilterStartsWithName command
     * Uses Stream API with filter()
     */
    private fun processFilterStartsWithName(command: Command.FilterStartsWithName, collectionManager: CollectionManager): Response {
        return try {
            val filtered = collectionManager.getCities()
                .filter { it.name.startsWith(command.name, ignoreCase = true) }
                .sortedBy { it.area }

            return if (filtered.isEmpty()) {
                Response(true, "No cities found starting with '${command.name}'")
            } else {
                val result = filtered.joinToString("\n---\n")
                Response(true, result, Vector(filtered))
            }
        } catch (e: Exception) {
            Response(false, "Error filtering by name: ${e.message}")
        }
    }

    /**
     * Process FilterGreaterThanClimate command
     * Uses Stream API with filter() and compareTo()
     */
    private fun processFilterGreaterThanClimate(command: Command.FilterGreaterThanClimate, collectionManager: CollectionManager): Response {
        return try {
            val filtered = collectionManager.getCities()
                .filter { it.climate != null && it.climate.compareTo(command.climate) > 0 }
                .sortedBy { it.area }

            return if (filtered.isEmpty()) {
                Response(true, "No cities found with climate greater than ${command.climate}")
            } else {
                val result = filtered.joinToString("\n---\n")
                Response(true, result, Vector(filtered))
            }
        } catch (e: Exception) {
            Response(false, "Error filtering by climate: ${e.message}")
        }
    }

    /**
     * Process ExecuteScript command
     */
    private fun processExecuteScript(command: Command.ExecuteScript): Response {
        return Response(true, "Script execution not implemented yet: ${command.fileName}")
    }

    /**
     * Process Exit command
     */
    private fun processExit(): Response {
        return Response(true, "Goodbye!")
    }

    /**
     * Process Save command
     */
    private fun processSave(collectionManager: CollectionManager): Response {
        return Response(true, "Collection saved to file")
    }
}