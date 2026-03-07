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
            val wrapper = CityWrapper(cities)
            val xml = xmlMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(wrapper)
            File("data.xml").writeText(xml)
        }
    }
