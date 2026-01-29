package interface_adapter.delete_pet;

import interface_adapter.ViewManagerModel;
import interface_adapter.delete_pet.DeletePetViewModel;

public class DeletePetPageController {

    private final ViewManagerModel viewManagerModel;
    private final DeletePetViewModel deletePetViewModel;

    public DeletePetPageController(ViewManagerModel viewManagerModel,
                                DeletePetViewModel deletePetViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.deletePetViewModel = deletePetViewModel;
    }

    public void execute() {
        viewManagerModel.setState(deletePetViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
