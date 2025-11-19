package data_access;

import entity.Application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseApplicationGateway implements ApplicationGatewayInterface {
    private final Connection conn;

    public DatabaseApplicationGateway(Connection conn) {
        this.conn = conn;
    }

//    @Override
//    public Application fetchApplicationById(String applicationId) { // why does applicationID have to be a string not int?
//        String sql = "SELECT * FROM pets WHERE id = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // here
//            stmt.setString(1, applicationId);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return new Application(
////                        rs.getString("id"),
////                        rs.getString("application_id"), //TODO: make Application entity, pass in personal_info list, not separate fields!
//                        //List<String> personal_information = getPersonalInformation(user); ..... etc.
////                        rs.getString("pet_id"),
////                        rs.getString("legal_name"),
////                        rs.getString("reason"),
//                        rs.getString("availability"),
//                        rs.getString("occupation"),
//                        rs.getString("age"),
//                        rs.getString("email"),
//                        rs.getString("phone"),
//                        rs.getString("previous_experience")
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public void addApplication(Application application) {
        String sql = "INSERT OR REPLACE INTO applications (id, pet_id, legal_name, reason, availability, " +
                "occupation, age, email, phone, previous_experience) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, application.getApplicationId());
            stmt.setString(2, application.getPetId());
            stmt.setString(3, application.getUser().getFirstName());
            stmt.setString(4, application.getUser().getLastName());
            stmt.setString(5, application.getReason());
            stmt.setString(6, application.getAvailability());
            stmt.setString(7, application.getUser().getOccupation());
            stmt.setString(8, application.getUser().getAge());
            stmt.setString(9, application.getUser().getEmail());
            stmt.setString(10, application.getUser().getPhoneNum());
            stmt.setString(11, application.getPreviousExperience());
            stmt.executeUpdate();
            System.out.println("✅ Added application to DB: " + application.getUser().getLastName() + " " + application.getUser().getLastName() + "'s application for"
                    + application.getPetId() + "ID: " + application.getApplicationId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteApplication(String id) {
        String sql = "DELETE FROM applications WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


