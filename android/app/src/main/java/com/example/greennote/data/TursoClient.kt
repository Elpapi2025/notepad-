package com.example.greennote.data

import android.content.Context
import tech.turso.libsql.Libsql
import tech.turso.libsql.SQLiteConnection
import tech.turso.libsql.SQLiteDatabase

object TursoClient {

    private var db: SQLiteDatabase? = null
    private var connection: SQLiteConnection? = null

    // TODO: Securely manage and provide the Turso database URL and authentication token.
    // For now, these are placeholders.
    private const val TURSO_DATABASE_URL = "libsql://<your-database-name>-<your-organization>.turso.io"
    private const val TURSO_AUTH_TOKEN = "your-auth-token"

    suspend fun getDb(context: Context): SQLiteDatabase {
        if (db == null) {
            db = Libsql.open(
                url = TURSO_DATABASE_URL,
                authToken = TURSO_AUTH_TOKEN
            )
            // Create the Note table if it doesn't exist
            db!!.connect().prepareStatement(
                """
                CREATE TABLE IF NOT EXISTS Note (
                    id TEXT PRIMARY KEY NOT NULL,
                    title TEXT NOT NULL,
                    content TEXT NOT NULL,
                    createdAt TEXT NOT NULL,
                    color TEXT
                );
                """.trimIndent()
            ).execute()
        }
        return db!!
    }

    suspend fun getConnection(context: Context): SQLiteConnection {
        if (connection == null || connection?.isClosed == true) {
            connection = getDb(context).connect()
        }
        return connection!!
    }

    // You might want a way to close connections, though LibSQL manages pooling internally
    // suspend fun closeConnection() {
    //    connection?.close()
    //    connection = null
    // }
}