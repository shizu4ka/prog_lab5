import java.util.Vector
import kotlin.collections.removeFirst

object History : Command {
    var history = Vector<String>()
    fun addHistory(command: String) {
        if (history.size < 8) {
            history.add(command)
        } else {
            history.removeFirst()
            history.add(command)
        }
    }

    override fun execute() {
        println(history)
    }
}