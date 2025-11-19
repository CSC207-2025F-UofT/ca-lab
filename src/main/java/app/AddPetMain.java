package app;

import app.AddPetAppBuilder;

import javax.swing.*;

public class AddPetMain {
    public static void main(String[] args) {

        AddPetAppBuilder builder = new AddPetAppBuilder();

        JFrame app = builder
                .addAddPetView()
                .addAddPetUseCase()
                .build();

        app.pack();
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }
}

