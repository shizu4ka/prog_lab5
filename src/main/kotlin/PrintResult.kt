import java.util.Vector

class PrintResult(
    var String: String? = null,
    var Vector: Vector<Any>? = null,
    var Int: Int? = null,
    var Boolean: Boolean? = null
) {
    fun printResult() {
        if (String != null) println(String)
        if (Vector != null) println(Vector)
        if (Int != null) println(Int)
        if (Boolean != null) println(Boolean)
    }
}