//Creating this file temporarily to put data in the database. Should be included with Main later on.

package app;

import data_access.*;
import java.sql.Connection;

public class MainDatabase {
    public static void main(String[] args) throws Exception {
        // Step 1: Connect to SQLite database
        Connection conn = DatabaseConnection.connect();
        DatabasePetGateway dbGateway = new DatabasePetGateway(conn);

        // Step 2: Populate DB from API if empty
        var existingPets = dbGateway.fetchPets("", "");
        if (existingPets.isEmpty()) {
            System.out.println("🐾 Database is empty — importing pets from API...");

            String[] speciesList = {"dog", "cat", "rabbit", "bird", "horse"}; // Can add more types
            int totalImported = 0;

            for (String species : speciesList) {
                // Get a new token for every API call (prevents "token expired" errors)
                String token = PetfinderAuthService.getAccessToken();
                PetfinderAPIGateway apiGateway = new PetfinderAPIGateway(token);

                System.out.println("Fetching " + species + " from API...");
                var pets = apiGateway.fetchPets(species, "");

                if (pets.isEmpty()) {
                    System.out.println("⚠️ No pets fetched for " + species + " — check API or token");
                } else {
                    for (var pet : pets) {
                        dbGateway.addPet(pet);
                    }
                    totalImported += pets.size();
                    System.out.println("Imported " + pets.size() + " " + species + "(s) into database!");
                }
            }

            System.out.println("Total pets imported: " + totalImported);
        }

        // Step 3: Show how many pets are in the database
        var allPets = dbGateway.fetchPets("", "");
        System.out.println("Total pets in database: " + allPets.size());
        for (var pet : allPets) {
            System.out.println(pet.getType());
        }

    }
}
