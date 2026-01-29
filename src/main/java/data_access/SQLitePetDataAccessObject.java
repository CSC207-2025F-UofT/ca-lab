package data_access;

import entity.Pet;
import use_case.add_pet.AddPetDataAccessInterface;
import use_case.delete_pet.DeletePetDataAccessInterface;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLitePetDataAccessObject implements AddPetDataAccessInterface, DeletePetDataAccessInterface {

    @Override
    public boolean existsPet(String id) {
        String sql = "SELECT id FROM pets WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error checking pet existence", e);
        }
    }

    @Override
    public void add(Pet pet) {
        String sql = """
            INSERT INTO pets(id, name, type, breed, age, gender, size, contact)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pet.getId());
            stmt.setString(2, pet.getName());
            stmt.setString(3, pet.getType());
            stmt.setString(4, pet.getBreed());
            stmt.setString(5, pet.getAge());
            stmt.setString(6, pet.getGender());
            stmt.setString(7, pet.getSize());
            stmt.setString(8, pet.getContact());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving pet", e);
        }
    }

    @Override
    public void deletePet(String id) {
        String sql = "DELETE FROM pets WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting pet", e);
        }
    }

    @Override
    public Pet getPet(String id) {
        String sql = "SELECT * FROM pets WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                return null;

            return new Pet(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("breed"),
                    rs.getString("age"),
                    rs.getString("gender"),
                    rs.getString("size"),
                    rs.getString("contact")
            );

        } catch (SQLException e) {
            throw new RuntimeException("Error loading pet", e);
        }
    }

    public List<Pet> getAllPets() {
        String sql = "SELECT * FROM pets";
        List<Pet> pets = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pets.add(new Pet(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("breed"),
                        rs.getString("age"),
                        rs.getString("gender"),
                        rs.getString("size"),
                        rs.getString("contact")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error reading pets", e);
        }

        return pets;
    }

    @Override
    public boolean existsPet(List<String> petData) {
        if (petData == null || petData.size() < 6) {
            return false;
        }

        String sql = """
        SELECT COUNT(*) FROM pets
        WHERE name = ? AND gender = ? AND age = ?
          AND type = ? AND size = ? AND breed = ?
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, petData.get(0)); // name
            stmt.setString(2, petData.get(1)); // gender
            stmt.setString(3, petData.get(2)); // age
            stmt.setString(4, petData.get(3)); // type
            stmt.setString(5, petData.get(4)); // size
            stmt.setString(6, petData.get(5)); // breed

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error checking pet existence", e);
        }
    }

    @Override
    public String generateId(String type) {
        String prefix = type.trim().toUpperCase().replaceAll("\\s+", "");

        String sql = "SELECT COUNT(*) FROM pets WHERE UPPER(type) = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type.trim().toUpperCase());

            ResultSet rs = ps.executeQuery();
            rs.next();
            long count = rs.getLong(1);

            return prefix + "-" + String.format("%03d", count + 1);

        } catch (SQLException e) {
            // Return fallback or wrap inside RuntimeException
            throw new RuntimeException("Failed to generate ID", e);
        }
    }
}

