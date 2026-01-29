package use_case.news;

/**
 * Interface implemented by the presenter
 */
public interface NewsOutputBoundary {

    void prepareSuccessView(NewsResponseModel responseModel);

    void prepareFailView(String errorMessage);
}
