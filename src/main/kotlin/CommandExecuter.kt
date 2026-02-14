import History
import History.execute
import java.util.Vector


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
        cities.cities.add(obj)
    }
}

fun exit() {
    println("Exit")
    System.exit(0)
}

