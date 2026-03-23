import Cities.cities
import Collections.City
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import command.findCommand
import java.beans.XMLEncoder
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.Scanner

class FileManager {
    @JacksonXmlRootElement(localName = "cities")
    data class CityWrapper(
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "city")
        val cities: List<City>
    )

    private val xmlMapper: XmlMapper = XmlMapper.builder()
        .addModule(JavaTimeModule())
        .build()

    fun execute_script(scriptFile: String) {
        val file = java.io.File(scriptFile)
        if (!file.exists()) {
            println("Script file not found")
            return
        }

        val scriptScanner = Scanner(file)
        println("Running script: $scriptFile")

        try {
            while (scriptScanner.hasNextLine()) {
                val line = scriptScanner.nextLine().trim()
                if (line.isEmpty()) continue

                findCommand(line, scriptScanner)
            }
        } catch (e: Exception) {
            println("Error during script execution: ${e.message}")
        } finally {
            scriptScanner.close()
            println("Script execution finished")
        }
    }

    fun save() {
            val wrapper = CityWrapper(cities)
            val xml = xmlMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(wrapper)
            File("data.xml").writeText(xml)
        }
    }
