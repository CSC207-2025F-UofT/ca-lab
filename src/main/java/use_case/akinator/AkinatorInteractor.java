package use_case.akinator;

import data_access.PokeApiGateway;
import entity.PokemonTrait;
import entity.SimplePokemonProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * The interactor for the Akinator use case.
 */
public class AkinatorInteractor implements AkinatorInputBoundary {
    private static final int MAX_QUESTIONS = 12;
    private static final int CONFIDENT_GUESS_THRESHOLD = 3;

    private final AkinatorOutputBoundary presenter;
    private final PokeApiGateway apiGateway;
    private final List<SimplePokemonProfile> knowledgeBase = new ArrayList<>();
    private final List<Question> questionBank;

    private List<SimplePokemonProfile> candidates;
    private EnumSet<PokemonTrait> askedTraits;
    private Question currentQuestion;
    private int questionsAsked;
    private boolean awaitingGuess;
    private boolean awaitingReveal;
    private boolean roundActive;
    private int revealPromptId;

    public AkinatorInteractor(AkinatorOutputBoundary presenter,
                              PokeApiGateway apiGateway,
                              List<SimplePokemonProfile> initialProfiles) {
        this.presenter = presenter;
        this.apiGateway = apiGateway;
        this.questionBank = buildQuestions();
        if (initialProfiles != null && !initialProfiles.isEmpty()) {
            knowledgeBase.addAll(initialProfiles);
        } else {
            seedDefaultKnowledgeBase();
        }
        resetRoundState();
        emitIdle();
    }

    /**
     * Starts a new Akinator game. If a game is currently ongoing, prompts the user to
     * finish or reset the current round.
     */
    @Override
    public void start() {
        if (roundActive) {
            presenter.presentError("Finish or reset the current round first.");
            return;
        }
        emitQuestion("Think of a Pokémon and answer the questions.");
    }

    /**
     * Resets the current game.
     */
    @Override
    public void reset() {
        resetRoundState();
        emitIdle();
    }

    /**
     * Handles the user's "yes" response.
     */
    @Override
    public void answerYes() {
        handleAnswer(Boolean.TRUE);
    }

    /**
     * Handles the user's "no" response.
     */
    @Override
    public void answerNo() {
        handleAnswer(Boolean.FALSE);
    }

    /**
     * Handles the user's "don't know" response.
     */
    @Override
    public void answerUnknown() {
        handleAnswer(null);
    }

    /**
     * Allows the user to confirm if the guess was correct, and provides a response.
     */
    @Override
    public void confirmGuess(boolean correct) {
        if (!awaitingGuess || candidates.isEmpty()) {
            presenter.presentError("No guess to confirm yet.");
            return;
        }
        SimplePokemonProfile guess = candidates.get(0);
        awaitingGuess = false;
        if (correct) {
            finishRound(
                    "Awesome! It was " + capitalize(guess.getName()) + ".",
                    "Press Start to play again.",
                    guess.getName());
        } else {
            candidates.remove(0);
            if (candidates.isEmpty()) {
                requestReveal("Alright, help me out?");
            } else if (candidates.size() <= CONFIDENT_GUESS_THRESHOLD
                    || questionsAsked >= MAX_QUESTIONS) {
                emitGuess("Let me try another guess!");
            } else {
                emitQuestion("Okay, another clue then.");
            }
        }
    }

    /**
     * Allows the user to reveal the Pokemon they were thinking of.
     */
    @Override
    public void revealPokemon(String pokemonName) {
        if (!awaitingReveal) {
            presenter.presentError("I’m still narrowing it down.");
            return;
        }
        awaitingReveal = false;
        String cleaned = pokemonName == null ? "" : pokemonName.trim();
        if (cleaned.isEmpty()) {
            finishRound(
                    "Thanks for playing!",
                    "Press Start to try another round.",
                    null);
        } else {
            finishRound(
                    "Got it! I’ll remember " + capitalize(cleaned) + " for next time.",
                    "Press Start when you’re ready for another round.",
                    cleaned);
        }
    }

    private void handleAnswer(Boolean answerYes) {
        if (!roundActive || currentQuestion == null) {
            presenter.presentError("Press Start to get the first question.");
            return;
        }
        if (awaitingGuess) {
            presenter.presentError("Answer the guess first.");
            return;
        }
        if (awaitingReveal) {
            presenter.presentError("Tell me the Pokémon before we continue.");
            return;
        }

        Question answered = currentQuestion;
        currentQuestion = null;

        if (answerYes != null) {
            candidates = filterByTrait(candidates, answered.trait(), answerYes);
        }

        if (candidates.isEmpty()) {
            requestReveal("Those clues don’t match anything I know. What was it?");
            return;
        }

        if (candidates.size() <= CONFIDENT_GUESS_THRESHOLD) {
            emitGuess(candidates.size() == 1
                    ? "I think I figured it out!"
                    : "Let me take a shot at it.");
            return;
        }

        if (questionsAsked >= MAX_QUESTIONS) {
            emitGuess("That’s my last question. Here’s my best guess!");
            return;
        }

        emitQuestion(answerYes == null
                ? "No worries, here’s another clue."
                : "Got it, next clue:");
    }

    private void emitQuestion(String status) {
        Question next = pickNextQuestion();
        if (next == null) {
            emitGuess("I’m out of good questions. Let me guess instead!");
            return;
        }

        currentQuestion = next;
        askedTraits.add(next.trait());
        roundActive = true;
        awaitingGuess = false;
        awaitingReveal = false;
        questionsAsked++;

        presenter.present(new AkinatorOutputData(
                AkinatorOutputData.Step.QUESTION,
                next.text(),
                status,
                false,
                false,
                true,
                null,
                questionsAsked,
                MAX_QUESTIONS,
                revealPromptId));
    }

    private void emitGuess(String status) {
        if (candidates.isEmpty()) {
            requestReveal("I lost track of the candidates. What was it?");
            return;
        }
        awaitingGuess = true;
        roundActive = true;
        awaitingReveal = false;
        String name = candidates.get(0).getName();

        presenter.present(new AkinatorOutputData(
                AkinatorOutputData.Step.GUESS,
                "Is your Pokémon " + capitalize(name) + "?",
                status,
                true,
                false,
                true,
                fetchInfo(name),
                questionsAsked,
                MAX_QUESTIONS,
                revealPromptId));
    }

    private void requestReveal(String status) {
        awaitingReveal = true;
        awaitingGuess = false;
        roundActive = true;
        revealPromptId++;
        presenter.present(new AkinatorOutputData(
                AkinatorOutputData.Step.REVEAL_REQUEST,
                "I’m stumped!",
                status,
                false,
                true,
                true,
                null,
                questionsAsked,
                MAX_QUESTIONS,
                revealPromptId));
    }

    private void finishRound(String prompt, String status, String pokemonName) {
        PokeApiGateway.PokemonApiInfo info =
                pokemonName == null || pokemonName.isBlank() ? null : fetchInfo(pokemonName);
        roundActive = false;
        presenter.present(new AkinatorOutputData(
                AkinatorOutputData.Step.FINISHED,
                prompt,
                status,
                false,
                false,
                false,
                info,
                questionsAsked,
                MAX_QUESTIONS,
                revealPromptId));
        resetRoundState();
    }

    private void emitIdle() {
        presenter.present(new AkinatorOutputData(
                AkinatorOutputData.Step.IDLE,
                "Press Start to begin.",
                "",
                false,
                false,
                false,
                null,
                questionsAsked,
                MAX_QUESTIONS,
                revealPromptId));
    }

    private void resetRoundState() {
        candidates = new ArrayList<>(knowledgeBase);
        askedTraits = EnumSet.noneOf(PokemonTrait.class);
        currentQuestion = null;
        questionsAsked = 0;
        awaitingGuess = false;
        awaitingReveal = false;
        roundActive = false;
    }

    private List<SimplePokemonProfile> filterByTrait(List<SimplePokemonProfile> source,
                                                     PokemonTrait trait,
                                                     boolean expected) {
        List<SimplePokemonProfile> filtered = new ArrayList<>();
        for (SimplePokemonProfile profile : source) {
            if (profile.hasTrait(trait) == expected) {
                filtered.add(profile);
            }
        }
        return filtered.isEmpty() ? source : filtered;
    }

    private Question pickNextQuestion() {
        Question best = null;
        int bestScore = Integer.MAX_VALUE;
        for (Question candidateQuestion : questionBank) {
            if (askedTraits.contains(candidateQuestion.trait())) {
                continue;
            }
            int yesCount = countTraitMatches(candidateQuestion.trait());
            int noCount = candidates.size() - yesCount;
            if (yesCount == 0 || noCount == 0) {
                continue;
            }
            int score = Math.abs(yesCount - noCount);
            if (score < bestScore) {
                bestScore = score;
                best = candidateQuestion;
            }
        }

        if (best != null) {
            return best;
        }

        for (Question fallback : questionBank) {
            if (!askedTraits.contains(fallback.trait())) {
                return fallback;
            }
        }
        return null;
    }

    private int countTraitMatches(PokemonTrait trait) {
        int count = 0;
        for (SimplePokemonProfile profile : candidates) {
            if (profile.hasTrait(trait)) {
                count++;
            }
        }
        return count;
    }

    private PokeApiGateway.PokemonApiInfo fetchInfo(String name) {
        try {
            return apiGateway.fetchPokemon(name);
        } catch (IOException e) {
            presenter.presentError("Couldn’t reach PokéAPI, showing text only.");
            return null;
        }
    }

    private void seedDefaultKnowledgeBase() {
        knowledgeBase.add(SimplePokemonProfile.of("pikachu",
                PokemonTrait.CUTE_MASCOT, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("charizard",
                PokemonTrait.STARTER, PokemonTrait.DUAL_TYPE, PokemonTrait.FLYING_OR_FLOATING,
                PokemonTrait.FULLY_EVOLVED, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("blastoise",
                PokemonTrait.STARTER, PokemonTrait.AQUATIC,
                PokemonTrait.FULLY_EVOLVED, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("venusaur",
                PokemonTrait.STARTER, PokemonTrait.DUAL_TYPE,
                PokemonTrait.FULLY_EVOLVED, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("snorlax",
                PokemonTrait.FULLY_EVOLVED, PokemonTrait.DEFENSIVE_TANK, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("mewtwo",
                PokemonTrait.LEGENDARY, PokemonTrait.HUMANOID,
                PokemonTrait.FULLY_EVOLVED, PokemonTrait.KANTO_ORIGINAL, PokemonTrait.PSYCHIC_TYPE));
        knowledgeBase.add(SimplePokemonProfile.of("gengar",
                PokemonTrait.DUAL_TYPE, PokemonTrait.SPOOKY,
                PokemonTrait.FULLY_EVOLVED, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("dragonite",
                PokemonTrait.DUAL_TYPE, PokemonTrait.FLYING_OR_FLOATING,
                PokemonTrait.FULLY_EVOLVED, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("lapras",
                PokemonTrait.DUAL_TYPE, PokemonTrait.AQUATIC,
                PokemonTrait.FULLY_EVOLVED, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("machamp",
                PokemonTrait.HUMANOID, PokemonTrait.FULLY_EVOLVED, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("jigglypuff",
                PokemonTrait.CUTE_MASCOT, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("bulbasaur",
                PokemonTrait.STARTER, PokemonTrait.DUAL_TYPE,
                PokemonTrait.CUTE_MASCOT, PokemonTrait.KANTO_ORIGINAL));
        knowledgeBase.add(SimplePokemonProfile.of("squirtle",
                PokemonTrait.STARTER, PokemonTrait.AQUATIC,
                PokemonTrait.CUTE_MASCOT, PokemonTrait.KANTO_ORIGINAL));
    }

    private List<Question> buildQuestions() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("Is your Pokémon one of the starter Pokémon?", PokemonTrait.STARTER));
        list.add(new Question("Does it have more than one type?", PokemonTrait.DUAL_TYPE));
        list.add(new Question("Is it legendary or mythical?", PokemonTrait.LEGENDARY));
        list.add(new Question("Is it fully evolved?", PokemonTrait.FULLY_EVOLVED));
        list.add(new Question("Can it fly or levitate?", PokemonTrait.FLYING_OR_FLOATING));
        list.add(new Question("Is it primarily water-based?", PokemonTrait.AQUATIC));
        list.add(new Question("Does it have a humanoid appearance?", PokemonTrait.HUMANOID));
        list.add(new Question("Is it ghostly or spooky?", PokemonTrait.SPOOKY));
        list.add(new Question("Is it known for being cute or mascot-like?", PokemonTrait.CUTE_MASCOT));
        list.add(new Question("Is it a Psychic-type Pokémon?", PokemonTrait.PSYCHIC_TYPE));
        list.add(new Question("Is it known for being a defensive tank?", PokemonTrait.DEFENSIVE_TANK));
        return list;
    }

    private String capitalize(String text) {
        if (text == null || text.isBlank()) return "";
        String lower = text.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    private static final class Question {
        private final String text;
        private final PokemonTrait trait;

        private Question(String text, PokemonTrait trait) {
            this.text = text;
            this.trait = trait;
        }

        public String text() {
            return text;
        }

        public PokemonTrait trait() {
            return trait;
        }
    }
}
