package interface_adapter.mysterypokemon;

public class MysteryPokemonState {

    private int guessesLeft;
    private String errorMessage = "";

    private boolean sameMainType;

    private boolean mult0;
    private boolean mult025;
    private boolean mult05;
    private boolean mult1;
    private boolean mult2;
    private boolean mult4;

    private boolean sameLegendaryStatus;
    private boolean sameMythicalStatus;

    private boolean tbsLess;
    private boolean tbsSame;
    private boolean tbsMore;

    private boolean gameOver;
    private boolean correct;

    private String answerName;
    private String answerSpriteUrl;


    public int getGuessesLeft() { return guessesLeft; }
    public void setGuessesLeft(int guessesLeft) { this.guessesLeft = guessesLeft; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public boolean isSameMainType() { return sameMainType; }
    public void setSameMainType(boolean sameMainType) { this.sameMainType = sameMainType; }

    public boolean isMult0() { return mult0; }
    public void setMult0(boolean mult0) { this.mult0 = mult0; }

    public boolean isMult025() {return mult025; }
    public void setMult025(boolean mult025) {this.mult025 = mult025; }

    public boolean isMult05() { return mult05; }
    public void setMult05(boolean mult05) { this.mult05 = mult05; }

    public boolean isMult1() { return mult1; }
    public void setMult1(boolean mult1) { this.mult1 = mult1; }

    public boolean isMult2() { return mult2; }
    public void setMult2(boolean mult2) { this.mult2 = mult2; }

    public boolean isMult4() { return mult4; }
    public void setMult4(boolean mult4) { this.mult4 = mult4; }

    public boolean isSameLegendaryStatus() { return sameLegendaryStatus; }
    public void setSameLegendaryStatus(boolean sameLegendaryStatus) { this.sameLegendaryStatus = sameLegendaryStatus; }

    public boolean isSameMythicalStatus() { return sameMythicalStatus; }
    public void setSameMythicalStatus(boolean sameMythicalStatus) { this.sameMythicalStatus = sameMythicalStatus; }

    public boolean isTbsLess() { return tbsLess; }
    public void setTbsLess(boolean tbsLess) { this.tbsLess = tbsLess; }

    public boolean isTbsSame() { return tbsSame; }
    public void setTbsSame(boolean tbsSame) { this.tbsSame = tbsSame; }

    public boolean isTbsMore() { return tbsMore; }
    public void setTbsMore(boolean tbsMore) { this.tbsMore = tbsMore; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public boolean isPlayerWon() { return correct; }
    public void setPlayerWon(boolean correct) { this.correct = correct; }

    public String getAnswerName() { return answerName; }
    public void setAnswerName(String answerName) { this.answerName = answerName; }

    public String getAnswerSpriteUrl() { return answerSpriteUrl; }
    public void setAnswerSpriteUrl(String answerSpriteUrl) { this.answerSpriteUrl = answerSpriteUrl; }

}

