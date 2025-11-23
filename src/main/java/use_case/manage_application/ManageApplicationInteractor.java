//package use_case.manage_application;
//
//import use_case.login.LoginOutputBoundary;
//import use_case.login.LoginUserDataAccessInterface;
//
//public class ManageApplicationInteractor implements ManageApplicationInputBoundary {
//    private final userDataAccessObject;
//    private final ManageApplicationOutputBoundary managePresenter;
//
//    public ManageApplicationInteractor(ManageApplicationDataAccessInterface userDataAccessInterface,
//                           ManageApplicationOutputBoundary manageApplicationOutputBoundary) {
//        this.userDataAccessObject = userDataAccessInterface;
//        this.managePresenter = manageApplicationOutputBoundary;
//    }
//
//
//    // grab information from database
//
//    // do http request, like for login logout
//
//    @Override
//    public void execute() {
//
//
////        final String username = loginInputData.getUsername();
////        final String password = loginInputData.getPassword();
////        if (!userDataAccessObject.existsByName(username)) {
////            loginPresenter.prepareFailView(username + ": Account does not exist.");
////        }
////        else {
////            final String pwd = userDataAccessObject.get(username).getPassword();
////            if (!password.equals(pwd)) {
////                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
////            }
////            else {
////
////                final User user = userDataAccessObject.get(loginInputData.getUsername());
////
////                userDataAccessObject.setCurrentUsername(username);
////
////                final LoginOutputData loginOutputData = new LoginOutputData(user.getName());
//
//        /// you are trying to do your logic here, and then define output data,
//        /// pass it back into the prepare success view method in outputboundary at last
////                loginPresenter.prepareSuccessView(loginOutputData);
////            }
////        }
//    }
//
//}
package use_case.manage_application;

import java.util.ArrayList;
import java.util.List;

public class ManageApplicationInteractor implements ManageApplicationInputBoundary {

    private final ManageApplicationOutputBoundary presenter;

    public ManageApplicationInteractor(ManageApplicationOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadApplications() {

        // Create placeholder applications (fake data)
        List<ManageApplicationOutputData.ApplicationData> fakeList = new ArrayList<>();

        fakeList.add(new ManageApplicationOutputData.ApplicationData(
                "1", "Application 1", "Pending", "This is a placeholder."
        ));

        fakeList.add(new ManageApplicationOutputData.ApplicationData(
                "2", "Application 2", "Approved", "Another placeholder."
        ));

        fakeList.add(new ManageApplicationOutputData.ApplicationData(
                "3", "Application 3", "Rejected", "Yet another placeholder."
        ));

        // Wrap it in output data
        ManageApplicationOutputData outputData =
                new ManageApplicationOutputData(fakeList);

        // Send to presenter → viewmodel → view
        presenter.presentApplications(outputData);
    }
}

