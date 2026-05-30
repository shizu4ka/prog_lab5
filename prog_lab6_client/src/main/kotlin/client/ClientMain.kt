package client

import java.util.Scanner

/**
 * Main client function for interactive mode
 */
fun main() {
    val scanner = Scanner(System.`in`)
    
    print("Enter server host (default: localhost): ")
    val host = scanner.nextLine().trim().ifEmpty { "localhost" }
    
    print("Enter server port (default: 9999): ")
    val port = scanner.nextLine().trim().toIntOrNull() ?: 9999
    
    val client = Client(host, port)

    if (!client.connect()) {
        println("Failed to connect to server")
        return
    }

    print("Enter username: ")
    val username = scanner.nextLine().trim()
    
    print("Enter password: ")
    val password = scanner.nextLine().trim()

    if (!client.authenticate(username, password)) {
        println("Authentication failed")
        client.disconnect()
        return
    }

    println("Connected to server. Enter commands (type 'help' for available commands):")

    while (true) {
        print("> ")
        val input = scanner.nextLine().trim()

        if (input.isEmpty()) continue

        val parts = input.split("\\s+".toRegex())
        val command = parts[0].uppercase()

        when (command) {
            "HELP" -> {
                client.sendCommand("HELP")
                val response = client.receiveResponse()
                println(response ?: "No response")
            }
            "INFO" -> {
                client.sendCommand("INFO")
                val response = client.receiveResponse()
                println(response ?: "No response")
            }
            "SHOW" -> {
                client.sendCommand("SHOW")
                val response = client.receiveResponse()
                println(response ?: "No response")
            }
            "ADD" -> {
                println("Enter city name:")
                val name = scanner.nextLine().trim()
                println("Enter X coordinate:")
                val x = scanner.nextLine().trim().toDoubleOrNull() ?: 0.0
                println("Enter Y coordinate:")
                val y = scanner.nextLine().trim().toFloatOrNull() ?: 0f
                println("Enter area:")
                val area = scanner.nextLine().trim().toDoubleOrNull() ?: 0.0
                println("Enter population:")
                val population = scanner.nextLine().trim().toLongOrNull() ?: 0L

                client.sendCommandWithParams("ADD", name, x, y, area, population)
                val response = client.receiveResponse()
                println(response ?: "No response")
            }
            "UPDATE" -> {
                if (parts.size < 2) {
                    println("Usage: update <id>")
                    continue
                }
                val id = parts[1].toLongOrNull()
                if (id == null) {
                    println("Invalid ID")
                    continue
                }

                println("Enter new city name:")
                val name = scanner.nextLine().trim()
                println("Enter new X coordinate:")
                val x = scanner.nextLine().trim().toDoubleOrNull() ?: 0.0
                println("Enter new Y coordinate:")
                val y = scanner.nextLine().trim().toFloatOrNull() ?: 0f
                println("Enter new area:")
                val area = scanner.nextLine().trim().toDoubleOrNull() ?: 0.0
                println("Enter new population:")
                val population = scanner.nextLine().trim().toLongOrNull() ?: 0L

                client.sendCommandWithParams("UPDATE", id, name, x, y, area, population)
                val response = client.receiveResponse()
                println(response ?: "No response")
            }
            "REMOVE_BY_ID" -> {
                if (parts.size < 2) {
                    println("Usage: remove_by_id <id>")
                    continue
                }
                val id = parts[1].toLongOrNull()
                if (id == null) {
                    println("Invalid ID")
                    continue
                }

                client.sendCommandWithParams("REMOVE_BY_ID", id)
                val response = client.receiveResponse()
                println(response ?: "No response")
            }
            "CLEAR" -> {
                client.sendCommand("CLEAR")
                val response = client.receiveResponse()
                println(response ?: "No response")
            }
            "EXIT" -> {
                client.sendCommand("EXIT")
                val response = client.receiveResponse()
                println(response ?: "No response")
                println("Goodbye!")
                client.disconnect()
                return
            }
            else -> {
                println("Unknown command. Type 'help' for available commands.")
            }
        }
    }
}
