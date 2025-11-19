package interface_adapter.manage_application;

/**
 * Represents the UI data for a single application card.
 */
public class ManageApplicationCardState {

    private final String applicationId;
    private final String title;
    private final String status;
    private final String subtitle;

    public ManageApplicationCardState(String applicationId,
                                      String title,
                                      String status,
                                      String subtitle) {

        this.applicationId = applicationId;
        this.title = title;
        this.status = status;
        this.subtitle = subtitle;
    }

    public String getApplicationId() { return applicationId; }

    public String getTitle() { return title; }

    public String getStatus() { return status; }

    public String getSubtitle() { return subtitle; }
}
