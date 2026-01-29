package use_case.filter_search;

/**
 * The output boundary for the Filter Search Use Case.
 */

public interface FilterSearchOutputBoundary {
    /**
     * Prepares the success view for the Filter Search Use Case.
     * @param filterSearchResponse the output data
     */
    void prepareSuccessView(FilterSearchResponse filterSearchResponse);

    void prepareFailView(String s);
}
