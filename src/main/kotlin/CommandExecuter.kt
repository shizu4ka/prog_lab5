import History.execute


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
        var result = AddElement()
        try {
            var obj = result.addObject()
            Cities.cities.add(obj)
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
    }
}

fun exit() {
    println("Exit")
    System.exit(0)
}

