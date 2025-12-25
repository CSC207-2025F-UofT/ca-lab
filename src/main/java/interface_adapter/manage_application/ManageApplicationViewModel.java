package interface_adapter.manage_application;

import entity.Application;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class ManageApplicationViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private List<Application> applications;

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
        support.firePropertyChange("applications", null, applications);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public String getViewName() {
        return "Manage Application";
    }
}
