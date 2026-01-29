package use_case.joingroup;


public interface JoinGroupOutputBoundary {
    void prepareSuccessView(JoinGroupOutputData data);
    void prepareFailView(String error);
}