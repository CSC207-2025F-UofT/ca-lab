//package use_case.manage_application;
//
///**
// * Input Boundary for actions which are related to logging in.
// */
//
///// // DONE //////
//public interface ManageApplicationInputBoundary {
//    // note that this is an interface so there isn't implementation of methods
//
//    /**
//     * Executes the manage application use case.
//     */
//
//    void execute(); // doesn't require input from user
//
//    // when clicked on an application:
////    /**
////     * Executes the switch to accept/reject view use case.
////     */
////    void switchToProcessView();?
//}


package use_case.manage_application;

public interface ManageApplicationInputBoundary {
    void loadApplications();
}
