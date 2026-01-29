package use_case.submit_application;

import entity.Application;
import entity.User;
import entity.Visitor;

public class SubmitInteractor implements SubmitInputBoundary {
    private final SubmitUserDataAccessInterface userDataAccessObject;
    private final SubmitApplicationDataAccessInterface applicationDataAccessObject;
    private final SubmitOutputBoundary submitPresenter;

    public SubmitInteractor(SubmitUserDataAccessInterface userDataAccessInterface,
                            SubmitApplicationDataAccessInterface applicationDataAccessInterface,
                            SubmitOutputBoundary submitOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.applicationDataAccessObject = applicationDataAccessInterface;
        this.submitPresenter = submitOutputBoundary;
    }

    @Override
    public void execute(SubmitInputData submitInputData) {
        //notify incomplete form
        if ("".equals(submitInputData.getUsername())||
                "".equals(submitInputData.getFirstname())||
                "".equals(submitInputData.getLastname())||
                "".equals(submitInputData.getAge())||
                "".equals(submitInputData.getOccupation())||
                "".equals(submitInputData.getAddress())||
                "".equals(submitInputData.getHomeEvi())||
                "".equals(submitInputData.getTel())||
                "".equals(submitInputData.getEmail())||
                "".equals(submitInputData.getReason())||
                "".equals(submitInputData.getPrevExp())||
                "".equals(submitInputData.getAvailability())) {
            submitPresenter.prepareFailView("Some required fields are missing.");
        }
        //notify typo in username (everyone should have a username and personal account in this stage)
        else if (! userDataAccessObject.existsByName(submitInputData.getUsername())){
            submitPresenter.prepareFailView("Username not found");
        }
        //prevent using others' account info to make application
        //notify that the username entered does not match the current logged-in account
        else if (! submitInputData.getUsername().equals(userDataAccessObject.getCurrentUsername())){
            submitPresenter.prepareFailView("Username does not match.");
        }
        else {
            final Visitor visitor = (Visitor)userDataAccessObject.get(submitInputData.getUsername());
            //store or update visitor additional info for future use, like
            //upgrading the submit view: when user who applied before and had the information in database,
            // we can automatically fill in the blanks of application form and show to visitor
            visitor.setFirstName(submitInputData.getFirstname());
            visitor.setLastName(submitInputData.getLastname());
            visitor.setAge(submitInputData.getAge());
            visitor.setOccupation(submitInputData.getOccupation());
            visitor.setAddress(submitInputData.getAddress());
            visitor.setHomeEvi(submitInputData.getHomeEvi());
            visitor.setEmail(submitInputData.getEmail());
            visitor.setTel(submitInputData.getTel());
            userDataAccessObject.save(visitor);
            //create an application and save it into database
            System.out.println("Pet_id in submit interactor: " + submitInputData.getPetId());
            final Application application = new Application(submitInputData.getPetId(),
                    visitor.getFirstName(), visitor.getLastName(), visitor.getEmail(), visitor.getTel(),
                    visitor.getAge(), visitor.getOccupation(), submitInputData.getReason(), visitor.getHomeEvi(),
                    submitInputData.getAvailability(), submitInputData.getPrevExp());
            applicationDataAccessObject.save(application);
            //notify user applying successfully
            submitPresenter.prepareSuccessView(
                    "Your application has been successfully submitted. " +
                            "We will review it and contact you shortly. Thank you!");
        }
    }

    @Override
    public void switchToBrowserFilterView() {
        submitPresenter.switchToBrowserFilterView();
    }

    @Override
    public void autofill() {
        String username = userDataAccessObject.getCurrentUsername();
        if (username == null || username.isEmpty()) {
            return;
        }
        User user = userDataAccessObject.get(username);
        if (!(user instanceof Visitor)) {
            return;
        }
        Visitor visitor = (Visitor) user;
        SubmitOutputData outputData = new SubmitOutputData(
                visitor.getName(),
                visitor.getFirstName(),
                visitor.getLastName(),
                visitor.getAge(),
                visitor.getOccupation(),
                visitor.getAddress(),
                visitor.getHomeEvi(),
                visitor.getTel(),
                visitor.getEmail()
                );
        submitPresenter.prepareAutofillView(outputData);
    }
}
