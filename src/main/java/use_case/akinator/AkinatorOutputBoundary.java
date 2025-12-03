package use_case.akinator;

/**
 * The output boundary for the Akinator use case.
 */
public interface AkinatorOutputBoundary {
    void present(AkinatorOutputData outputData);
    void presentError(String message);
}
