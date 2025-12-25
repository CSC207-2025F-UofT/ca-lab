package use_case.deletepet;

import data_access.InMemoryPetDataAccessObject;
import entity.Pet;
import entity.PetFactory;
import org.junit.jupiter.api.Test;
import use_case.delete_pet.*;

import static org.junit.jupiter.api.Assertions.*;

class DeletePetInteractorTest {

    @Test
    void successTest() {
        DeletePetDataAccessInterface repo = new InMemoryPetDataAccessObject();
        PetFactory factory = new PetFactory();

        Pet p = factory.create("CAT-001", "Bird","Cat","Persian",
                "Adult","Female","Small","6470001111");

        ((InMemoryPetDataAccessObject) repo).add(p);

        DeletePetInputData input = new DeletePetInputData("CAT-001");

        DeletePetOutputBoundary presenter = new DeletePetOutputBoundary() {

            @Override
            public void preparePetView(DeletePetOutputData petData) {
                assertEquals("CAT-001", petData.getId());
                assertEquals("Bird", petData.getName());
                assertEquals("Cat", petData.getType());
            }

            @Override
            public void prepareWarningView(String warning) {
                assertEquals("Are you sure you want to Delete this Pet?", warning);
            }

            @Override
            public void prepareSuccessView(DeletePetOutputData data) {
                assertEquals("Pet deleted successfully!", data.getMessage());
                assertFalse(repo.existsPet("CAT-001"));
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        DeletePetInputBoundary interactor =
                new DeletePetInteractor(repo, presenter);

        interactor.execute(input);
    }


    @Test
    void failurePetDoesNotExist() {
        DeletePetDataAccessInterface petRepo = new InMemoryPetDataAccessObject();

        DeletePetInputData input = new DeletePetInputData("CAT-999");

        DeletePetOutputBoundary presenter = new DeletePetOutputBoundary() {
            @Override
            public void preparePetView(DeletePetOutputData petData) {
                fail("Should not show preview for missing pet.");
            }

            @Override
            public void prepareWarningView(String warningMessage) {
                fail("Should not show warning for missing pet.");
            }

            @Override
            public void prepareSuccessView(DeletePetOutputData data) {
                fail("Should not succeed when pet does not exist.");
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                assertEquals("Pet Does not Exist.", errorMessage);
            }
        };

        DeletePetInputBoundary interactor =
                new DeletePetInteractor(petRepo, presenter);

        interactor.execute(input);
    }
}

