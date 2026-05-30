package client

import common.Command
import common.Response
import common.Serializer
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket

/**
 * Client application that communicates with server
 * Handles server unavailability with retry logic
 */
class Client(private val host: String = "localhost", private val port: Int = 9999) {

    private var socket: Socket? = null
    private var retryCount = 0
    private val maxRetries = 5

    /**
     * Connect to server with retry logic
     */
    fun connect(): Boolean {
        return try {
            socket = Socket(host, port)
            println("Connected to server at $host:$port")
            retryCount = 0
            true
        } catch (e: IOException) {
            retryCount++
            if (retryCount < maxRetries) {
                println("Connection failed. Retrying... ($retryCount/$maxRetries)")
                Thread.sleep(2000)
                connect()
            } else {
                println("Failed to connect after $maxRetries attempts")
                false
            }
        }
    }

    /**
     * Send command to server
     */
    fun sendCommand(command: Command) {
        try {
            val output = socket?.getOutputStream() ?: return
            val serialized = Serializer.serialize(command)
            val length = serialized.size

            val dos = DataOutputStream(output)
            dos.writeInt(length)
            dos.write(serialized)
            dos.flush()
        } catch (e: IOException) {
            println("Error sending command: ${e.message}")
        }
    }

    /**
     * Receive response from server
     */
    fun receiveResponse(): Response? {
        return try {
            val input = socket?.getInputStream() ?: return null
            val dis = DataInputStream(input)

            val length = dis.readInt()
            val data = ByteArray(length)
            dis.readFully(data)

            Serializer.deserialize(data)
        } catch (e: IOException) {
            println("Error receiving response: ${e.message}")
            null
        }
    }

    /**
     * Close connection
     */
    fun disconnect() {
        socket?.close()
    }
}