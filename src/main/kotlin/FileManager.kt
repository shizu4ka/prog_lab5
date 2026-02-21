import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class FileManager {
    fun copyFile(sourcePath: String, destinationPath: String) {
        val sourceFile = File(sourcePath)
        val destFile = File(destinationPath)
        sourceFile.copyTo(destFile, overwrite = true)

    }

    fun removeFirstLine(filePath: String) {
        val file = File(filePath)
        file.writeText(file.readLines().drop(1).joinToString("\n"))
    }

    fun execute_script(scriptFile: String) {
        copyFile(scriptFile, "copyScript")
        val originalIn = System.`in`
        val fileStream = FileInputStream("copyScript")
        val reader = InputStreamReader(fileStream, Charsets.UTF_8)
        val bufferedReader = BufferedReader(reader)
        System.setIn(FileInputStream("copyScript"))
        try {
            var line: String? = ""
            while (line != null) {
                line = bufferedReader.readLine()
                removeFirstLine("copyScript")
                findCommand(line)
            }
        } catch (e: Exception) {
            println("Script is ended")
        } finally {
            System.setIn(originalIn)
        }
    }
}