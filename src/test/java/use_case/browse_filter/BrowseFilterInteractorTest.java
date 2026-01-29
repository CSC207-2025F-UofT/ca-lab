package use_case.browse_filter;

import data_access.PetAPIGatewayInterface;
import entity.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrowseFilterInteractorTest {

    private PetAPIGatewayInterface gateway;
    private BrowseFilterOutputBoundary presenter;
    private BrowseFilterInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(PetAPIGatewayInterface.class);
        presenter = mock(BrowseFilterOutputBoundary.class);
        interactor = new BrowseFilterInteractor(gateway, presenter);
    }

    @Test
    void testExecute_WithPets() {
        
        String type = "Dog";
        String gender = "Female";

        List<Pet> expectedPets = Arrays.asList(
                new Pet("1", "Bella", "Dog", "Husky", "Young", "Female", "Medium", "555-1111"),
                new Pet("2", "Luna", "Dog", "Beagle", "Adult", "Female", "Small", "555-2222")
        );

        when(gateway.fetchPets(type, gender)).thenReturn(expectedPets);

        BrowseFilterInputData inputData = new BrowseFilterInputData(type, gender);

        
        interactor.execute(inputData);

        
        verify(gateway).fetchPets(type, gender);

       
        ArgumentCaptor<BrowseFilterOutputData> captor =
                ArgumentCaptor.forClass(BrowseFilterOutputData.class);

        verify(presenter).present(captor.capture());

        assertEquals(expectedPets, captor.getValue().getPets());
    }

    @Test
    void testExecute_EmptyList() {
        when(gateway.fetchPets("Cat", "Male"))
                .thenReturn(Collections.emptyList());

        BrowseFilterInputData inputData =
                new BrowseFilterInputData("Cat", "Male");

        interactor.execute(inputData);

        verify(gateway).fetchPets("Cat", "Male");

        ArgumentCaptor<BrowseFilterOutputData> captor =
                ArgumentCaptor.forClass(BrowseFilterOutputData.class);

        verify(presenter).present(captor.capture());
        assertTrue(captor.getValue().getPets().isEmpty());
    }
}
