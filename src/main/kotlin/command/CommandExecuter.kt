package command

import Collections.Climate
import Collections.StandardOfLiving
import FileManager
import PrintResult
import java.util.Scanner

class CommandHandler {
    private val commands: HashMap<String, (String?, Scanner) -> Unit> = hashMapOf()

    init {
        commands["exit"] = { _, _ -> exit() }
        commands["help"] = { _, _ -> HelpCommand().result.printResult() }
        commands["history"] = { _, _ -> History.execute().printResult() }

        commands["add"] = { _, sc ->
            val creator = CreateObject(sc, Cities.getNextId())
            try {
                val obj = creator.createObject()
                Cities.addCity(obj)
                println("New city is created")
            } catch (e: Exception) {
                println("Error: could not create city. Check input data.")
            }
        }

        commands["info"] = { _, _ -> Cities.getInfo().printResult() }
        commands["show"] = { _, _ -> Cities.getCollection().printResult() }
        commands["clear"] = { _, _ ->
            Cities.clearCollection()
            println("Collection cleared")
        }
        commands["remove_last"] = { _, _ ->
            Cities.removeLastElement()
            println("Last element removed")
        }
        commands["add_if_max"] = { _, sc ->
            Cities.add_if_max(sc)
        }
        commands["save"] = { _, _ -> FileManager().save() }
    }

    fun findCommand(input: String?, sc: Scanner) {
        if (input.isNullOrBlank()) return

        val parts = input.split(Regex("\\s+"), 2)
        val cmdName = parts[0].lowercase()
        val arg = if (parts.size > 1) parts[1] else null

        when (cmdName) {
            "update" -> handleUpdate(arg, sc)
            "remove_by_id" -> handleRemoveById(arg)
            "remove_at" -> handleRemoveAt(arg)
            "filter_by_standard_of_living" -> handleFilterByStandard(arg)
            "filter_starts_with_name" -> handleFilterStartsWithName(arg)
            "filter_greater_than_climate" -> handleFilterGreaterThanClimate(arg)
            "execute_script" -> handleExecuteScript(arg)
            else -> {
                val handler = commands[cmdName]
                if (handler != null) handler(arg, sc)
                else println("Unknown command: $cmdName")
            }
        }
    }

    private fun handleUpdate(arg: String?, sc: Scanner) {
        val id = arg?.toLongOrNull() ?: return println("Error: ID must be a number")
        Cities.updateElement(id, sc)
    }

    private fun handleRemoveById(arg: String?) {
        val id = arg?.toLongOrNull() ?: return println("Invalid ID")
        Cities.remove_by_id(id)
    }

    private fun handleRemoveAt(arg: String?) {
        val index = arg?.toIntOrNull() ?: return println("Invalid index")
        Cities.remove_at(index)
    }

    private fun handleFilterByStandard(arg: String?) {
        val standard = StandardOfLiving.values().find { it.name.equals(arg, true) }
            ?: return println("Invalid standard. Available: ${StandardOfLiving.values().joinToString()}")

        val filtered = Cities.filter_by_standard_of_living(standard)
        if (filtered.isNullOrEmpty()) println("No matches.")
        else PrintResult(filtered.joinToString("\n")).printResult()
    }

    private fun handleFilterStartsWithName(arg: String?) {
        if (arg == null) return println("Name prefix required")
        val result = Cities.filter_starts_with_name(arg)
        if (result.isNullOrEmpty()) println("No matches.")
        else PrintResult(result.joinToString("\n")).printResult()
    }

    private fun handleFilterGreaterThanClimate(arg: String?) {
        val climate = Climate.values().find { it.name.equals(arg, true) }
            ?: return println("Invalid climate.")

        val result = Cities.filter_greater_than_climate(climate)
        if (result.isNullOrEmpty()) println("No matches.")
        else PrintResult(result.joinToString("\n")).printResult()
    }

    private fun handleExecuteScript(arg: String?) {
        if (arg == null) return println("Path required")
        FileManager().execute_script(arg)
    }

    fun exit() {
        println("Exiting...")
        System.exit(0)
    }
}

private val handler = CommandHandler()

fun findCommand(command: String?, sc: Scanner) {
    handler.findCommand(command, sc)
}

fun exit() {
    handler.exit()
}
