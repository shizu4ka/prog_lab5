package common

import java.io.*

/**
 * Protocol for binary communication between client and server
 * Format: [4 bytes - data length][data bytes]
 */
object Protocol {
    const val HEADER_SIZE = 4

    /**
     * Encode data with length header for transmission
     */
    fun encodeMessage(data: ByteArray): ByteArray {
        val buffer = ByteArrayOutputStream()
        val dos = DataOutputStream(buffer)
        dos.writeInt(data.size)
        dos.write(data)
        dos.flush()
        return buffer.toByteArray()
    }

    /**
     * Read message length from input stream
     */
    fun readMessageLength(input: DataInputStream): Int {
        return input.readInt()
    }

    /**
     * Read exact number of bytes from input stream
     */
    fun readBytes(input: DataInputStream, length: Int): ByteArray {
        val bytes = ByteArray(length)
        input.readFully(bytes)
        return bytes
    }
}