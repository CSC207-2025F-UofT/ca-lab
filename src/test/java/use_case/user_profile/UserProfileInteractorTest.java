package use_case.user_profile;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.themes.ThemeManager;
import org.junit.jupiter.api.*;
import view.GameDashboard;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileInteractorTest {

    // Test implementation that extends FileUserDataAccessObject for the cast to
    // work
    static class TestUserDataAccessObject extends FileUserDataAccessObject {
        private static String createTempFile() throws IOException {
            File tempFile = File.createTempFile("test_users", ".csv");
            tempFile.deleteOnExit();
            return tempFile.getAbsolutePath();
        }

        public TestUserDataAccessObject(UserFactory userFactory) throws IOException {
            // Call parent constructor with temp file path - super() must be first
            super(createTempFile(), userFactory);
        }

        // The parent class already implements updateUserProfile and updateUsername
        // So we don't need to override them
    }

    // Mock GameDashboard - create with proper dependencies
    static class MockGameDashboard extends GameDashboard {
        private String currentUser;

        public MockGameDashboard() {
            super(new ViewManagerModel(), new ThemeManager());
        }

        @Override
        public String getCurrentUser() {
            return currentUser;
        }

        @Override
        public void setUser(String username) {
            this.currentUser = username;
        }

        public void setCurrentUser(String username) {
            this.currentUser = username;
        }
    }

    @Test
    void successTest_updateProfile() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        // Create a user - make sure name matches username for the key
        User existingUser = factory.create("Annie", "password", 100, "Old bio", "Pikachu", null, null);
        existingUser.setName("Old Name");
        // Use updateUserProfile to save with username as key
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                "New bio", "Charizard", "New Name",
                "path/to/photo.jpg", "path/to/banner.jpg");

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("Annie", outputData.getUsername());
                assertEquals("New Name", outputData.getName());
                assertEquals("New bio", outputData.getBio());
                assertEquals("Charizard", outputData.getFavPokemon());
                assertEquals("path/to/photo.jpg", outputData.getProfilePhotoPath());
                assertEquals("path/to/banner.jpg", outputData.getBannerPath());

                // Verify user was updated in repository
                User updatedUser = userRepository.get("Annie");
                assertNotNull(updatedUser);
                assertEquals("New Name", updatedUser.getName());
                assertEquals("New bio", updatedUser.getBio());
                assertEquals("Charizard", updatedUser.getFavPokemon());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_updateUsername() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", "NewAnnie", null, 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("NewAnnie", outputData.getUsername());
                assertEquals("NewAnnie", dashboard.getCurrentUser());

                // Verify old username is gone and new one exists
                assertNull(userRepository.get("Annie"));
                assertNotNull(userRepository.get("NewAnnie"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_updatePassword() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "oldpassword", 100, "Bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "oldpassword", null, "newpassword", 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                User updatedUser = userRepository.get("Annie");
                assertEquals("newpassword", updatedUser.getPassword());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_emptyDisplayName() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                "Bio", "Pikachu", "", // Empty display name
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Display name cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_nullDisplayName() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                "Bio", "Pikachu", null, // Null display name
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Display name cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_displayNameTooLong() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        String longName = "a".repeat(33); // 33 characters, exceeds limit of 32
        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                "Bio", "Pikachu", longName,
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Display name must be <= 32 characters long.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_bioTooLong() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        String longBio = "a".repeat(501); // 501 characters, exceeds limit of 500
        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                longBio, "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Bio must be <= 500 characters long.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_usernameTooLong() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        String longUsername = "a".repeat(33); // 33 characters, exceeds limit of 32
        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", longUsername, null, 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Username must be <= 32 characters long.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_usernameAlreadyExists() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.save(existingUser);

        // Create another user with the username we want to change to
        User otherUser = factory.create("John", "password", 50, "Bio", "Charizard", null, null);
        userRepository.updateUserProfile("John", otherUser);

        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", "John", null, 100, // Try to change username to "John"
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Username already exists.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_emptyPassword() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, "", 100, // Empty new password
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Password cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_maxLengthDisplayName() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        String maxLengthName = "a".repeat(32); // Exactly 32 characters
        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                "Bio", "Pikachu", maxLengthName,
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals(maxLengthName, outputData.getName());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_maxLengthBio() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        String maxLengthBio = "a".repeat(500); // Exactly 500 characters
        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                maxLengthBio, "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals(maxLengthBio, outputData.getBio());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_nullBio() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                null, "Pikachu", "Display Name", // Null bio should be allowed
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertNull(outputData.getBio());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_whitespaceOnlyDisplayName() throws IOException {
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                "Bio", "Pikachu", "   ", // Whitespace only
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Display name cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void testUserProfileOutputData_constructor() {
        // Test the constructor and all getters for coverage
        UserProfileOutputData outputData = new UserProfileOutputData(
                "testuser", "Test Name", "Test bio", "Pikachu",
                "photo.jpg", "banner.jpg");

        // Test all getters for coverage
        assertEquals("testuser", outputData.getUsername());
        assertEquals("Test Name", outputData.getName());
        assertEquals("Test bio", outputData.getBio());
        assertEquals("Pikachu", outputData.getFavPokemon());
        assertEquals("photo.jpg", outputData.getProfilePhotoPath());
        assertEquals("banner.jpg", outputData.getBannerPath());
    }

    @Test
    void testUserProfileOutputData_withNullValues() {
        // Test with null values
        UserProfileOutputData partialData = new UserProfileOutputData(
                "testuser", "Test Name", null, "None",
                null, null);
        assertNull(partialData.getBio());
        assertEquals("None", partialData.getFavPokemon());
        assertNull(partialData.getProfilePhotoPath());
        assertNull(partialData.getBannerPath());
    }

    @Test
    void successTest_newUsernameIsNull() throws IOException {
        // Test branch: newUsername is null (line 58 - false branch)
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100, // newUsername is null
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("Annie", outputData.getUsername()); // Should keep current username
                assertEquals("Annie", dashboard.getCurrentUser()); // Dashboard should not change
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_newUsernameIsEmpty() throws IOException {
        // Test branch: newUsername is empty string (line 58 - false branch)
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", "", null, 100, // newUsername is empty
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("Annie", outputData.getUsername()); // Should keep current username
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_newUsernameEqualsCurrentUsername() throws IOException {
        // Test branch: newUsername equals currentUsername (line 63 - false branch for
        // first condition)
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", "Annie", null, 100, // newUsername equals currentUsername
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("Annie", outputData.getUsername());
                // Should use updateUserProfile, not updateUsername
                assertNotNull(userRepository.get("Annie"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_newPasswordIsNull() throws IOException {
        // Test branch: newPassword is null (line 71 - false branch)
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "oldpassword", 100, "Bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "oldpassword", null, null, 100, // newPassword is null
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                User updatedUser = userRepository.get("Annie");
                assertEquals("oldpassword", updatedUser.getPassword()); // Should keep old password
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_newPasswordIsWhitespace() throws IOException {
        // Test branch: newPassword is whitespace only (line 71 - should trigger
        // validation)
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "oldpassword", 100, "Bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "oldpassword", null, "   ", 100, // newPassword is whitespace only
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Password cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_bioIsNull() throws IOException {
        // Test branch: bio is null (line 51 - false branch)
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "password", 100, "Old bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "password", null, null, 100,
                null, "Pikachu", "Display Name", // bio is null
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertNull(outputData.getBio());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_newPasswordNotNullAndNotEmpty() throws IOException {
        // Test branch: newPassword != null && !newPassword.trim().isEmpty() is TRUE
        // (line 80 - ternary true branch)
        // This explicitly tests the true branch of the ternary operator
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Annie", "oldpassword123", 100, "Bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Annie", existingUser);
        dashboard.setCurrentUser("Annie");

        // Use a password that is not null and not empty (and not just whitespace)
        UserProfileInputData inputData = new UserProfileInputData(
                "Annie", "oldpassword123", null, "validNewPassword", 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                User updatedUser = userRepository.get("Annie");
                // Verify the new password was used (ternary true branch)
                assertEquals("validNewPassword", updatedUser.getPassword());
                assertNotEquals("oldpassword123", updatedUser.getPassword());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_newPasswordWithWhitespace() throws IOException {
        // Test branch: newPassword != null && !newPassword.trim().isEmpty() is TRUE
        // Specifically testing with a password that has whitespace but trims to
        // non-empty
        // This ensures both parts of the condition are evaluated as true
        UserFactory factory = new UserFactory();
        TestUserDataAccessObject userRepository = new TestUserDataAccessObject(factory);
        MockGameDashboard dashboard = new MockGameDashboard();

        User existingUser = factory.create("Paul", "oldpass", 100, "Bio", "Pikachu", null, null);
        existingUser.setName("Display Name");
        userRepository.updateUserProfile("Paul", existingUser);
        dashboard.setCurrentUser("Paul");

        // Password with leading/trailing whitespace that trims to non-empty
        UserProfileInputData inputData = new UserProfileInputData(
                "Paul", "oldpass", null, "  newpass  ", 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                User updatedUser = userRepository.get("Paul");
                // The password should be trimmed and used (ternary true branch)
                // The condition evaluates: newPassword != null (true) &&
                // !newPassword.trim().isEmpty() (true)
                assertEquals("  newpass  ", updatedUser.getPassword()); // Password is stored as-is, not trimmed
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, factory, dashboard);
        interactor.execute(inputData);
    }
}
