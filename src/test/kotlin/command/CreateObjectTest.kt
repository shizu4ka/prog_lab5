package command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import java.math.BigInteger
import java.util.Scanner

class CreateObjectTest {
//.\gradlew clean test для запуска
    @Test
    fun `test successful city creation with retry on invalid input`() {
        // Имитируем ввод:
        // 1. Ошибка в имени (пусто), потом нормальное имя "Omsk"
        // 2. Ошибка в X ("abc"), потом "10"
        // 3. Y = "20.5"
        // 4. Площадь = "500"
        // 5. Население = "1500000"
        // 6. Метры = "" (null)
        // 7. Дата = "2023-10-10"
        // 8. Климат = "HUMIDCONTINENTAL"
        // 9. Стандарт = "ULTRA_HIGH"
        // 10. Рост губернатора = "180.5"
        val input = """
            
            Omsk
            abc
            10
            20.5
            500
            1500000
            
            2023-10-10
            HUMIDCONTINENTAL
            ULTRA_HIGH
            180.5
        """.trimIndent()

        val sc = Scanner(input)
        val creator = CreateObject(sc, nextId = 777L)

        val city = creator.createObject()

        // Проверяем, что объект создался корректно, пропустив ошибки
        Assertions.assertEquals(777L, city.id)
        Assertions.assertEquals("Omsk", city.name)
        Assertions.assertEquals(10L, city.coordinates.x)
        Assertions.assertEquals(500.0, city.area)
        Assertions.assertEquals(BigInteger.valueOf(1500000), city.population)
        assertNotNull(city.creationDate)
    }
}