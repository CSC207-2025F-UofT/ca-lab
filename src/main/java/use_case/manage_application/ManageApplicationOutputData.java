//package use_case.manage_application;
//
//public class ManageApplicationOutputData {
//}


package use_case.manage_application;

import java.util.List;

public class ManageApplicationOutputData {

    private final List<ApplicationData> apps;

    public ManageApplicationOutputData(List<ApplicationData> apps) {
        this.apps = apps;
    }

    public List<ApplicationData> getApps() {
        return apps;
    }

    public static class ApplicationData {
        public final String id;
        public final String title;
        public final String status;
        public final String subtitle;

        public ApplicationData(String id, String title, String status, String subtitle) {
            this.id = id;
            this.title = title;
            this.status = status;
            this.subtitle = subtitle;
        }
    }
}

