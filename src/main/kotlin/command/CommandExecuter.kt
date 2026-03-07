package command

import Collections.Climate
import Collections.StandardOfLiving
import FileManager
import PrintResult

class CommandHandler {
    private val commands: HashMap<String, (String?) -> Unit> = hashMapOf()

    init {
        commands["exit"] = { exit() }
        commands["help"] = {
            val helpCommand = HelpCommand()
            helpCommand.result.printResult()
        }
        commands["history"] = {
            val result = History.execute()
            result.printResult()
        }
        commands["add"] = {
            val result = CreateObject()
            try {
                val obj = result.createObject()
                Cities.addCity(obj)
                println("New city is created")
            } catch (e: Exception) {
                println("Error with add element")
            }
        }
        commands["info"] = {
            val result: PrintResult = Cities.getInfo()
            result.printResult()
        }
        commands["show"] = {
            val result = Cities.getCollection()
            result.printResult()
        }
        commands["clear"] = {
            try {
                Cities.clearCollection()
                println("Collection cleared")
            } catch (e: Exception) {
                println("Error with Clearing Cities")
            }
        }
        commands["remove_last"] = {
            try {
                Cities.removeLastElement()
                println("Remove element by last")
            } catch (e: Exception) {
                println("Error with remove element")
            }
        }
        commands["add_if_max"] = {
            Cities.add_if_max()
        }
        commands["save"] = {
            val fileManager = FileManager()
            fileManager.save()
        }
    }
    fun findCommand(command: String?) {
        if (command.isNullOrBlank()) return

        // Проверяем команды с параметрами
        when {
            command.matches("^update \\d+$".toRegex()) -> handleUpdate(command)
            command.matches("^remove_by_id \\d+$".toRegex()) -> handleRemoveById(command)
            command.matches("^remove_at \\d+$".toRegex()) -> handleRemoveAt(command)
            command.matches("^filter_by_standard_of_living \\w+$".toRegex()) -> handleFilterByStandard(command)
            command.matches("^filter_starts_with_name \\w+$".toRegex()) -> handleFilterStartsWithName(command)
            command.matches("^filter_greater_than_climate \\w+$".toRegex()) -> handleFilterGreaterThanClimate(command)
            command.matches("^execute_script \\w+$".toRegex()) -> handleExecuteScript(command)
            else -> {
                val handler = commands[command]
                if (handler != null) {
                    handler(command)
                }
            }
        }
    }
    private fun handleUpdate(command: String) {
        try {
            val mas = command.split(' ')
            Cities.updateElement(mas.last().toLong())
        } catch (e: Exception) {
            println("Error with id")
        }
    }

    private fun handleRemoveById(command: String) {
        try {
            val mas = command.split(' ')
            Cities.remove_by_id(mas.last().toLong())
        } catch (e: Exception) {
            println("Error with removed, try again")
        }
    }

    private fun handleRemoveAt(command: String) {
        try {
            val mas = command.split(' ')
            Cities.remove_at(mas.last().toInt())
            println("Remove element by index: ${mas.last()}")
        } catch (e: Exception) {
            println("Error with removed, try again")
        }
    }

    private fun handleFilterByStandard(command: String) {
        try {
            val mas = command.split(' ')
            val param = mas.last()
            StandardOfLiving.values().forEach { enumValues ->
                if (enumValues.toString().equals(param, ignoreCase = true)) {
                    val standard = enumValues
                    val filteredCities = Cities.filter_by_standard_of_living(standard)
                    if (filteredCities == null) {
                        print("No cities with this standard of living.")
                    } else {
                        val string = filteredCities.joinToString("\n") { it.toString() }
                        val result = PrintResult(String = string)
                        result.printResult()
                    }
                }
            }
        } catch (e: Exception) {
            println("Invalid standard")
        }
    }

    private fun handleFilterStartsWithName(command: String) {
        val mas = command.split(' ')
        val param = mas.last()
        val result = Cities.filter_starts_with_name(param)
        if (result == null) {
            println("There is no city starting with that name.")
        } else {
            val string = result.joinToString("\n") { it.toString() }
            val printResult = PrintResult(String = string)
            printResult.printResult()
        }
    }

    private fun handleFilterGreaterThanClimate(command: String) {
        try {
            val mas = command.split(' ')
            val param = mas.last()
            Climate.values().forEach { enumValues ->
                if (enumValues.toString().equals(param, ignoreCase = true)) {
                    val climate = enumValues
                    val filteredCities = Cities.filter_greater_than_climate(climate)
                    if (filteredCities == null) {
                        print("No cities with this climate")
                    } else {
                        val string = filteredCities.joinToString("\n") { it.toString() }
                        val result = PrintResult(String = string)
                        result.printResult()
                    }
                }
            }
        } catch (e: Exception) {
            println("Error with filter")
        }
    }

    private fun handleExecuteScript(command: String) {
        val fileManager = FileManager()
        val mas = command.split(' ')
        val param = mas.last()
        fileManager.execute_script(param)
    }

    fun exit() {
        println("Exit")
        System.exit(0)
    }
}

private val handler = CommandHandler()

fun findCommand(command: String?) {
    handler.findCommand(command)
}

fun exit() {
    handler.exit()
}