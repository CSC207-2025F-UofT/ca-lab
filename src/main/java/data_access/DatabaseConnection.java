package data_access;

import entity.Visitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/pets.db";

    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        conn.setAutoCommit(true); // simpler for small projects
        createPetTableIfNotExists(conn);
        createApplicationTableIfNotExists(conn);
        return conn;
    }

    private static void createPetTableIfNotExists(Connection conn) throws SQLException {
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
        // NOTE: id = application id
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS applications (
                id TEXT PRIMARY KEY,
                pet_id TEXT,
                first_name TEXT,
                last_name TEXT,
                email TEXT,
                phone TEXT,
                age TEXT,
                occupation TEXT,
                reason TEXT,
                environment TEXT,
                availability TEXT,
                previous_experience TEXT
            );
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        }

        //pet might not be text, but an id from the pets table so we get the correct one
        //might want to find pet id using the pet entity we got from the form

        // use unique application id to find the correct application
    }
}

