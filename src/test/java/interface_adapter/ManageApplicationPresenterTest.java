package interface_adapter;

import interface_adapter.manage_application.ManageApplicationPresenter;
import interface_adapter.manage_application.ManageApplicationViewModel;
import org.junit.jupiter.api.Test;
import use_case.manage_application.ManageApplicationOutputData;
import entity.Application;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageApplicationPresenterTest {

    @Test
    void presenter_updatesViewModel() {
        ManageApplicationViewModel viewModel = new ManageApplicationViewModel();
        ManageApplicationPresenter presenter = new ManageApplicationPresenter(viewModel);

        Application a = new Application(
                "id123", "John", "Doe", "j@mail.com",
                "555", "20", "Student", "p1",
                "Love pets", "Home", "Always", "None"
        );

        ManageApplicationOutputData output = new ManageApplicationOutputData(List.of(a));

        presenter.present(output);

        assertEquals(1, viewModel.getApplications().size());
        assertEquals("id123", viewModel.getApplications().get(0).getApplicationId());
    }
}
