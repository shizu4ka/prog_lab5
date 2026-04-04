package server

import common.Response
import common.Serializer
import java.io.DataOutputStream
import java.io.IOException

/**
 * Module for sending responses to client over TCP
 */
class ResponseSender(private val output: DataOutputStream) {

    /**
     * Send response to client
     */
    fun sendResponse(response: Response) {
        try {
            val serialized = Serializer.serialize(response)
            val length = serialized.size

            output.writeInt(length)
            output.write(serialized)
            output.flush()
        } catch (e: IOException) {
            println("Error sending response: ${e.message}")
        }
    }
}