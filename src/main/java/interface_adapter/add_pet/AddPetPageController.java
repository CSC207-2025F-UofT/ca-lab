package interface_adapter.add_pet;

import interface_adapter.ViewManagerModel;

public class AddPetPageController {

    private final ViewManagerModel viewManagerModel;
    private final AddPetViewModel addPetViewModel;

    public AddPetPageController(ViewManagerModel viewManagerModel,
                                AddPetViewModel addPetViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.addPetViewModel = addPetViewModel;
    }

    public void execute() {
        viewManagerModel.setState(addPetViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
