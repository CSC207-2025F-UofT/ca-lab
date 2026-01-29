package entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void testConstructorWithId() {
        // Arrange
        String appId = "app123";
        Application app = new Application(
                appId,
                "John",
                "Doe",
                "jd@mail.com",
                "123456",
                "30",
                "Engineer",
                "pet123",
                "I love pets",
                "Big house",
                "Full time",
                "None"
        );

        // Act & Assert
        assertEquals(appId, app.getApplicationId());
        assertEquals("John", app.getFirstName());
        assertEquals("Doe", app.getLastName());
        assertEquals("jd@mail.com", app.getEmail());
        assertEquals("123456", app.getPhoneNumber());
        assertEquals("30", app.getAge());
        assertEquals("Engineer", app.getOccupation());
        assertEquals("pet123", app.getPetId());
        assertEquals("I love pets", app.getReason());
        assertEquals("Big house", app.getHomeEnvironment());
        assertEquals("Full time", app.getAvailability());
        assertEquals("None", app.getPreviousExperience());
        assertEquals("processing", app.getStatus());
    }

    @Test
    void testConstructorWithoutId() {
        // Arrange
        Application app = new Application(
                "pet123",
                "John",
                "Doe",
                "jd@mail.com",
                "123456",
                "30",
                "Engineer",
                "I love pets",
                "Big house",
                "Full time",
                "None"
        );

        // Act & Assert
        assertNotNull(app.getApplicationId()); // UUID generated
        assertDoesNotThrow(() -> UUID.fromString(app.getApplicationId())); // valid UUID

        assertEquals("John", app.getFirstName());
        assertEquals("Doe", app.getLastName());
        assertEquals("jd@mail.com", app.getEmail());
        assertEquals("123456", app.getPhoneNumber());
        assertEquals("30", app.getAge());
        assertEquals("Engineer", app.getOccupation());
        assertEquals("pet123", app.getPetId());
        assertEquals("I love pets", app.getReason());
        assertEquals("Big house", app.getHomeEnvironment());
        assertEquals("Full time", app.getAvailability());
        assertEquals("None", app.getPreviousExperience());
        assertEquals("processing", app.getStatus());
    }

    @Test
    void testSetStatus() {
        // Arrange
        Application app = new Application(
                "pet123",
                "John",
                "Doe",
                "jd@mail.com",
                "123456",
                "30",
                "Engineer",
                "I love pets",
                "Big house",
                "Full time",
                "None"
        );

        // Act
        String newStatus = app.setStatus("approved");

        // Assert
        assertEquals("approved", newStatus);
        assertEquals("approved", app.getStatus());
    }
}
