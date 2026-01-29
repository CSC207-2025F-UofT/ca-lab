package data_access;

import entity.User;
import entity.UserFactory;
import entity.Visitor;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.submit_application.SubmitUserDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
                                                 LoginUserDataAccessInterface,
                                                 ChangePasswordUserDataAccessInterface,
                                                 LogoutUserDataAccessInterface,
                                                 SubmitUserDataAccessInterface {

    private static final String HEADER = "username,password,usertype,firstName,lastName," +
            "address,homeEvi,occupation,email,tel,age";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();

    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {

        csvFile = new File(csvPath);
        headers.put("username", 0);
        headers.put("password", 1);
        headers.put("usertype", 2);
        headers.put("firstName", 3);
        headers.put("lastName", 4);
        headers.put("address", 5);
        headers.put("homeEvi", 6);
        headers.put("occupation", 7);
        headers.put("email", 8);
        headers.put("tel", 9);
        headers.put("age", 10);

        if (csvFile.length() == 0) {
            save();
        }
        else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",", -1);
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    final String usertype = String.valueOf(col[headers.get("usertype")]);

                    final User user = userFactory.create(usertype, username, password);

                    if (user instanceof Visitor) {
                        Visitor visitor = (Visitor) user;
                        visitor.setFirstName(col[headers.get("firstName")]);
                        visitor.setLastName(col[headers.get("lastName")]);
                        visitor.setAddress(col[headers.get("address")]);
                        visitor.setHomeEvi(col[headers.get("homeEvi")]);
                        visitor.setOccupation(col[headers.get("occupation")]);
                        visitor.setEmail(col[headers.get("email")]);
                        visitor.setTel(col[headers.get("tel")]);
                        visitor.setAge(col[headers.get("age")]);
                    }

                    accounts.put(username, user);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                if (user instanceof Visitor) {
                    Visitor visitor = (Visitor) user;
                    writer.write(String.join(",",
                            visitor.getName(),
                            visitor.getPassword(),
                            visitor.getUserType(),
                            visitor.getFirstName(),
                            visitor.getLastName(),
                            visitor.getAddress(),
                            visitor.getHomeEvi(),
                            visitor.getOccupation(),
                            visitor.getEmail(),
                            visitor.getTel(),
                            visitor.getAge()
                    ));
                }
                else {writer.write(String.join(",",
                        user.getName(),
                        user.getPassword(),
                        user.getUserType(),
                        "","","","","","","",""
                ));}
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getName(), user);
        this.save();
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        // Replace the User object in the map
        accounts.put(user.getName(), user);
        save();
    }
}
