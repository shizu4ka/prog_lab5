package server.db

import java.sql.Connection
import java.sql.DriverManager
import java.util.*

object DatabaseManager {
    private const val DB_URL = "jdbc:postgresql://pg:5432/studs"
    private val props = Properties().apply {
        setProperty("user", System.getenv("PGUSER") ?: "studs")
        setProperty("password", System.getenv("PGPASSWORD") ?: "studs")
    }

    init {
        Class.forName("org.postgresql.Driver")
    }

    fun getConnection(): Connection {
        return DriverManager.getConnection(DB_URL, props)
    }

    fun initializeDatabase() {
        getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                // Create users table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id SERIAL PRIMARY KEY,
                        username VARCHAR(255) UNIQUE NOT NULL,
                        password_hash VARCHAR(64) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """)

                // Create sequence for city IDs
                stmt.execute("""
                    CREATE SEQUENCE IF NOT EXISTS city_id_seq START WITH 1
                """)

                // Create cities table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS cities (
                        id BIGINT DEFAULT nextval('city_id_seq') PRIMARY KEY,
                        owner_id INT NOT NULL REFERENCES users(id),
                        name VARCHAR(255) NOT NULL,
                        x DOUBLE PRECISION NOT NULL,
                        y FLOAT NOT NULL,
                        creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        area DOUBLE PRECISION NOT NULL CHECK (area > 0),
                        population BIGINT NOT NULL CHECK (population > 0),
                        meters_above_sea_level FLOAT,
                        establishment_date DATE,
                        climate VARCHAR(50),
                        standard_of_living VARCHAR(50),
                        governor_name VARCHAR(255),
                        governor_height INT,
                        governor_birth_date DATE
                    )
                """)
            }
        }
    }
}