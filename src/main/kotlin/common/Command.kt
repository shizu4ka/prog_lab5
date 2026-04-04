package common

import java.io.Serializable

/**
 * Sealed class representing all possible commands that can be sent from client to server
 */
sealed class Command : Serializable {
    /** Help command - display available commands */
    data class Help : Command()

    /** Info command - display collection information */
    data class Info : Command()

    /** Show command - display all elements in collection */
    data class Show : Command()

    /** Add command - add new city to collection */
    data class Add(val city: City) : Command()

    /** Update command - update existing city by id */
    data class Update(val id: Long, val city: City) : Command()

    /** RemoveById command - remove city by id */
    data class RemoveById(val id: Long) : Command()

    /** RemoveAt command - remove city at specific index */
    data class RemoveAt(val index: Int) : Command()

    /** RemoveLast command - remove last city from collection */
    data class RemoveLast : Command()

    /** Clear command - clear entire collection */
    data class Clear : Command()

    /** AddIfMax command - add city if its area is maximum */
    data class AddIfMax(val city: City) : Command()

    /** FilterByStandardOfLiving command - filter cities by standard of living */
    data class FilterByStandardOfLiving(val standard: StandardOfLiving) : Command()

    /** FilterStartsWithName command - filter cities by name prefix */
    data class FilterStartsWithName(val name: String) : Command()

    /** FilterGreaterThanClimate command - filter cities by climate */
    data class FilterGreaterThanClimate(val climate: Climate) : Command()

    /** ExecuteScript command - execute commands from script file */
    data class ExecuteScript(val fileName: String) : Command()

    /** Exit command - terminate client */
    data class Exit : Command()

    /** Save command - save collection to file (server only) */
    data class Save : Command()
}