package client

import common.Command
import java.util.Scanner

/**
 * Main client function for interactive mode
 */
fun main() {
    val client = Client("localhost", 9999)

    if (!client.connect()) {
        println("Failed to connect to server")
        return
    }

    val scanner = Scanner(System.`in`)
    val commandBuilder = CommandBuilder(scanner)

    println("Connected to server. Enter commands (type 'help' for available commands):")

    while (true) {
        print("> ")
        val input = scanner.nextLine().trim().split(" ")

        if (input.isEmpty()) continue

        val command = when (input[0].lowercase()) {
            "help" -> Command.Help()
            "info" -> Command.Info()
            "show" -> Command.Show()
            "add" -> commandBuilder.buildAddCommand()
            "update" -> {
                if (input.size < 2) {
                    println("Usage: update <id>")
                    continue
                }
                commandBuilder.buildUpdateCommand(input[1].toLongOrNull() ?: continue)
            }
            "remove_by_id" -> {
                if (input.size < 2) {
                    println("Usage: remove_by_id <id>")
                    continue
                }
                Command.RemoveById(input[1].toLongOrNull() ?: continue)
            }
            "remove_at" -> {
                if (input.size < 2) {
                    println("Usage: remove_at <index>")
                    continue
                }
                Command.RemoveAt(input[1].toIntOrNull() ?: continue)
            }
            "remove_last" -> Command.RemoveLast()
            "clear" -> Command.Clear()
            "add_if_max" -> commandBuilder.buildAddCommand().let { Command.AddIfMax((it as Command.Add).city) }
            "filter_by_standard" -> {
                if (input.size < 2) {
                    println("Usage: filter_by_standard <standard>")
                    continue
                }
                try {
                    Command.FilterByStandardOfLiving(common.StandardOfLiving.valueOf(input[1]))
                } catch (e: Exception) {
                    println("Invalid standard of living")
                    continue
                }
            }
            "filter_starts_with_name" -> {
                if (input.size < 2) {
                    println("Usage: filter_starts_with_name <prefix>")
                    continue
                }
                Command.FilterStartsWithName(input.drop(1).joinToString(" "))
            }
            "filter_greater_than_climate" -> {
                if (input.size < 2) {
                    println("Usage: filter_greater_than_climate <climate>")
                    continue
                }
                try {
                    Command.FilterGreaterThanClimate(common.Climate.valueOf(input[1]))
                } catch (e: Exception) {
                    println("Invalid climate")
                    continue
                }
            }
            "execute_script" -> {
                if (input.size < 2) {
                    println("Usage: execute_script <file_name>")
                    continue
                }
                Command.ExecuteScript(input.drop(1).joinToString(" "))
            }
            "exit" -> Command.Exit()
            "save" -> Command.Save()
            else -> {
                println("Unknown command. Type 'help' for available commands.")
                continue
            }
        }

        client.sendCommand(command)
        val response = client.receiveResponse()

        if (response != null) {
            println(response.message)
            if (response.data != null) {
                println("Data: ${response.data}")
            }
        }

        if (input[0].lowercase() == "exit") break
    }

    client.disconnect()
}