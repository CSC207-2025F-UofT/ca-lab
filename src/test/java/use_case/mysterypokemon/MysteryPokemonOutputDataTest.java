package use_case.mysterypokemon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MysteryPokemonOutputDataTest {

    @Test
    void answerGettersReturnValuesPassedToConstructor() {
        // Arrange
        int guessesLeft = 7;
        boolean sameMainType = true;
        boolean mult0 = false;
        boolean mult025 = false;
        boolean mult05 = true;
        boolean mult1 = false;
        boolean mult2 = false;
        boolean mult4 = false;
        boolean sameLegendaryStatus = true;
        boolean sameMythicalStatus = false;
        boolean tbsLess = true;
        boolean tbsSame = false;
        boolean tbsMore = false;
        boolean gameOver = false;
        boolean correct = false;
        String answerName = "Pikachu";
        String answerSpriteUrl = "https://example.com/pikachu.png";

        MysteryPokemonOutputData data = new MysteryPokemonOutputData(
                guessesLeft,
                sameMainType,
                mult0,
                mult025,
                mult05,
                mult1,
                mult2,
                mult4,
                sameLegendaryStatus,
                sameMythicalStatus,
                tbsLess,
                tbsSame,
                tbsMore,
                gameOver,
                correct,
                answerName,
                answerSpriteUrl
        );

        // Act & Assert (this is what gives you coverage)
        assertEquals("Pikachu", data.getAnswerName());
        assertEquals("https://example.com/pikachu.png", data.getAnswerSpriteUrl());
    }
}