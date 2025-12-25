package data_access;

import entity.User;
import entity.UserFactory;
import entity.Visitor;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.submit_application.SubmitUserDataAccessInterface;

import java.io.IOException;

/**
 * The DAO for user data.
 */
public class DBUserDataAccessObject implements SignupUserDataAccessInterface,
                                               LoginUserDataAccessInterface,
                                               ChangePasswordUserDataAccessInterface,
                                               LogoutUserDataAccessInterface,
                                               SubmitUserDataAccessInterface {

    private static final int SUCCESS_CODE = 200;
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String STATUS_CODE_LABEL = "status_code";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MESSAGE = "message";

    private static final String INFO = "info";
    private static final String USER_TYPE = "usertype";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ADDRESS = "address";
    private static final String HOME_EVI = "homeEvi";
    private static final String OCCUPATION = "occupation";
    private static final String EMAIL = "email";
    private static final String TEL = "tel";
    private static final String AGE = "age";

    private final UserFactory userFactory;

    private String currentUsername;

    public DBUserDataAccessObject(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    @Override
    public User get(String username) {
        // Make an API call to get the user object.
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("http://vm003.teach.cs.toronto.edu:20112/user?username=%s", username))
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                final JSONObject userJSONObject = responseBody.getJSONObject("user");
                final String name = userJSONObject.getString(USERNAME);
                final String password = userJSONObject.getString(PASSWORD);

                JSONObject infoJSON = null;
                String usertype = "visitor";

                if (userJSONObject.has(INFO)) {
                    infoJSON = userJSONObject.getJSONObject(INFO);
                    usertype = infoJSON.optString(USER_TYPE, "visitor");
                }

                final User user = userFactory.create(usertype, name, password);
                if (user instanceof Visitor && infoJSON != null) {
                    final Visitor visitor = (Visitor) user;

                    visitor.setFirstName(infoJSON.optString(FIRST_NAME, ""));
                    visitor.setLastName(infoJSON.optString(LAST_NAME, ""));
                    visitor.setAddress(infoJSON.optString(ADDRESS, ""));
                    visitor.setHomeEvi(infoJSON.optString(HOME_EVI, ""));
                    visitor.setOccupation(infoJSON.optString(OCCUPATION, ""));
                    visitor.setEmail(infoJSON.optString(EMAIL, ""));
                    visitor.setTel(infoJSON.optString(TEL, ""));
                    visitor.setAge(infoJSON.optString(AGE, ""));
                }
                return user;
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
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
    public boolean existsByName(String username) {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final Request request = new Request.Builder()
                .url(String.format("http://vm003.teach.cs.toronto.edu:20112/checkIfUserExists?username=%s", username))
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            //                throw new RuntimeException(responseBody.getString("message"));
            return responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE;
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        if (!existsByName(user.getName())) {
            createUserHelper(user);
        }

        JSONObject info = new JSONObject();
        info.put("usertype", user.getUserType());

        if (user instanceof Visitor){
            Visitor visitor = (Visitor) user;
            info.put(FIRST_NAME, visitor.getFirstName());
            info.put(LAST_NAME, visitor.getLastName());
            info.put(ADDRESS, visitor.getAddress());
            info.put(HOME_EVI, visitor.getHomeEvi());
            info.put(OCCUPATION, visitor.getOccupation());
            info.put(EMAIL, visitor.getEmail());
            info.put(TEL, visitor.getTel());
            info.put(AGE, visitor.getAge());
        }

        updateInfoHelper(user.getName(), user.getPassword(), info);
    }

    private void createUserHelper(User user){
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);

        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, user.getName());
        requestBody.put(PASSWORD, user.getPassword());

        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
        final Request request = new Request.Builder()
                .url("http://vm003.teach.cs.toronto.edu:20112/user")
                .method("POST", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try(Response response = client.newCall(request).execute()){
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                // success!
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void updateInfoHelper(String username, String password, JSONObject info) {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);

        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, username);
        requestBody.put(PASSWORD, password);
        requestBody.put(INFO, info);

        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
        final Request request = new Request.Builder()
                .url("http://vm003.teach.cs.toronto.edu:20112/modifyUserInfo")
                .method("PUT", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();

        try (final Response response = client.newCall(request).execute()){
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                // success!
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void changePassword(User user) {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                                        .build();

        // POST METHOD
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, user.getName());
        requestBody.put(PASSWORD, user.getPassword());
        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
        final Request request = new Request.Builder()
                                    .url("http://vm003.teach.cs.toronto.edu:20112/user")
                                    .method("PUT", body)
                                    .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                                    .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                // success!
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}
