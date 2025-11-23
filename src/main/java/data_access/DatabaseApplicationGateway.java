package data_access;

import entity.Application;
import java.sql.*;
import java.util.*;

public class DatabaseApplicationGateway implements ApplicationGatewayInterface {

    private final Connection conn;

    private final Map<String, Application> applicationMap = new HashMap<>();

    public DatabaseApplicationGateway(Connection conn) {
        this.conn = conn;
        loadAllApplicationsIntoMap();
    }


    private void loadAllApplicationsIntoMap() {
        String sql = "SELECT * FROM applications";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Application application = new Application(
                        rs.getString("id"),
                        rs.getString("pet_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("age"),
                        rs.getString("occupation"),
                        rs.getString("reason"),
                        rs.getString("environment"),
                        rs.getString("availability"),
                        rs.getString("previous_experience")
                );
                applicationMap.put(application.getApplicationId(), application);
            }

            System.out.println("📥 Loaded " + applicationMap.size() + " applications into memory map.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Application fetchApplicationById(String application_id) { return applicationMap.get(application_id); }

    public void addApplication(Application application) {
        String sql = "INSERT OR REPLACE INTO applications (id, pet_id, first_name, last_name, email, phone, age, " +
                "occupation, reason, environment, availability, previous_experience) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, application.getApplicationId());
            stmt.setString(2, application.getPetId());
            stmt.setString(3, application.getFirstName());
            stmt.setString(4, application.getLastName());
            stmt.setString(5, application.getEmail());
            stmt.setString(6, application.getPhoneNumber());
            stmt.setString(7, application.getAge());
            stmt.setString(8, application.getOccupation());
            stmt.setString(9, application.getReason());
            stmt.setString(10, application.getHomeEnvironment());
            stmt.setString(10, application.getAvailability());
            stmt.setString(11, application.getPreviousExperience());
            stmt.executeUpdate();
            System.out.println("✅ Added application to DB: " + application.getFirstName() + " "
                    + application.getLastName() + "'s application for"
                    + application.getPetId() + "ID: " + application.getApplicationId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // won't actually use this in the program, but for internal use
    public void deleteApplication(String id) {
        String sql = "DELETE FROM applications WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();

            // remove from map
            applicationMap.remove(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


