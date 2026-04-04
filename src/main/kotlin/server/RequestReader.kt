package server

import common.Command
import common.Protocol
import common.Serializer
import java.io.DataInputStream

/**
 * Module for reading requests from client
 * Reads binary serialized Command objects
 */
class RequestReader {
    /**
     * Read command from input stream
     */
    fun readRequest(input: DataInputStream): Command? {
        return try {
            val length = Protocol.readMessageLength(input)
            if (length <= 0) return null
            
            val data = Protocol.readBytes(input, length)
            Serializer.deserialize<Command>(data)
        } catch (e: Exception) {
            println("Error reading request: ${'\'}${'{'}.e.message${'\'}${'}'}")
            null
        }
    }
}