package use_case.akinator;
import data_access.PokeApiGateway;

/**
 * The output data for the Akinator use case.
 */
public class AkinatorOutputData {
    public enum Step {IDLE, QUESTION, GUESS, REVEAL_REQUEST, FINISHED}

    private final Step step;
    private final String prompt;
    private final String status;
    private final boolean awaitingGuess;
    private final boolean awaitingReveal;
    private final boolean roundActive;
    private final PokeApiGateway.PokemonApiInfo guessInfo;
    private final int questionsAsked;
    private final int questionLimit;
    private final int revealPromptId;

    public AkinatorOutputData(Step step,
                              String prompt,
                              String status,
                              boolean awaitingGuess,
                              boolean awaitingReveal,
                              boolean roundActive,
                              PokeApiGateway.PokemonApiInfo guessInfo,
                              int questionsAsked,
                              int questionLimit,
                              int revealPromptId) {
        this.step = step;
        this.prompt = prompt;
        this.status = status;
        this.awaitingGuess = awaitingGuess;
        this.awaitingReveal = awaitingReveal;
        this.roundActive = roundActive;
        this.guessInfo = guessInfo;
        this.questionsAsked = questionsAsked;
        this.questionLimit = questionLimit;
        this.revealPromptId = revealPromptId;
    }

    public Step getStep() {
        return step;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAwaitingGuess() {
        return awaitingGuess;
    }

    public boolean isAwaitingReveal() {
        return awaitingReveal;
    }

    public boolean isRoundActive() {
        return roundActive;
    }

    public PokeApiGateway.PokemonApiInfo getGuessInfo() {
        return guessInfo;
    }

    public int getQuestionsAsked() {
        return questionsAsked;
    }

    public int getQuestionLimit() {
        return questionLimit;
    }

    public int getRevealPromptId() {
        return revealPromptId;
    }
}
