package use_case.daily_mix;

public interface DailyMixOutputBoundary {
    void prepareSuccessView(DailyMixOutputData response);
    void prepareFailView(String error);
}

