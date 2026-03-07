import Cities.cities
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import command.findCommand
import java.io.IOException
import java.io.InputStreamReader

class FileManager {

    fun execute_script(scriptFile: String) {
        val originalIn = System.`in`
        val fileStream = FileInputStream(scriptFile)
        val reader = InputStreamReader(fileStream, Charsets.UTF_8)
        val bufferedReader = BufferedReader(reader)
        System.setIn(FileInputStream(scriptFile))
        try {
            var line: String? = ""
            while (line != null) {
                line = bufferedReader.readLine()
                findCommand(line)
            }
        } catch (e: Exception) {
            println("Script is ended")
        } finally {
            System.setIn(originalIn)
            try {
                bufferedReader?.close()
            } catch (e: IOException) {
                println("something went wrong")
            }
        }
    }

    fun save() {
        println("error")
    }
}