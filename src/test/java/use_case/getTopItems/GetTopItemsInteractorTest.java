package use_case.getTopItems;

import data_access.TopItemDataAccessObject;
import org.junit.jupiter.api.Test;
import entity.SpotifyUser;
import use_case.get_topItems.*;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Tests for GetTopItemsInteractor
 */

public class GetTopItemsInteractorTest {

    /**
     * Fake TopItemDataAccessObject that returns a fixed GetTopItemsOutput (success path)
     */
    static class FakeTopItemDataAccessObjectSuccess extends TopItemDataAccessObject {
        @Override
        public GetTopItemsOutputData fetchSpotifyResult(GetTopItemsInputData inputData) {
            List<String> topItems =  new ArrayList<>();
            for (int i = 1; i<=10; i++){
                topItems.add("Item " + i);
            }
            return new GetTopItemsOutputData(topItems);
        }
    }
    /**
     * Fake TopItemDataAccessObject that always throws an exception (failure path)
     */
    static class FakeTopItemDataAccessObjectFailure extends TopItemDataAccessObject {
        @Override
        public GetTopItemsOutputData fetchSpotifyResult(GetTopItemsInputData inputData) {
            throw new RuntimeException("Spotify API error");
        }
    }

    /**
     * Fake presenter that just records what the interactor sends it.
     */
    static class RecordingGetTopItemsPresenter implements GetTopItemsOutputBoundary{
        private List<String> topItems;
        private String errorMessage;

        @Override
        public void prepareSuccessView(GetTopItemsOutputData outputData) {
            this.topItems = outputData.getTopItems();
            this.errorMessage = null;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.topItems = null;
            this.errorMessage = errorMessage;
        }

        public List<String> getTopItems() {
            return topItems;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public SpotifyUser makeSpotifyUser(){
        return new SpotifyUser("alice",
                "access-token",
                "refresh-token",
                "spotify-id"
        );
    }

    @Test
    public void testExecuteSuccess(){
        RecordingGetTopItemsPresenter presenter = new RecordingGetTopItemsPresenter();
        GetTopItemsInteractor interactor = new GetTopItemsInteractor(presenter,
                new FakeTopItemDataAccessObjectSuccess());

        SpotifyUser spotifyUser = makeSpotifyUser();
        GetTopItemsInputData input = new GetTopItemsInputData(TopItem.tracks,
                TimeRange.long_term,
                spotifyUser);

        interactor.execute(input);

        assertNotNull(presenter.getTopItems());
        assertNull(presenter.getErrorMessage());
        assertEquals(10,presenter.getTopItems().size());
        assertEquals("Item 1", presenter.getTopItems().get(0));

    }

    @Test
    public void testExecuteFailure(){
        RecordingGetTopItemsPresenter presenter = new RecordingGetTopItemsPresenter();
        GetTopItemsInteractor interactor = new GetTopItemsInteractor(presenter
                , new  FakeTopItemDataAccessObjectFailure());

        SpotifyUser spotifyUser = makeSpotifyUser();
        GetTopItemsInputData input = new GetTopItemsInputData(null,
                TimeRange.long_term,
                spotifyUser);

        interactor.execute(input);

        assertNull(presenter.getTopItems());
        assertNotNull(presenter.getErrorMessage());
        assertTrue(presenter.getErrorMessage().contains("Spotify API error"));
    }
}
