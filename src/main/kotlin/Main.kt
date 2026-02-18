import History.addHistory

fun main(args: Array<String>) {

    while (true) {
        var command = readLine()?.trim()
        if (command == null || command == "") {
            continue
        }
        addHistory(command)
        findCommand(command)
    }
}
