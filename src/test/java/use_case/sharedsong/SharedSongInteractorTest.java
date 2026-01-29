package use_case.sharedsong;

import data_access.SpotifyDataAccessObject;
import entity.Group;
import entity.SpotifyUser;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.requests.data.library.CheckUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SharedSongInteractorTest {

    SpotifyUser spotifyUser = new SpotifyUser("heath",
            "x",
            "y",
            "htaeh");
    SpotifyUser member1 = new SpotifyUser("user1",
            "a",
            "b",
            "1stUser");
    SpotifyUser member2 = new SpotifyUser("user2",
            "c",
            "d",
            "2ndUser");
    Group group = new Group("group1", spotifyUser);
    SharedSongInputData inputData = new SharedSongInputData(spotifyUser, group);


    @Test
    void successTest() {

        // Mock
        SpotifyDataAccessObject mockSpotifyDAO = mock(SpotifyDataAccessObject.class);
        GetUsersCurrentlyPlayingTrackRequest mockCurrentlyPlayingRequest =  mock(GetUsersCurrentlyPlayingTrackRequest.class);

        CheckUsersSavedTracksRequest mockCheckSavedRequest0 = mock(CheckUsersSavedTracksRequest.class);
        CheckUsersSavedTracksRequest mockCheckSavedRequest1 = mock(CheckUsersSavedTracksRequest.class);
        CheckUsersSavedTracksRequest mockCheckSavedRequest2 = mock(CheckUsersSavedTracksRequest.class);

        CurrentlyPlaying mockCurrentlyPlaying = mock(CurrentlyPlaying.class);
        List<SpotifyUser> mockUserList = new ArrayList<>();
        mockUserList.add(spotifyUser);
        mockUserList.add(member1);
        mockUserList.add(member2);

        String trackId = "track1";

        when(mockSpotifyDAO
                .getSpotifyApiForUser(spotifyUser)
                .getUsersCurrentlyPlayingTrack()
                .build()
        ).thenReturn(mockCurrentlyPlayingRequest);

        when(mockSpotifyDAO
                .getSpotifyApiForUser(spotifyUser)
                .checkUsersSavedTracks(new String[]{trackId})
                .build()
        ).thenReturn(mockCheckSavedRequest0);

        when(mockSpotifyDAO
                .getSpotifyApiForUser(member1)
                .checkUsersSavedTracks(new String[]{trackId})
                .build()
        ).thenReturn(mockCheckSavedRequest1);

        when(mockSpotifyDAO
                .getSpotifyApiForUser(member2)
                .checkUsersSavedTracks(new String[]{trackId})
                .build()
        ).thenReturn(mockCheckSavedRequest2);

        try {
            //mock calls to execute and get currently playing info
            when(mockCurrentlyPlayingRequest.execute()).thenReturn(mockCurrentlyPlaying);
            when(mockCurrentlyPlaying.getIs_playing()).thenReturn(true);
            when(mockCurrentlyPlaying.getCurrentlyPlayingType().getType()).thenReturn("track");
            when(mockCurrentlyPlaying.getItem().getId()).thenReturn(trackId);

            // current group doesnt work
            when(inputData.getListOfMembers()).thenReturn(mockUserList);

            // mock calls to execute check if song is saved for map builder
            when(mockCheckSavedRequest0.execute()).thenReturn(new Boolean[]{true});
            when(mockCheckSavedRequest1.execute()).thenReturn(new Boolean[]{false});
            when(mockCheckSavedRequest2.execute()).thenReturn(new Boolean[]{false});

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println(e.getMessage());
        }

        SharedSongOutputBoundary presenter = new SharedSongOutputBoundary() {
            @Override
            public void prepareSuccessView(SharedSongOutputData outputData) {
                assertEquals( "Yes", outputData.getSharedSongOutputData().get("heath"));
                assertEquals("No", outputData.getSharedSongOutputData().get("user1"));
                assertEquals("Yes", outputData.getSharedSongOutputData().get("user2"));

            }

            @Override
            public void prepareFailureView(String errorMessage) {
                System.out.println(errorMessage);
            }
        };

        //make interactor and execute
        SharedSongInputBoundary interactor = new SharedSongInteractor(presenter, mockSpotifyDAO);
        interactor.execute(inputData, mockSpotifyDAO);

    }

}
