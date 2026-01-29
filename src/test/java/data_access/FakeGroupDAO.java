package data_access;

import entity.Group;
import entity.SpotifyUser;
import use_case.GroupDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class FakeGroupDAO implements GroupDataAccessInterface {

    public List<Group> savedGroups = new ArrayList<>();

    @Override
    public void saveGroup(Group group) {
        savedGroups.add(group);
    }

    @Override
    public Group getGroupByCode(String code) { return null; }

    @Override
    public boolean existsByName(String name) { return false; }

    @Override
    public boolean joinGroup(String code, SpotifyUser user) { return false; }

    @Override
    public boolean existsByCode(String code) { return false; }

    @Override
    public void updateGroup(Group group) { }
}