package use_case.view_pet_details;

import data_access.PetAPIGatewayInterface;
import entity.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class ViewPetDetailInteractorTest {
    private PetAPIGatewayInterface petGateway;
    private ViewPetDetailsOutputBoundary presenter;
    private ViewPetDetailsInteractor interactor;

    @BeforeEach
    void setUp() {
        petGateway = mock(PetAPIGatewayInterface.class);
        presenter = mock(ViewPetDetailsOutputBoundary.class);
        interactor = new ViewPetDetailsInteractor(petGateway, presenter);
    }
    @Test
    void testExecute_WithValidPet() {
        String petId = "123";
        Pet expectedPet = new Pet(
                "123", "Buddy", "Dog", "Beagle",
                "3 years", "Male", "Small", "555-2222"
        );

        when(petGateway.fetchPetById(petId)).thenReturn(expectedPet);
        ViewPetDetailsInputData inputData = new ViewPetDetailsInputData(petId);

        interactor.execute(inputData);

        verify(petGateway).fetchPetById(petId);
        ArgumentCaptor<ViewPetDetailsOutputData> captor =
                ArgumentCaptor.forClass(ViewPetDetailsOutputData.class);
        verify(presenter).present(captor.capture());
        assertEquals(expectedPet, captor.getValue().getPet());
    }

    @Test
    void testExecute_WithNullPet() {
        String petId = "999";
        when(petGateway.fetchPetById(petId)).thenReturn(null);
        ViewPetDetailsInputData inputData = new ViewPetDetailsInputData(petId);

        interactor.execute(inputData);

        verify(petGateway).fetchPetById(petId);
        ArgumentCaptor<ViewPetDetailsOutputData> captor =
                ArgumentCaptor.forClass(ViewPetDetailsOutputData.class);
        verify(presenter).present(captor.capture());
        assertNull(captor.getValue().getPet());
    }

    @Test
    void testSwitchToApplicationView_CallsPresenter() {
        interactor.switchToApplicationView();
        verify(presenter).switchToApplicationView();
    }
}
