package server.auth

import server.db.DatabaseManager

object UserManager {
    fun register(username: String, password: String): Boolean {
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(
                    "INSERT INTO users (username, password_hash) VALUES (?, ?)"
                ).use { stmt ->
                    stmt.setString(1, username)
                    stmt.setString(2, PasswordManager.hashPassword(password))
                    stmt.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            println("Registration error: ${e.message}")
            false
        }
    }

    fun authenticate(username: String, password: String): Int? {
        return try {
            DatabaseManager.getConnection().use { conn ->
                conn.prepareStatement(
                    "SELECT id, password_hash FROM users WHERE username = ?"
                ).use { stmt ->
                    stmt.setString(1, username)
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        val storedHash = rs.getString("password_hash")
                        if (PasswordManager.verifyPassword(password, storedHash)) {
                            rs.getInt("id")
                        } else null
                    } else null
                }
            }
        } catch (e: Exception) {
            println("Authentication error: ${e.message}")
            null
        }
    }
}