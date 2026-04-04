package common

import java.io.*

/**
 * Utility object for binary serialization and deserialization of objects
 */
object Serializer {
    /**
     * Serialize an object to byte array
     */
    fun <T : Serializable> serialize(obj: T): ByteArray {
        val buffer = ByteArrayOutputStream()
        val oos = ObjectOutputStream(buffer)
        oos.writeObject(obj)
        oos.close()
        return buffer.toByteArray()
    }

    /**
     * Deserialize byte array to object
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable> deserialize(bytes: ByteArray): T {
        val buffer = ByteArrayInputStream(bytes)
        val ois = ObjectInputStream(buffer)
        val obj = ois.readObject()
        ois.close()
        return obj as T
    }
}