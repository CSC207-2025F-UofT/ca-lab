package use_case.manage_application;
//
import entity.Application;
import java.util.List;

public class ManageApplicationOutputData {

    // this is packaging data to send to the presenter to present to users (to View)
    // this ODT should get all the applications and extract all the data needed to be displayed to send to View
    // it's purely just data structure

    private final List<Application> applications;

    public ManageApplicationOutputData(List<Application> applications) { this.applications = applications; }

    public List<Application> getApplications() { return applications; }

    // we don't need to do anything extra cuz really the list of applications given by interactor from DAO
    // is already exactly what we need, so just pass it on to presenter basically
    // so that's why we are also just using List<Application>

    // it's just a container
}

