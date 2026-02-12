import History.addHistory
import java.util.Vector

fun main(args: Array<String>) {
    while (true) {
        var command = readLine()?.trim()
        if (command == null) {
            command = " "
            continue
        }
        addHistory(command)
        findCommand(command)
    }
}
