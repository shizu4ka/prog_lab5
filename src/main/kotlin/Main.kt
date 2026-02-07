import java.util.Vector

fun main(args: Array<String>) {

    while (true) {
        var command = readLine()?.trim()
        if (command == null) {
            command = " "
            continue
        }
        if (history.size < 8) {
            history.add(command)
        } else {
            history.removeFirst()
            history.add(command)
        }
        findCommand(command)
    }
}
