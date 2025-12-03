package use_case.mysterypokemon;

public class MysteryPokemonOutputData {

    private final int guessesLeft;

    private final boolean sameMainType;

    private final boolean mult0;
    private final boolean mult025;
    private final boolean mult05;
    private final boolean mult1;
    private final boolean mult2;
    private final boolean mult4;

    private final boolean sameLegendaryStatus;
    private final boolean sameMythicalStatus;

    private final boolean tbsLess;
    private final boolean tbsSame;
    private final boolean tbsMore;

    private final boolean gameOver;
    private final boolean correct;

    private final String answerName;
    private final String answerSpriteUrl;

    public MysteryPokemonOutputData(
            int guessesLeft,
            boolean sameMainType,
            boolean mult0, boolean mult025,
            boolean mult05,
            boolean mult1,
            boolean mult2,
            boolean mult4,
            boolean sameLegendaryStatus,
            boolean sameMythicalStatus,
            boolean tbsLess,
            boolean tbsSame,
            boolean tbsMore,
            boolean gameOver,
            boolean correct, String answerName, String answerSpriteUrl
    ) {
        this.guessesLeft = guessesLeft;
        this.sameMainType = sameMainType;
        this.mult0 = mult0;
        this.mult025 = mult025;
        this.mult05 = mult05;
        this.mult1 = mult1;
        this.mult2 = mult2;
        this.mult4 = mult4;
        this.sameLegendaryStatus = sameLegendaryStatus;
        this.sameMythicalStatus = sameMythicalStatus;
        this.tbsLess = tbsLess;
        this.tbsSame = tbsSame;
        this.tbsMore = tbsMore;
        this.gameOver = gameOver;
        this.correct = correct;
        this.answerName = answerName;
        this.answerSpriteUrl = answerSpriteUrl;
    }

    public int getGuessesLeft() { return guessesLeft; }

    public boolean isSameMainType() { return sameMainType; }

    public boolean isMult0() { return mult0; }
    public boolean isMult025() {return mult025; }
    public boolean isMult05() { return mult05; }
    public boolean isMult1() { return mult1; }
    public boolean isMult2() { return mult2; }
    public boolean isMult4() { return mult4; }

    public boolean isSameLegendaryStatus() { return sameLegendaryStatus; }
    public boolean isSameMythicalStatus() { return sameMythicalStatus; }

    public boolean isTbsLess() { return tbsLess; }
    public boolean isTbsSame() { return tbsSame; }
    public boolean isTbsMore() { return tbsMore; }

    public boolean isGameOver() { return gameOver; }
    public boolean isCorrect() { return correct; }

    public String getAnswerName() {return answerName; }
    public String getAnswerSpriteUrl() {return answerSpriteUrl; }
}

