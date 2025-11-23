package app;

import data_access.InMemoryPetDataAccessObject;
import entity.PetFactory;
import interface_adapter.add_pet.AddPetController;
import interface_adapter.add_pet.AddPetPresenter;
import interface_adapter.add_pet.AddPetView;
import interface_adapter.add_pet.AddPetViewModel;
import use_case.add_pet.AddPetDataAccessInterface;
import use_case.add_pet.AddPetInputBoundary;
import use_case.add_pet.AddPetInteractor;
import use_case.add_pet.AddPetOutputBoundary;

import javax.swing.*;

public class AddPetAppBuilder {

    private AddPetView addPetView;
    private AddPetController addPetController;

    public AddPetAppBuilder addAddPetView() {
        this.addPetView = new AddPetView();
        return this;
    }

    public AddPetAppBuilder addAddPetUseCase() {

        AddPetDataAccessInterface petRepo = new InMemoryPetDataAccessObject();

        AddPetViewModel addPetViewModel = new AddPetViewModel();
        AddPetOutputBoundary presenter = new AddPetPresenter(addPetViewModel);

        PetFactory petFactory = new PetFactory();

        AddPetInputBoundary interactor =
                new AddPetInteractor(petFactory, petRepo, presenter);

        this.addPetController = new AddPetController(interactor);
        this.addPetView.setViewModel(addPetViewModel);
        this.addPetView.setAddPetController(addPetController);

        return this;
    }

    public JFrame build() {
        JFrame frame = new JFrame("Add Pet");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(addPetView);
        return frame;
    }
}


