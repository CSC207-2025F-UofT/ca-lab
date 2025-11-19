package data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/pets.db";

    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        conn.setAutoCommit(true); // simpler for small projects
        createTableIfNotExists(conn);
        return conn;
    }

    private static void createTableIfNotExists(Connection conn) throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS pets (
                id TEXT PRIMARY KEY,
                name TEXT,
                type TEXT,
                breed TEXT,
                age TEXT,
                gender TEXT,
                size TEXT,
                contact TEXT
            );
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    private static void createApplicationTableIfNotExists(Connection conn) throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS applications (
                id TEXT PRIMARY KEY,
                application_id TEXT,
                pet_id TEXT,
                legal_name TEXT,
                reason TEXT,
                availability TEXT,
                occupation TEXT,
                age TEXT,
                email TEXT,
                phone TEXT,
                previous_experience TEXT
            );
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        }

        //pet might not be text, but an id from the pets table so we get the correct one
        //might want to find pet id using the pet entity we got from the form
    }
}

