package use_case.watchlist;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WatchlistInteractorTest {
    @Test
    void successAddTest() {
        JSONObject object = new JSONObject();
        WatchlistInputData inputData = new WatchlistInputData("Bob", object);

        UserFactory factory = new UserFactory();
        User bob = factory.create("Bob", "password");

        WatchlistUserDataAccessInterface test = new FileUserDataAccessObject("test1.csv", factory);
        test.save(bob);

        WatchlistOutputBoundary successPresenter = new WatchlistOutputBoundary() {
            @Override
            public void prepareSuccessView(String username, WatchlistOutputData data) {
                assertEquals("Bob", username);
                assertTrue(data.getWatchlist().contains(object));
                assertEquals(object, data.getWatchlist().get(0));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Failed to add to watchlist.");
            }
        };

        WatchlistInputBoundary interactor =
                new WatchlistInteractor(test, successPresenter);

        interactor.execute(inputData);
    }

    @Test
    void failUserDoesntExistTest() {
        JSONObject object = new JSONObject();
        UserFactory factory = new UserFactory();
        WatchlistInputData inputData = new WatchlistInputData("Bob", object);

        WatchlistUserDataAccessInterface test = new FileUserDataAccessObject("test2.csv", factory);

        WatchlistOutputBoundary failurePresenter = new WatchlistOutputBoundary() {
            @Override
            public void prepareSuccessView(String username, WatchlistOutputData data) {
                fail("User doesn't exist.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("User does not exist.", error);
            }
        };

        WatchlistInputBoundary interactor =
                new WatchlistInteractor(test, failurePresenter);

        interactor.execute(inputData);
    }

    @Test
    void successMultipleItemsTest() {
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        WatchlistInputData inputData = new WatchlistInputData("Bob", object1);

        UserFactory factory = new UserFactory();
        User bob = factory.create("Bob", "password");
        bob.getWatchlist().add(object2);
        WatchlistUserDataAccessInterface test = new FileUserDataAccessObject("test3.csv", factory);
        test.save(bob);

        WatchlistOutputBoundary successPresenter = new WatchlistOutputBoundary() {
            @Override
            public void prepareSuccessView(String username, WatchlistOutputData data) {
                assertEquals("Bob", username);
                assertEquals(2, data.getWatchlist().size());
                assertTrue(data.getWatchlist().contains(object1));
                assertTrue(data.getWatchlist().contains(object2));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Failed to contain both.");
            }
        };

        WatchlistInputBoundary interactor =
                new WatchlistInteractor(test, successPresenter);

        interactor.execute(inputData);
    }

    @Test
    void successfulRemoveTest() {
        JSONObject object = new JSONObject();
        JSONObject toRemove = new JSONObject().put("name", object);
        RemoveWatchlistInputData inputData =
                new RemoveWatchlistInputData("Bob", toRemove);

        UserFactory factory = new UserFactory();
        User bob = factory.create("Bob", "password");
        WatchlistUserDataAccessInterface test = new FileUserDataAccessObject("test4.csv", factory);
        bob.getWatchlist().add(new JSONObject().put("name", object));
        test.save(bob);

        WatchlistOutputBoundary successPresenter = new WatchlistOutputBoundary() {
            @Override
            public void prepareSuccessView(String username, WatchlistOutputData data) {
                assertEquals("Bob", username);
                assertEquals(0, data.getWatchlist().size());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Failed to remove, but shouldn't.");
            }
        };

        RemoveWatchlistInputBoundary interactor =
                new RemoveWatchlistInteractor(test, successPresenter);

        interactor.execute(inputData);
    }


    @Test
    void failUserDoesntExistRemoveTest() {
        JSONObject object = new JSONObject();
        UserFactory factory = new UserFactory();
        JSONObject toRemove = new JSONObject().put("name", object);
        RemoveWatchlistInputData inputData =
                new RemoveWatchlistInputData("Bob", toRemove);

        WatchlistUserDataAccessInterface repo = new FileUserDataAccessObject("test5.csv", factory);

        WatchlistOutputBoundary failurePresenter = new WatchlistOutputBoundary() {
            @Override
            public void prepareSuccessView(String username, WatchlistOutputData data) {
                fail("User doesn't exist, shouldn't work.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("User does not exist.", error);
            }
        };

        RemoveWatchlistInputBoundary interactor =
                new RemoveWatchlistInteractor(repo, failurePresenter);

        interactor.execute(inputData);
    }


    @Test
    void successRemoveFromMultipleItemsTest() {
        JSONObject object = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject toRemove = new JSONObject().put("name", object);
        RemoveWatchlistInputData inputData =
                new RemoveWatchlistInputData("Bob", toRemove);

        UserFactory factory = new UserFactory();
        User bob = factory.create("Bob", "password");
        WatchlistUserDataAccessInterface repo = new FileUserDataAccessObject("test6.csv", factory);
        bob.getWatchlist().add(new JSONObject().put("name", object));
        bob.getWatchlist().add(new JSONObject().put("name2", object2));
        repo.save(bob);

        WatchlistOutputBoundary successPresenter = new WatchlistOutputBoundary() {
            @Override
            public void prepareSuccessView(String username, WatchlistOutputData data) {
                assertEquals("Bob", username);
                assertEquals(1, data.getWatchlist().size());
                assertTrue(data.getWatchlist().get(0).has("name2"));
                JSONObject remaining = data.getWatchlist().get(0).getJSONObject("name2");
                assertEquals(object2.toString(), remaining.toString());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Failed to remove, but shouldn't.");
            }
        };

        RemoveWatchlistInputBoundary interactor =
                new RemoveWatchlistInteractor(repo, successPresenter);

        interactor.execute(inputData);
    }
}

