package client

import common.Command
import java.util.Scanner

fun main() {
    val client = Client()

    if (!client.connect()) {
        println("Cannot connect to server")
        return
    }

    val scanner = Scanner(System.`in`)
    val commandBuilder = CommandBuilder(scanner)

    println("Connected to server. Enter commands (type 'help' for available commands):")

    while (true) {
        print("> ")
        val input = scanner.nextLine().trim()

        if (input.isEmpty()) continue

        val parts = input.split(" ", limit = 2)
        val command = parts[0].lowercase()
        val arg = if (parts.size > 1) parts[1] else ""

        try {
            val cmd = when (command) {
                "help" -> Command.Help()
                "info" -> Command.Info()
                "show" -> Command.Show()
                "add" -> commandBuilder.buildAddCommand()
                "update" -> {
                    val id = arg.toLongOrNull() ?: return println("Invalid ID")
                    commandBuilder.buildUpdateCommand(id)
                }
                "remove_by_id" -> Command.RemoveById(arg.toLongOrNull() ?: return println("Invalid ID"))
                "remove_at" -> Command.RemoveAt(arg.toIntOrNull() ?: return println("Invalid index"))
                "remove_last" -> Command.RemoveLast()
                "clear" -> Command.Clear()
                "exit" -> break
                else -> {
                    println("Unknown command: $command")
                    continue
                }
            }

            client.sendCommand(cmd)
            val response = client.receiveResponse()
            if (response != null) {
                println("Server: ${response.message}")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    client.disconnect()
}