package use_case.joingroup;

import entity.SpotifyUser;

public class JoinGroupInputData {

    private final String groupCode;
    private final SpotifyUser user;

    public JoinGroupInputData(String groupCode, SpotifyUser user) {
        this.groupCode = groupCode;
        this.user = user;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public SpotifyUser getUser() {
        return user;
    }}