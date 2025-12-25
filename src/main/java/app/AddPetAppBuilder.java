package app;

import data_access.InMemoryPetDataAccessObject;
import data_access.SQLitePetDataAccessObject;
import entity.PetFactory;

// ADD
import interface_adapter.add_pet.*;
import use_case.add_pet.*;

// DELETE
import interface_adapter.delete_pet.*;
import use_case.delete_pet.*;

import javax.swing.*;

public class AddPetAppBuilder {

    private AddPetView addPetView;
    private DeletePetView deletePetView;

    private AddPetController addPetController;
    private DeletePetController deletePetController;

    // shared repo
    //private InMemoryPetDataAccessObject petRepo = new InMemoryPetDataAccessObject();
    private SQLitePetDataAccessObject petRepo = new SQLitePetDataAccessObject();


    public AddPetAppBuilder addAddPetView() {
        this.addPetView = new AddPetView();
        return this;
    }

    public AddPetAppBuilder addDeletePetView() {
        this.deletePetView = new DeletePetView();
        return this;
    }

    public AddPetAppBuilder addAddPetUseCase() {

        AddPetViewModel vm = new AddPetViewModel();
        AddPetOutputBoundary presenter = new AddPetPresenter(vm);

        PetFactory factory = new PetFactory();

        AddPetInputBoundary interactor =
                new AddPetInteractor(factory, petRepo, presenter);

        this.addPetController = new AddPetController(interactor);
        this.addPetView.setViewModel(vm);
        this.addPetView.setAddPetController(addPetController);

        return this;
    }

    public AddPetAppBuilder addDeletePetUseCase() {

        DeletePetViewModel vm = new DeletePetViewModel();
        DeletePetOutputBoundary presenter = new DeletePetPresenter(vm);

        DeletePetInputBoundary interactor =
                new DeletePetInteractor(petRepo, presenter);

        this.deletePetController = new DeletePetController(interactor);

        this.deletePetView.setViewModel(vm);
        this.deletePetView.setDeletePetController(deletePetController);

        return this;
    }

    public JFrame build() {

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Add Pet", addPetView);
        tabs.add("Delete Pet", deletePetView);

        JFrame frame = new JFrame("Pet Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(tabs);

        return frame;
    }
}




