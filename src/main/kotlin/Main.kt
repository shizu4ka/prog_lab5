import History.addHistory
import command.findCommand
import java.util.Scanner

fun main(args: Array<String>) {
    val sc = Scanner(System.`in`)

    while (true) {
        if (!sc.hasNextLine()) break
        val command = sc.nextLine().trim()

        if (command.isEmpty()) continue

        addHistory(command)
        findCommand(command, sc)
    }
}
