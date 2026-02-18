import Collection.City
import History.addHistory
import java.util.Vector
import javax.lang.model.util.Elements

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
