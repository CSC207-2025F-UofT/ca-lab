package use_case.submit_application;

import data_access.InMemoryApplicationDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entity.User;
import entity.UserFactory;
import entity.Visitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubmitInteractorTest {

    @Test
    void successTest() {
        SubmitUserDataAccessInterface  userRepository = new InMemoryUserDataAccessObject();
        SubmitApplicationDataAccessInterface  applicationRepository = new InMemoryApplicationDataAccessObject();

        //set the info making sure test would not fail in second branch
        UserFactory factory = new UserFactory();
        User user = factory.create("visitor", "rebecca", "12345");
        userRepository.save(user);

        SubmitInputData inputData = new SubmitInputData("0111", "rebecca",
                "rebecca", "L", "29", "teacher",
                "Fifth Avenue", "good", "123456789", "1234@gamil.com",
                "love pets", "no experience", "everyday");

        //set the same current username making sure test would not fail in third branch
        ((InMemoryUserDataAccessObject) userRepository).setCurrentUsername("rebecca");

        SubmitOutputBoundary successPresenter = new SubmitOutputBoundary() {
            @Override
            public void prepareSuccessView(String successMessage){
                assertEquals("Your application has been successfully submitted. " +
                        "We will review it and contact you shortly. Thank you!", successMessage);
                assertEquals(1, ((InMemoryApplicationDataAccessObject)applicationRepository).getAllApplications().size());
            }

            @Override
            public void prepareFailView(String errorMessage){
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareAutofillView(SubmitOutputData submitOutputData) {
                fail("Use case autofill is unexpected.");
            }

            @Override
            public void switchToBrowserFilterView(){
                fail("Use case switched to other view is unexpected.");
            }
        };

        SubmitInputBoundary interactor = new SubmitInteractor(userRepository, applicationRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureMissingInfoTest() {
        SubmitInputData inputData = new SubmitInputData("0111", "rebecca123",
                "rebecca", "L", "29", "teacher",
                "Fifth Avenue", "good", "123456789", "1234@gamil.com",
                "love pets", "no experience", "");
        SubmitUserDataAccessInterface  userRepository = new InMemoryUserDataAccessObject();
        SubmitApplicationDataAccessInterface applicationRepository = new InMemoryApplicationDataAccessObject();

        SubmitOutputBoundary failurePresenter = new SubmitOutputBoundary() {

            @Override
            public void prepareSuccessView(String successMessage){
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage){
                assertEquals("Some required fields are missing.", errorMessage);
            }

            @Override
            public void prepareAutofillView(SubmitOutputData submitOutputData) {
                fail("Use case autofill is unexpected.");
            }

            @Override
            public void switchToBrowserFilterView(){
                fail("Use case switched to other view is unexpected.");
            }
        };

        SubmitInputBoundary interactor = new SubmitInteractor(userRepository, applicationRepository, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureUsernameNotFoundSubmitTest() {
        SubmitInputData inputData = new SubmitInputData("0111", "rebecca123",
                "rebecca", "L", "29", "teacher",
                "Fifth Avenue", "good", "123456789", "1234@gamil.com",
                "love pets", "no experience", "everyday");
        SubmitUserDataAccessInterface  userRepository = new InMemoryUserDataAccessObject();
        SubmitApplicationDataAccessInterface  applicationRepository = new InMemoryApplicationDataAccessObject();

        SubmitOutputBoundary failurePresenter = new SubmitOutputBoundary() {

            @Override
            public void prepareSuccessView(String successMessage){
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage){
                assertEquals("Username not found", errorMessage);
            }

            @Override
            public void prepareAutofillView(SubmitOutputData submitOutputData) {
                fail("Use case autofill is unexpected.");
            }

            @Override
            public void switchToBrowserFilterView(){
                fail("Use case switched to other view is unexpected.");
            }
        };

        SubmitInputBoundary interactor = new SubmitInteractor(userRepository, applicationRepository, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureUsernameMismatchTest() {
        SubmitUserDataAccessInterface  userRepository = new InMemoryUserDataAccessObject();
        SubmitApplicationDataAccessInterface  applicationRepository = new InMemoryApplicationDataAccessObject();

        //set the info making sure test would not fail in second branch
        UserFactory factory = new UserFactory();
        User user = factory.create("visitor", "rebecca", "12345");
        userRepository.save(user);

        SubmitInputData inputData = new SubmitInputData("0111", "rebecca",
                "rebecca", "L", "29", "teacher",
                "Fifth Avenue", "good", "123456789", "1234@gamil.com",
                "love pets", "no experience", "everyday");

        //set a different current username to fail
        ((InMemoryUserDataAccessObject) userRepository).setCurrentUsername("Ivy");

        SubmitOutputBoundary failurePresenter = new SubmitOutputBoundary() {

            @Override
            public void prepareSuccessView(String successMessage){
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage){
                assertEquals("Username does not match.", errorMessage);
            }

            @Override
            public void prepareAutofillView(SubmitOutputData submitOutputData) {
                fail("Use case autofill is unexpected.");
            }

            @Override
            public void switchToBrowserFilterView(){
                fail("Use case switched to other view is unexpected.");
            }
        };

        SubmitInputBoundary interactor = new SubmitInteractor(userRepository, applicationRepository, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void switchToBrowserFilterViewTest(){
        SubmitUserDataAccessInterface  userRepository = new InMemoryUserDataAccessObject();
        SubmitApplicationDataAccessInterface  applicationRepository = new InMemoryApplicationDataAccessObject();
        final boolean[] called = {false};

        SubmitOutputBoundary failurePresenter = new SubmitOutputBoundary() {

            @Override
            public void prepareSuccessView(String successMessage){
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage){
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareAutofillView(SubmitOutputData submitOutputData) {
                fail("Use case autofill is unexpected.");
            }

            @Override
            public void switchToBrowserFilterView(){
                called[0] = true;
            }
        };

        SubmitInputBoundary interactor = new SubmitInteractor(userRepository, applicationRepository, failurePresenter);
        interactor.switchToBrowserFilterView();
        assertTrue(called[0]);
    }

    @Test
    void autofillSuccessTest(){
        SubmitUserDataAccessInterface  userRepository = new InMemoryUserDataAccessObject();
        SubmitApplicationDataAccessInterface  applicationRepository = new InMemoryApplicationDataAccessObject();
        final boolean[] called = {false};

        UserFactory factory = new UserFactory();
        User user = factory.create("visitor", "rebecca", "12345");
        Visitor visitor = (Visitor) user;
        visitor.setFirstName("Rebecca");
        visitor.setLastName("L");
        visitor.setAge("33");
        visitor.setOccupation("teacher");
        visitor.setAddress("FIFTH AVENUE");
        visitor.setHomeEvi("good");
        visitor.setTel("123456789");
        visitor.setEmail("visitor@gmail.com");
        userRepository.save(visitor);
        ((InMemoryUserDataAccessObject) userRepository).setCurrentUsername("rebecca");

        SubmitOutputBoundary autofillPresenter = new SubmitOutputBoundary() {
            @Override
            public void prepareSuccessView(String successMessage){
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage){
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareAutofillView(SubmitOutputData submitOutputData) {
                called[0] = true;

                assertEquals("rebecca",submitOutputData.getUsername());
                assertEquals("Rebecca",submitOutputData.getFirstname());
                assertEquals("L",submitOutputData.getLastname());
                assertEquals("33",submitOutputData.getAge());
                assertEquals("teacher",submitOutputData.getOccupation());
                assertEquals("FIFTH AVENUE",submitOutputData.getAddress());
                assertEquals("good",submitOutputData.getHomeEvi());
                assertEquals("123456789",submitOutputData.getTel());
                assertEquals("visitor@gmail.com",submitOutputData.getEmail());
            }

            @Override
            public void switchToBrowserFilterView(){
                fail("Use case switched to other view is unexpected.");
            }
        };
        SubmitInputBoundary interactor = new SubmitInteractor(userRepository, applicationRepository, autofillPresenter);
        interactor.autofill();
        assertTrue(called[0]);
    }
}
