package client

import java.util.Scanner

/**
 * Validates user input for City creation
 */
object InputValidator {

    private val scanner = Scanner(System.`in`)

    /**
     * Validate and read long input
     */
    fun readLong(prompt: String, min: Long? = null): Long {
        while (true) {
            try {
                print(prompt)
                val value = scanner.nextLong()
                scanner.nextLine()
                if (min != null && value <= min) {
                    println("Value must be greater than $min")
                    continue
                }
                return value
            } catch (e: Exception) {
                scanner.nextLine()
                println("Invalid input. Please enter a number.")
            }
        }
    }

    /**
     * Validate and read double input
     */
    fun readDouble(prompt: String, min: Double? = null): Double {
        while (true) {
            try {
                print(prompt)
                val value = scanner.nextDouble()
                scanner.nextLine()
                if (min != null && value <= min) {
                    println("Value must be greater than $min")
                    continue
                }
                return value
            } catch (e: Exception) {
                scanner.nextLine()
                println("Invalid input. Please enter a number.")
            }
        }
    }

    /**
     * Validate and read float input
     */
    fun readFloat(prompt: String, max: Float? = null): Float {
        while (true) {
            try {
                print(prompt)
                val value = scanner.nextFloat()
                scanner.nextLine()
                if (max != null && value > max) {
                    println("Value must be less than or equal to $max")
                    continue
                }
                return value
            } catch (e: Exception) {
                scanner.nextLine()
                println("Invalid input. Please enter a number.")
            }
        }
    }

    /**
     * Validate and read string input
     */
    fun readString(prompt: String, allowEmpty: Boolean = false): String {
        while (true) {
            print(prompt)
            val value = scanner.nextLine().trim()
            if (value.isEmpty() && !allowEmpty) {
                println("Input cannot be empty")
                continue
            }
            return value
        }
    }
}