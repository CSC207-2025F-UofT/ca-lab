package use_case.back;

/**
 * Access boundary for the Access Settings use case.
 */
public interface BackOutputBoundary {
    /**
     * Prepares the view the user should see after successfully requesting
     * to access the Settings screen.
     */
    void prepareSuccessView();
}
