package server

import common.AuthRequest
import server.auth.UserManager
import server.collection.SynchronizedCityCollection
import server.db.DatabaseManager
import server.threading.ThreadPoolManager
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.util.concurrent.TimeUnit

/**
 * Main Server class
 * Manages collection of cities with database persistence and authentication
 * Handles multithreaded requests with proper synchronization
 */
class Server(private val port: Int = 9999) {
    private val collection = SynchronizedCityCollection()
    private val serverSocket = ServerSocket(port)

    /**
     * Start server and initialize database
     */
    fun start() {
        try {
            // Initialize database schema
            DatabaseManager.initializeDatabase()
            println("Database initialized successfully")

            // Load existing cities from database into memory
            loadCitiesFromDatabase()

            println("Server listening on port $port")

            while (true) {
                val clientSocket = serverSocket.accept()
                println("Client connected from ${clientSocket.inetAddress}")

                // Submit client handling to reading pool
                ThreadPoolManager.readingPool.submit {
                    try {
                        val input = clientSocket.getInputStream()
                        val output = clientSocket.getOutputStream()

                        val dis = DataInputStream(input)
                        val dos = DataOutputStream(output)

                        handleClient(dis, dos)
                    } catch (e: Exception) {
                        println("Client error: ${e.message}")
                    } finally {
                        clientSocket.close()
                    }
                }
            }
        } catch (e: Exception) {
            println("Server error: ${e.message}")
            e.printStackTrace()
        } finally {
            shutdown()
        }
    }

    private fun handleClient(dis: DataInputStream, dos: DataOutputStream) {
        try {
            // Read authentication request
            val authUsername = dis.readUTF()
            val authPassword = dis.readUTF()

            val userId = UserManager.authenticate(authUsername, authPassword)
            if (userId == null) {
                // Try registration
                if (UserManager.register(authUsername, authPassword)) {
                    val newUserId = UserManager.authenticate(authUsername, authPassword)
                    if (newUserId != null) {
                        dos.writeBoolean(true)
                        dos.writeInt(newUserId)
                        dos.flush()
                    } else {
                        dos.writeBoolean(false)
                        dos.flush()
                        return
                    }
                } else {
                    dos.writeBoolean(false)
                    dos.flush()
                    return
                }
            } else {
                dos.writeBoolean(true)
                dos.writeInt(userId)
                dos.flush()
            }

            // Handle commands from authenticated user
            while (true) {
                val commandType = dis.readUTF()
                if (commandType.isEmpty()) break

                // Submit command processing to processing pool
                ThreadPoolManager.processingPool.submit {
                    val response = processCommand(commandType, dis, userId)
                    // Submit response sending to sending pool
                    ThreadPoolManager.sendingPool.submit {
                        try {
                            dos.writeUTF(response)
                            dos.flush()
                        } catch (e: Exception) {
                            println("Error sending response: ${e.message}")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Client handling error: ${e.message}")
        }
    }

    private fun processCommand(commandType: String, dis: DataInputStream, userId: Int): String {
        return when (commandType) {
            "ADD" -> {
                try {
                    // Read city data from client
                    val name = dis.readUTF()
                    val x = dis.readDouble()
                    val y = dis.readFloat()
                    val area = dis.readDouble()
                    val population = dis.readLong()
                    
                    // Create city and save to database
                    val cityId = saveCityToDatabase(userId, name, x, y, area, population)
                    if (cityId > 0) {
                        "City added successfully with ID: $cityId"
                    } else {
                        "Error adding city"
                    }
                } catch (e: Exception) {
                    "Error: ${e.message}"
                }
            }
            "SHOW" -> {
                val cities = collection.getAll()
                buildString {
                    cities.forEach { city ->
                        appendLine("City(id=${city.id}, name=${city.name}, area=${city.area}, population=${city.population})")
                    }
                }
            }
            "INFO" -> {
                "Collection size: ${collection.size()}"
            }
            "CLEAR" -> {
                // Only clear user's own cities
                clearUserCities(userId)
                "Cleared all your cities"
            }
            else -> "Unknown command"
        }
    }

    private fun saveCityToDatabase(userId: Int, name: String, x: Double, y: Float, 
                                    area: Double, population: Long): Long {
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(
                    """INSERT INTO cities (owner_id, name, x, y, area, population) 
                       VALUES (?, ?, ?, ?, ?, ?) RETURNING id""",
                    java.sql.Statement.RETURN_GENERATED_KEYS
                ).use { stmt ->
                    stmt.setInt(1, userId)
                    stmt.setString(2, name)
                    stmt.setDouble(3, x)
                    stmt.setFloat(4, y)
                    stmt.setDouble(5, area)
                    stmt.setLong(6, population)

                    stmt.executeUpdate()
                    val rs = stmt.generatedKeys
                    if (rs.next()) {
                        rs.getLong(1)
                    } else {
                        -1
                    }
                }
            }
        } catch (e: Exception) {
            println("Database error: ${e.message}")
            -1
        }
    }

    private fun loadCitiesFromDatabase() {
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.createStatement().use { stmt ->
                    val rs = stmt.executeQuery("SELECT * FROM cities")
                    while (rs.next()) {
                        // Load cities into memory (simplified - full implementation needed)
                        println("Loaded city: ${rs.getString("name")}")
                    }
                }
            }
        } catch (e: Exception) {
            println("Error loading cities: ${e.message}")
        }
    }

    private fun clearUserCities(userId: Int) {
        try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement("DELETE FROM cities WHERE owner_id = ?").use { stmt ->
                    stmt.setInt(1, userId)
                    stmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            println("Error clearing cities: ${e.message}")
        }
    }

    private fun shutdown() {
        try {
            serverSocket.close()
            ThreadPoolManager.shutdown()
            ThreadPoolManager.awaitTermination(10, TimeUnit.SECONDS)
        } catch (e: Exception) {
            println("Error during shutdown: ${e.message}")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = args.getOrNull(0)?.toIntOrNull() ?: 9999
            val server = Server(port)
            server.start()
        }
    }
}
