import History.execute


fun findCommand(command: String?) {
    if (command.equals("exit")) {
        exit()
    } else if (command.equals("help")) {
        val helpCommand = HelpCommand()
        helpCommand.result.print()
    } else if (command.equals("history")) {
        var result = execute()
        result.print()
    } else if (command.equals("add")) {
        var result = AddElement()
        var obj = result.addObject()
        Cities.cities.add(obj)
    } else if (command.equals("info")) {
        Cities.getInfo()
    }
}

fun exit() {
    println("Exit")
    System.exit(0)
}

