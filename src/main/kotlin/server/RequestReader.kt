package server

import common.Command
import common.Serializer
import java.io.DataInputStream
import java.io.IOException

/**
 * Module for reading commands from client through TCP
 */
class RequestReader {

    /**
     * Read command from input stream
     */
    fun readRequest(input: DataInputStream): Command? {
        return try {
            // Read message length (first 4 bytes)
            val length = input.readInt()

            if (length <= 0) return null

            // Read message data
            val data = ByteArray(length)
            input.readFully(data)

            // Deserialize to Command object
            Serializer.deserialize<Command>(data)
        } catch (e: IOException) {
            println("Error reading request: ${e.message}")
            null
        }
    }
}