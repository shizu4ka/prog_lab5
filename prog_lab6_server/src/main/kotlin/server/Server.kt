package server

import common.*
import server.auth.UserManager
import server.collection.CityRepository
import server.collection.SynchronizedCityCollection
import server.db.DatabaseManager
import server.threading.ThreadPoolManager
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.util.concurrent.TimeUnit
import java.math.BigInteger
import java.time.LocalDateTime

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
                        try {
                            clientSocket.close()
                        } catch (e: Exception) {
                            // ignore
                        }
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

            var userId = UserManager.authenticate(authUsername, authPassword)
            if (userId == null) {
                // Try registration
                if (UserManager.register(authUsername, authPassword)) {
                    userId = UserManager.authenticate(authUsername, authPassword)
                    if (userId != null) {
                        dos.writeBoolean(true)
                        dos.writeInt(userId)
                        dos.writeUTF("Registered and authenticated successfully")
                        dos.flush()
                    } else {
                        dos.writeBoolean(false)
                        dos.writeUTF("Registration failed")
                        dos.flush()
                        return
                    }
                } else {
                    dos.writeBoolean(false)
                    dos.writeUTF("User already exists or registration failed")
                    dos.flush()
                    return
                }
            } else {
                dos.writeBoolean(true)
                dos.writeInt(userId)
                dos.writeUTF("Authenticated successfully")
                dos.flush()
            }

            println("User authenticated (ID: $userId)")

            // Handle commands from authenticated user
            while (true) {
                try {
                    val commandType = dis.readUTF()
                    if (commandType.isEmpty()) break

                    println("Processing command: $commandType for user $userId")

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
                } catch (e: Exception) {
                    if (!e.message?.contains("EOFException") == true) {
                        println("Error: ${e.message}")
                    }
                    break
                }
            }
        } catch (e: Exception) {
            println("Client handling error: ${e.message}")
        }
    }

    private fun processCommand(commandType: String, dis: DataInputStream, userId: Int): String {
        return when (commandType.uppercase()) {
            "ADD" -> handleAddCommand(dis, userId)
            "SHOW" -> handleShowCommand()
            "INFO" -> handleInfoCommand()
            "UPDATE" -> handleUpdateCommand(dis, userId)
            "REMOVE_BY_ID" -> handleRemoveByIdCommand(dis, userId)
            "CLEAR" -> handleClearCommand(userId)
            "HELP" -> handleHelpCommand()
            "EXIT" -> "Goodbye"
            else -> "Unknown command: $commandType"
        }
    }

    private fun handleAddCommand(dis: DataInputStream, userId: Int): String {
        return try {
            val name = dis.readUTF()
            val x = dis.readDouble()
            val y = dis.readFloat()
            val area = dis.readDouble()
            val population = dis.readLong()

            val city = City(
                id = 0L,
                owner = userId,
                name = name,
                coordinates = Coordinates(x, y),
                creationDate = LocalDateTime.now(),
                area = area,
                population = BigInteger.valueOf(population),
                metersAboveSeaLevel = null,
                establishmentDate = null,
                climate = null,
                standardOfLiving = null,
                governor = null
            )

            val cityId = CityRepository.saveCityToDatabase(city)
            if (cityId > 0) {
                val savedCity = CityRepository.getCityById(cityId)
                if (savedCity != null) {
                    collection.add(savedCity)
                    "City added successfully with ID: $cityId"
                } else {
                    "Error: City saved but could not be retrieved"
                }
            } else {
                "Error adding city to database"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun handleShowCommand(): String {
        val cities = collection.getAll()
        return if (cities.isEmpty()) {
            "Collection is empty"
        } else {
            buildString {
                cities.forEach { city ->
                    appendLine("ID: ${city.id}, Owner: ${city.owner}, Name: ${city.name}, Area: ${city.area}, Population: ${city.population}")
                }
            }
        }
    }

    private fun handleInfoCommand(): String {
        return """
            Collection Info:
            - Size: ${collection.size()}
            - Type: Synchronized City Collection
            - Storage: PostgreSQL Database
        """.trimIndent()
    }

    private fun handleUpdateCommand(dis: DataInputStream, userId: Int): String {
        return try {
            val id = dis.readLong()
            val city = collection.getById(id)

            if (city == null) {
                return "City with ID $id not found"
            }

            if (city.owner != userId) {
                return "You don't have permission to update this city"
            }

            val name = dis.readUTF()
            val x = dis.readDouble()
            val y = dis.readFloat()
            val area = dis.readDouble()
            val population = dis.readLong()

            city.apply {
                this.name = name
                this.coordinates = Coordinates(x, y)
                this.area = area
                this.population = BigInteger.valueOf(population)
            }

            if (CityRepository.updateCityInDatabase(city)) {
                collection.update(city)
                "City updated successfully"
            } else {
                "Error updating city in database"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun handleRemoveByIdCommand(dis: DataInputStream, userId: Int): String {
        return try {
            val id = dis.readLong()
            val city = collection.getById(id)

            if (city == null) {
                return "City with ID $id not found"
            }

            if (city.owner != userId) {
                return "You don't have permission to delete this city"
            }

            if (CityRepository.deleteCityFromDatabase(id, userId)) {
                collection.remove(id)
                "City deleted successfully"
            } else {
                "Error deleting city"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun handleClearCommand(userId: Int): String {
        return try {
            val deleted = CityRepository.deleteAllUserCities(userId)
            collection.removeUserCities(userId)
            "Deleted $deleted cities"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun handleHelpCommand(): String {
        return """
            Available commands:
            - ADD: Add a new city (requires: name, x, y, area, population)
            - SHOW: Display all cities in the collection
            - INFO: Show collection information
            - UPDATE <id>: Update city with given ID (requires: name, x, y, area, population)
            - REMOVE_BY_ID <id>: Remove city with given ID
            - CLEAR: Remove all your cities
            - HELP: Show this help message
            - EXIT: Disconnect from server
        """.trimIndent()
    }

    private fun loadCitiesFromDatabase() {
        try {
            val cities = CityRepository.loadAllCities()
            cities.forEach { (_, city) ->
                collection.add(city)
            }
            println("Loaded ${cities.size} cities from database")
        } catch (e: Exception) {
            println("Error loading cities: ${e.message}")
        }
    }

    private fun shutdown() {
        try {
            serverSocket.close()
            ThreadPoolManager.shutdown()
            ThreadPoolManager.awaitTermination(10, TimeUnit.SECONDS)
            println("Server shut down gracefully")
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
