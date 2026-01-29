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
//        deleteAllApplications(); // UNCOMMENT this line if you want to delete all applications
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
                System.out.println("app_id: " + rs.getString("id")+
                        "pet_id: " + rs.getString("pet_id")+
                        "first_name: " + rs.getString("first_name")+
                        "last_name: " + rs.getString("last_name")+
                        "email: " + rs.getString("email")+
                        "phone: " + rs.getString("phone")+
                        "age: " + rs.getString("age")+
                        "occupation: " + rs.getString("occupation")+
                        "reason: " + rs.getString("reason")+
                        "environment: " + rs.getString("environment")+
                        "availbaility: " + rs.getString("availability")+
                        "prev_exp:" + rs.getString("previous_experience"));
            }

            System.out.println("📥 Loaded " + applicationMap.size() + " applications into memory map.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Application> fetchAllApplications() {
        return new ArrayList<>(applicationMap.values());
    }

    @Override
    public Application fetchApplicationById(String application_id) { return applicationMap.get(application_id); }

    public void addApplication(Application application) {
        String sql = "INSERT INTO applications (id, pet_id, first_name, last_name, email, phone, age, " +
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
            stmt.setString(11, application.getAvailability());        // FIXED
            stmt.setString(12, application.getPreviousExperience());   // FIXED
            stmt.executeUpdate();

            // 🔥 save to memory list too
            applicationMap.put(application.getApplicationId(), application);
            System.out.println("app_id = " + application.getApplicationId());
            System.out.println("pet_id = " + application.getPetId());
            System.out.println("first_name = " + application.getFirstName());
            System.out.println("last_name = " + application.getLastName());
            System.out.println("email = " + application.getEmail());
            System.out.println("phone_number = " + application.getPhoneNumber());
            System.out.println("age = " + application.getAge());
            System.out.println("occupation = " + application.getOccupation());
            System.out.println("reason = " + application.getReason());
            System.out.println("home_environment = " + application.getHomeEnvironment());
            System.out.println("availability = " + application.getAvailability());
            System.out.println("previous_experience = " + application.getPreviousExperience());

            System.out.println("✅ Application saved: " + application.getApplicationId());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Calling loadAllApplicationsIntoMap() again
        // because adding application as a visitor, then switching to admin account
        // does not run this function again
        // which means as an admin, you can't see the application you just added
        // as a visitor

        loadAllApplicationsIntoMap();
    }


    // Update status of application (for admin):
    public void updateApplicationStatus (Application application, String status) {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, application.getApplicationId());
            stmt.executeUpdate(); // Update data in database

            // Update in-memory map to sync
            application.setStatus(status);
            applicationMap.put(application.getApplicationId(), application);

            System.out.println("✅ Updated status for " + application.getApplicationId()
                    + " to " + status);

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

    public void deleteAllApplications() { // FOR INTERNAL USE
        String sql = "DELETE FROM applications";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();

            // clear the map since everything is deleted
            applicationMap.clear();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}





