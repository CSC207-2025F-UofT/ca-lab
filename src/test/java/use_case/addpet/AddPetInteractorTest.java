package use_case.addpet;

import data_access.InMemoryPetDataAccessObject;
import entity.Pet;
import entity.PetFactory;
import org.junit.jupiter.api.Test;
import use_case.add_pet.*;

import static org.junit.jupiter.api.Assertions.*;

class AddPetInteractorTest {

    @Test
    void successTest() {
        // Arrange
        PetFactory factory = new PetFactory();
        InMemoryPetDataAccessObject repo = new InMemoryPetDataAccessObject();

        AddPetInputData input = new AddPetInputData(
                "Buddy",
                "Dog",
                "Beagle",
                "3",
                "Male",
                "Medium",
                "zahra@mail.com"
        );

        AddPetOutputBoundary presenter = new AddPetOutputBoundary() {
            @Override
            public void prepareSuccessView(AddPetOutputData data) {
                assertNotNull(data.getId());
                assertTrue(repo.existsPet(data.getId()));   // check ID exists in repo
            }

            @Override
            public void prepareFailView(String error) {
                fail("Unexpected failure: " + error);
            }
        };

        AddPetInputBoundary interactor = new AddPetInteractor(factory, repo, presenter);

        // Act
        interactor.execute(input);
    }

    @Test
    void failureDuplicatePetTest() {
        // Arrange
        PetFactory factory = new PetFactory();
        AddPetDataAccessInterface repo = new InMemoryPetDataAccessObject();

        AddPetInputData input = new AddPetInputData(
                "Buddy", "Dog", "Beagle", "3", "Male", "Medium", "zahra@mail.com"
        );

        // Generate the SAME ID the interactor will generate
        String existingId = repo.generateId("Dog");

        Pet existingPet = factory.create(existingId, "Buddy", "Dog", "Beagle",
                "3", "Male", "Medium", "zahra@mail.com");

        repo.add(existingPet);

        AddPetOutputBoundary presenter = new AddPetOutputBoundary() {
            @Override
            public void prepareSuccessView(AddPetOutputData data) {
                fail("Expected duplicate pet failure — got success.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("A pet already exists.", error);
            }
        };

        AddPetInputBoundary interactor = new AddPetInteractor(factory, repo, presenter);

        // Act
        interactor.execute(input);
    }

}



