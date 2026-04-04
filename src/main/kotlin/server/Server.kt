package server

import common.Command
import common.Response
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.ServerSocket

/**
 * Main Server class
 * Manages collection of cities and handles client requests
 * Works in single-threaded mode with blocking I/O
 */
class Server(private val port: Int = 9999) {

    private val collectionManager = CollectionManager()
    private val commandProcessor = CommandProcessor()

    /**
     * Start server
     */
    fun start() {
        try {
            val serverSocket = ServerSocket(port)
            println("Server listening on port $port")

            while (true) {
                val clientSocket = serverSocket.accept()
                println("Client connected from ${clientSocket.inetAddress}")

                try {
                    val input = clientSocket.getInputStream()
                    val output = clientSocket.getOutputStream()

                    val dis = DataInputStream(input)
                    val dos = DataOutputStream(output)

                    val requestReader = RequestReader()
                    val responseSender = ResponseSender(dos)

                    while (true) {
                        val command = requestReader.readRequest(dis) ?: break
                        val response = commandProcessor.processCommand(command, collectionManager)
                        responseSender.sendResponse(response)
                    }
                } finally {
                    clientSocket.close()
                }
            }
        } catch (e: IOException) {
            println("Server error: ${e.message}")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val server = Server()
            server.start()
        }
    }
}