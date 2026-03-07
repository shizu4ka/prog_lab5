package command

import Collections.Climate
import Collections.StandardOfLiving
import FileManager
import PrintResult

fun findCommand(command: String?) {
    if (command.equals("exit")) {
        exit()
    } else if (command.equals("help")) {
        val helpCommand = HelpCommand()
        helpCommand.result.printResult()
    } else if (command.equals("history")) {
        var result = History.execute()
        result.printResult()
    } else if (command.equals("add")) {
        var result = CreateObject()
        try {
            var obj = result.createObject()
            Cities.addCity(obj)
            println("New city is created")
        } catch (e: Exception) {
            println("Error with add element")
        }
    } else if (command.equals("info")) {
        var result: PrintResult = Cities.getInfo()
        result.printResult()
    } else if (command.equals("show")) {
        var result = Cities.getCollection()
        result.printResult()
    } else if (command.equals("clear")) {
        try {
            Cities.clearCollection()
            println("Collection cleared")
        } catch (e: Exception) {
            println("Error with Clearing Cities")
        }
    } else if (command!!.matches("^update \\d+$".toRegex())) {
        try {
            var mas = command.split(' ')
            Cities.updateElement(mas.last().toLong())
        } catch (e: Exception) {
            println("Error with id")
        }
    } else if (command!!.matches("^remove_by_id \\d+$".toRegex())) {
        try {
            var mas = command.split(' ')
            Cities.remove_by_id(mas.last().toLong())
        } catch (e: Exception) {
            println("Error with removed, try again")
        }
    } else if (command!!.matches("^remove_at \\d+$".toRegex())) {
        try {
            var mas = command.split(' ')
            Cities.remove_at(mas.last().toInt())
            println("Remove element by index: ${mas.last()}")
        } catch (e: Exception) {
            println("Error with removed, try again")
        }
    } else if (command.equals("remove_last")) {
        try {
            Cities.removeLastElement()
            println("Remove element by last")
        } catch (e: Exception) {
            println("Error with remove element")
        }
    } else if (command.equals("add_if_max")) {
        Cities.add_if_max()
    } else if (command!!.matches("^filter_by_standard_of_living \\w+$".toRegex())) {
        try {
            var mas = command.split(' ')
            var param = mas.last()
            StandardOfLiving.values().forEach { enumValues ->
                if (enumValues.toString().equals(param)) {
                    var standard = enumValues
                    var filtered_cities = Cities.filter_by_standard_of_living(standard)
                    if (filtered_cities == null) print("No cities with this standard of living.")
                    else {
                        var string = ""
                        for (c in filtered_cities) {
                            string += c.toString() + "\n"
                        }
                        var result = PrintResult(String = string)
                        result.printResult()
                    }
                }
            }
        } catch (e: Exception) {
            println("Invalid standart")
        }
    } else if (command!!.matches("^filter_starts_with_name \\w+$".toRegex())) {
        var mas = command.split(' ')
        var param = mas.last()
        var result = Cities.filter_starts_with_name(param)
        if (result == null) println("There is no city starting with that name.")
        else {
            var string = ""
            for (c in result) {
                string += c.toString() + "\n"
            }
            var result = PrintResult(String = string)
            result.printResult()
        }
    } else if (command!!.matches("^filter_greater_than_climate \\w+$".toRegex())) {
        try {
            var mas = command.split(' ')
            var param = mas.last()
            Climate.values().forEach { enumValues ->
                if (enumValues.toString().equals(param)) {
                    var climate = enumValues
                    var filtered_cities = Cities.filter_greater_than_climate(climate)
                    if (filtered_cities == null) print("No cities with this climate")
                    else {
                        var string = ""
                        for (c in filtered_cities) {
                            string += c.toString() + "\n"
                        }
                        var result = PrintResult(String = string)
                        result.printResult()
                    }
                }
            }
        } catch (e: Exception) {
            println("Error with filter")
        }
    } else if (command!!.matches("^execute_script \\w+$".toRegex())) {
        var FileManager = FileManager()
        var mas = command.split(' ')
        var param = mas.last()
        FileManager.execute_script(param)
    }
    else if (command.equals("save")){
        var FileManager = FileManager()
        FileManager.save()
    }
}
fun exit() {
    println("Exit")
    System.exit(0)
}