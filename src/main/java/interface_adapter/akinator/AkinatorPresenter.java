package interface_adapter.akinator;

import use_case.akinator.AkinatorOutputBoundary;
import use_case.akinator.AkinatorOutputData;

/**
 * The presenter for the Akinator use case.
 */
public class AkinatorPresenter implements AkinatorOutputBoundary{

    private final AkinatorViewModel viewModel;

    public AkinatorPresenter(AkinatorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Sets up output data from the Akinator use case for display.
     *
     * @param outputData output data from the Akinator
     */
    @Override
    public void present(AkinatorOutputData outputData) {
        AkinatorState state = viewModel.getState();
        state.setStep(outputData.getStep());
        state.setPrompt(outputData.getPrompt());
        state.setStatus(outputData.getStatus());
        state.setAwaitingGuess(outputData.isAwaitingGuess());
        state.setAwaitingReveal(outputData.isAwaitingReveal());
        state.setRoundActive(outputData.isRoundActive());
        state.setQuestionsAsked(outputData.getQuestionsAsked());
        state.setQuestionLimit(outputData.getQuestionLimit());
        state.setRevealPromptId(outputData.getRevealPromptId());
        boolean showGuess = outputData.getStep() == AkinatorOutputData.Step.GUESS
                || (outputData.getStep() == AkinatorOutputData.Step.FINISHED
                && outputData.getGuessInfo() != null);
        state.setGuessVisible(showGuess);
        state.setGuessInfo(outputData.getGuessInfo());
        state.setErrorMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    /**
     * Displays an error from the Akinator.
     *
     * @param message the error
     */
    @Override
    public void presentError(String message) {
        AkinatorState state = viewModel.getState();
        state.setErrorMessage(message);
        viewModel.setState(state);
        viewModel.firePropertyChange("error");
    }
}
