package use_case.sharedsong;

import data_access.SpotifyDataAccessObject;
import entity.SpotifyUser;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.requests.data.library.CheckUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SharedSongInteractor implements SharedSongInputBoundary {

    private final SharedSongOutputBoundary presenter;
    private final SpotifyDataAccessObject spotifyDAO;

    public SharedSongInteractor(SharedSongOutputBoundary presenter,
                                SpotifyDataAccessObject spotifyDAO) {
        this.presenter = presenter;
        this.spotifyDAO = spotifyDAO;
    }

    @Override
    public void execute(SharedSongInputData inputData) {

        SpotifyUser mainUser = inputData.getUser();

        // Filter real users
        List<SpotifyUser> realUsers = inputData.getListOfMembers()
                .stream()
                .filter(u -> u != null && u.getAccessToken() != null)
                .collect(Collectors.toList());

        if (realUsers.isEmpty()) {
            presenter.prepareFailureView("No valid group members.");
            return;
        }

        try {
            // Use existing access token - do NOT refresh
            GetUsersCurrentlyPlayingTrackRequest req =
                    spotifyDAO.getSpotifyApiForUser(mainUser)
                            .getUsersCurrentlyPlayingTrack()
                            .build();

            CurrentlyPlaying response = req.execute();

            if (response == null) {
                presenter.prepareFailureView("Spotify returned no playback — open Spotify and play a song.");
                return;
            }

            if (response.getIs_playing() == null || !response.getIs_playing()) {
                presenter.prepareFailureView("You must be actively playing a song.");
                return;
            }

            if (response.getItem() == null) {
                presenter.prepareFailureView("No track detected — try switching songs.");
                return;
            }

            if (!"track".equals(response.getCurrentlyPlayingType().getType())) {
                presenter.prepareFailureView("Only track playback is supported.");
                return;
            }

            String trackId = response.getItem().getId();

            // Compare with other group members
            Map<String, String> results = new HashMap<>();
            boolean anyoneShares = false;

            for (SpotifyUser member : realUsers) {

                Boolean[] resp = spotifyDAO
                        .getSpotifyApiForUser(member)
                        .checkUsersSavedTracks(new String[]{trackId})
                        .build()
                        .execute();

                if (resp[0]) {
                    results.put(member.getUsername(), "Yes");
                    anyoneShares = true;
                } else {
                    results.put(member.getUsername(), "No");
                }
            }

            if (!anyoneShares) {
                presenter.prepareFailureView("No one shares your song :(");
            } else {
                presenter.prepareSuccessView(new SharedSongOutputData(results));
            }

        } catch (SpotifyWebApiException e) {
            presenter.prepareFailureView("Spotify permissions missing — reconnect Spotify.");
        } catch (IOException | ParseException e) {
            presenter.prepareFailureView("Error: " + e.getMessage());
        }
    }
}