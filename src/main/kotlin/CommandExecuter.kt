import History
import History.execute
import java.util.Vector

fun findCommand(command: String?) {
    if (command.equals("exit")) {
        exit()
    } else if (command.equals("help")) {
        val helpCommand = HelpCommand()
        helpCommand.execute()
    } else if (command.equals("history")) {
        History.execute()
    }
}

fun exit() {
    println("Exit")
    System.exit(0)
}

