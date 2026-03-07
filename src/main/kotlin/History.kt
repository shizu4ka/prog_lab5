import command.Command
import java.util.Vector
import kotlin.collections.removeFirst

object History : Command {
    var history = Vector<Any>()
    fun addHistory(command: String) {
        if (history.size < 8) {
            history.add(command)
        } else {
            history.removeFirst()
            history.add(command)
        }
    }

    var result = PrintResult(
        Vector = history
    )

    override fun execute(): PrintResult {
        return result
    }
}