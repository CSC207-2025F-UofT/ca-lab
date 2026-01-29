package interface_adapter;

import entity.Application;
import org.junit.jupiter.api.Test;

import interface_adapter.manage_application.ManageApplicationPresenter;
import interface_adapter.manage_application.ManageApplicationViewModel;
import org.junit.jupiter.api.Test;
import use_case.manage_application.ManageApplicationOutputData;
import entity.Application;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageApplicationViewModelTest {

    @Test
    void setApplications_firesPropertyChange() {
        ManageApplicationViewModel vm = new ManageApplicationViewModel();

        Application a = new Application(
                "idA", "A", "B", "a@mail.com", "111",
                "30", "Engineer", "p1",
                "Reason", "Env", "Avail", "Exp"
        );

        final boolean[] eventReceived = {false};

        vm.addPropertyChangeListener(event -> {
            assertEquals("applications", event.getPropertyName());
            assertEquals(List.of(a), event.getNewValue());
            eventReceived[0] = true;
        });

        vm.setApplications(List.of(a));

        assertTrue(eventReceived[0]);
        assertEquals(1, vm.getApplications().size());
        assertEquals("idA", vm.getApplications().get(0).getApplicationId());
    }

    @Test
    void getViewName_returnsCorrectString() {
        ManageApplicationViewModel vm = new ManageApplicationViewModel();
        assertEquals("Manage Application", vm.getViewName());
    }
}
