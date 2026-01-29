package use_case.daily_mix;

import data_access.SpotifyDataAccessObject;
import entity.SpotifyUser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DailyMixInteractor.
 */
public class DailyMixInteractorTest {

    /**
     * Fake DAO that returns a fixed list of tracks (success path).
     */
    static class FakeSpotifyDataAccessObjectSuccess extends SpotifyDataAccessObject {
        @Override
        public List<String> generateDailyMix(SpotifyUser user, int count) {
            List<String> tracks = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                tracks.add("Track " + i);
            }
            return tracks;
        }
    }

    /**
     * Fake DAO that always throws an exception (failure path).
     */
    static class FakeSpotifyDataAccessObjectFailure extends SpotifyDataAccessObject {
        @Override
        public List<String> generateDailyMix(SpotifyUser user, int count) {
            throw new RuntimeException("Spotify API error");
        }
    }

    /**
     * Fake presenter that just records what the interactor sends it.
     */
    static class RecordingDailyMixPresenter implements DailyMixOutputBoundary {
        private List<String> tracks;
        private String errorMessage;

        @Override
        public void prepareSuccessView(DailyMixOutputData response) {
            this.tracks = response.getTracks();
            this.errorMessage = null;
        }

        @Override
        public void prepareFailView(String error) {
            this.tracks = null;
            this.errorMessage = error;
        }

        public List<String> getTracks() {
            return tracks;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    private SpotifyUser makeDummySpotifyUser() {
        return new SpotifyUser(
                "alice",
                "access-token",
                "refresh-token",
                "spotify-id"
        );
    }

    @Test
    public void testExecuteSuccess() {
        RecordingDailyMixPresenter presenter = new RecordingDailyMixPresenter();
        DailyMixInteractor interactor =
                new DailyMixInteractor(new FakeSpotifyDataAccessObjectSuccess(), presenter);

        SpotifyUser user = makeDummySpotifyUser();
        DailyMixInputData input = new DailyMixInputData(user, 5);

        interactor.execute(input);

        assertNotNull(presenter.getTracks());
        assertNull(presenter.getErrorMessage());
        assertEquals(5, presenter.getTracks().size());
        assertEquals("Track 1", presenter.getTracks().get(0));
    }

    @Test
    public void testExecuteFailure() {
        RecordingDailyMixPresenter presenter = new RecordingDailyMixPresenter();
        DailyMixInteractor interactor =
                new DailyMixInteractor(new FakeSpotifyDataAccessObjectFailure(), presenter);

        SpotifyUser user = makeDummySpotifyUser();
        DailyMixInputData input = new DailyMixInputData(user, 5);

        interactor.execute(input);

        assertNull(presenter.getTracks());
        assertNotNull(presenter.getErrorMessage());
        assertTrue(presenter.getErrorMessage().contains("Failed to generate Daily Mix"));
    }
}





