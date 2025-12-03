package interface_adapter.akinator;

import data_access.PokeApiGateway;
import use_case.akinator.AkinatorOutputData;

/**
 * The state for the Akinator use case.
 */
public class AkinatorState {
    private String prompt = "Press Start to begin.";
    private String status = "";
    private boolean guessVisible;
    private boolean awaitingGuess;
    private boolean awaitingReveal;
    private boolean roundActive;
    private PokeApiGateway.PokemonApiInfo guessInfo;
    private String errorMessage;
    private int questionsAsked;
    private int questionLimit;
    private int revealPromptId;
    private AkinatorOutputData.Step step = AkinatorOutputData.Step.IDLE;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isGuessVisible() {
        return guessVisible;
    }

    public void setGuessVisible(boolean guessVisible) {
        this.guessVisible = guessVisible;
    }

    public boolean isAwaitingGuess() {
        return awaitingGuess;
    }

    public void setAwaitingGuess(boolean awaitingGuess) {
        this.awaitingGuess = awaitingGuess;
    }

    public PokeApiGateway.PokemonApiInfo getGuessInfo() {
        return guessInfo;
    }

    public void setGuessInfo(PokeApiGateway.PokemonApiInfo guessInfo) {
        this.guessInfo = guessInfo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getQuestionsAsked() {
        return questionsAsked;
    }

    public void setQuestionsAsked(int questionsAsked) {
        this.questionsAsked = questionsAsked;
    }

    public int getQuestionLimit() {
        return questionLimit;
    }

    public void setQuestionLimit(int questionLimit) {
        this.questionLimit = questionLimit;
    }

    public boolean isAwaitingReveal() {
        return awaitingReveal;
    }

    public void setAwaitingReveal(boolean awaitingReveal) {
        this.awaitingReveal = awaitingReveal;
    }

    public int getRevealPromptId() {
        return revealPromptId;
    }

    public void setRevealPromptId(int revealPromptId) {
        this.revealPromptId = revealPromptId;
    }

    public boolean isRoundActive() {
        return roundActive;
    }

    public void setRoundActive(boolean roundActive) {
        this.roundActive = roundActive;
    }

    public AkinatorOutputData.Step getStep() {
        return step;
    }

    public void setStep(AkinatorOutputData.Step step) {
        this.step = step;
    }
}
