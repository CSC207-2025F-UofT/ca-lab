package use_case.akinator;

import data_access.PokeApiGateway;
import entity.PokemonTrait;
import entity.SimplePokemonProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AkinatorInteractorTest {

    private CapturingPresenter presenter;
    private AkinatorInteractor interactor;

    @BeforeEach
    void setUp() {
        presenter = new CapturingPresenter();
        interactor = new AkinatorInteractor(presenter, new StubGateway(), List.of());
    }

    @Test
    void startEmitsQuestion() {
        interactor.start();
        assertNotNull(presenter.last);
        assertEquals(AkinatorOutputData.Step.QUESTION, presenter.last.getStep());
        assertTrue(presenter.last.isRoundActive());
    }

    @Test
    void startWhileActiveShowsError() {
        interactor.start();
        interactor.start();
        assertEquals("Finish or reset the current round first.", presenter.lastError);
    }

    @Test
    void answeringBeforeStartShowsError() {
        interactor.answerYes();
        assertEquals("Press Start to get the first question.", presenter.lastError);
    }

    @Test
    void answeringYesLeadsToGuessWhenOnlyOneCandidateRemains() throws Exception {
        useSmallKnowledgeBase();
        interactor.start();
        interactor.answerYes();

        assertNotNull(presenter.last);
        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingGuess());
    }

    @Test
    void confirmGuessTrueFinishesRound() throws Exception {
        useSmallKnowledgeBase();
        interactor.start();
        interactor.answerYes();
        interactor.confirmGuess(true);

        assertEquals(AkinatorOutputData.Step.FINISHED, presenter.last.getStep());
        assertFalse(presenter.last.isRoundActive());
    }

    @Test
    void confirmGuessFalseTriggersAnotherGuess() throws Exception {
        useSmallKnowledgeBase();
        interactor.start();
        interactor.answerNo(); // keep multiple candidates
        interactor.confirmGuess(false);

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingGuess());
    }

    @Test
    void revealRequestAcceptsPokemonName() throws Exception {
        useTinyKnowledgeBase();
        interactor.start();
        interactor.answerYes();
        interactor.confirmGuess(false);
        assertEquals(AkinatorOutputData.Step.REVEAL_REQUEST, presenter.last.getStep());

        interactor.revealPokemon("Snorlax");

        assertEquals(AkinatorOutputData.Step.FINISHED, presenter.last.getStep());
        assertFalse(presenter.last.isRoundActive());
    }

    @Test
    void hittingQuestionLimitTriggersGuess() {
        interactor.start();
        for (int i = 0; i < 12 && (presenter.last == null
                || presenter.last.getStep() != AkinatorOutputData.Step.GUESS); i++) {
            interactor.answerUnknown();
        }

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingGuess());
    }

    @Test
    void revealWithoutPromptShowsError() {
        interactor.revealPokemon("pikachu");
        assertEquals("I’m still narrowing it down.", presenter.lastError);
    }

    @Test
    void resetReturnsToIdleState() {
        interactor.start();
        interactor.answerUnknown();

        interactor.reset();

        assertNotNull(presenter.last);
        assertEquals(AkinatorOutputData.Step.IDLE, presenter.last.getStep());
        assertFalse(presenter.last.isRoundActive());
    }

    @Test
    void revealWithBlankNameSkipsApiLookup() throws Exception {
        useTinyKnowledgeBase();
        interactor.start();
        interactor.answerYes();
        interactor.confirmGuess(false);
        assertEquals(AkinatorOutputData.Step.REVEAL_REQUEST, presenter.last.getStep());

        interactor.revealPokemon("   ");

        assertEquals(AkinatorOutputData.Step.FINISHED, presenter.last.getStep());
        assertNull(presenter.last.getGuessInfo());
    }

    @Test
    void startingWithNoUsefulQuestionsFallsBackToGuess() throws Exception {
        Field askedTraitsField = AkinatorInteractor.class.getDeclaredField("askedTraits");
        askedTraitsField.setAccessible(true);
        askedTraitsField.set(interactor, java.util.EnumSet.allOf(PokemonTrait.class));

        interactor.start();

        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingGuess());
        assertEquals("I’m out of good questions. Let me guess instead!", presenter.last.getStatus());
    }

    @Test
    void emptyCandidatesTriggerRevealRequest() throws Exception {
        Field candidatesField = AkinatorInteractor.class.getDeclaredField("candidates");
        candidatesField.setAccessible(true);
        candidatesField.set(interactor, new java.util.ArrayList<>());

        Method emitGuess = AkinatorInteractor.class.getDeclaredMethod("emitGuess", String.class);
        emitGuess.setAccessible(true);
        emitGuess.invoke(interactor, "test");

        assertEquals(AkinatorOutputData.Step.REVEAL_REQUEST, presenter.last.getStep());
        assertTrue(presenter.last.isAwaitingReveal());
    }

    @Test
    void filterByTraitReturnsOriginalWhenFilterEmpty() throws Exception {
        Method filter = AkinatorInteractor.class.getDeclaredMethod("filterByTrait", List.class, PokemonTrait.class, boolean.class);
        filter.setAccessible(true);
        List<SimplePokemonProfile> base = List.of(SimplePokemonProfile.of("ditto", PokemonTrait.SPOOKY));

        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> result = (List<SimplePokemonProfile>) filter.invoke(interactor, base, PokemonTrait.STARTER, true);

        assertSame(base, result);
    }

    @Test
    void capitalizeHandlesNullAndBlank() throws Exception {
        Method capitalize = AkinatorInteractor.class.getDeclaredMethod("capitalize", String.class);
        capitalize.setAccessible(true);

        assertEquals("", capitalize.invoke(interactor, (Object) null));
        assertEquals("", capitalize.invoke(interactor, "   "));
        assertEquals("Bulbasaur", capitalize.invoke(interactor, "bULbASAUR"));
    }

    @Test
    void pokeApiFailureIsReportedButGameContinues() throws Exception {
        presenter = new CapturingPresenter();
        interactor = new AkinatorInteractor(presenter, new FailingGateway(), List.of());
        useSmallKnowledgeBase();

        interactor.start();
        interactor.answerYes();

        assertEquals("Couldn’t reach PokéAPI, showing text only.", presenter.lastError);
        assertEquals(AkinatorOutputData.Step.GUESS, presenter.last.getStep());
    }

    private void useSmallKnowledgeBase() throws Exception {
        Field kbField = AkinatorInteractor.class.getDeclaredField("knowledgeBase");
        kbField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> base =
                (List<SimplePokemonProfile>) kbField.get(interactor);
        base.clear();
        base.addAll(Arrays.asList(
                SimplePokemonProfile.of("pikachu", PokemonTrait.CUTE_MASCOT),
                SimplePokemonProfile.of("eevee", PokemonTrait.CUTE_MASCOT),
                SimplePokemonProfile.of("charmander", PokemonTrait.STARTER)
        ));
        Method reset = AkinatorInteractor.class.getDeclaredMethod("resetRoundState");
        reset.setAccessible(true);
        reset.invoke(interactor);
    }

    private void useTinyKnowledgeBase() throws Exception {
        Field kbField = AkinatorInteractor.class.getDeclaredField("knowledgeBase");
        kbField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SimplePokemonProfile> base =
                (List<SimplePokemonProfile>) kbField.get(interactor);
        base.clear();
        base.add(SimplePokemonProfile.of("snorlax", PokemonTrait.DEFENSIVE_TANK));
        Method reset = AkinatorInteractor.class.getDeclaredMethod("resetRoundState");
        reset.setAccessible(true);
        reset.invoke(interactor);
    }

    private static final class CapturingPresenter implements AkinatorOutputBoundary {
        private AkinatorOutputData last;
        private String lastError;

        @Override
        public void present(AkinatorOutputData outputData) {
            this.last = outputData;
        }

        @Override
        public void presentError(String message) {
            this.lastError = message;
        }
    }

    private static final class StubGateway extends PokeApiGateway {
        @Override
        public PokemonApiInfo fetchPokemon(String name) {
            return new PokemonApiInfo(
                    name,
                    null,
                    List.of("Test"),
                    1.0,
                    9.0
            );
        }
    }

    private static final class FailingGateway extends PokeApiGateway {
        @Override
        public PokemonApiInfo fetchPokemon(String name) throws IOException {
            throw new IOException("offline");
        }
    }
}
