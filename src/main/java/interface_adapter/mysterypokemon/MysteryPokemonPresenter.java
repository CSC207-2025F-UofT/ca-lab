package interface_adapter.mysterypokemon;

import interface_adapter.ViewManagerModel;
import use_case.mysterypokemon.MysteryPokemonOutputBoundary;
import use_case.mysterypokemon.MysteryPokemonOutputData;

import javax.swing.*;

public class MysteryPokemonPresenter implements MysteryPokemonOutputBoundary {

    private final MysteryPokemonViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public MysteryPokemonPresenter(MysteryPokemonViewModel viewModel,
                                   ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(MysteryPokemonOutputData outputData) {
        MysteryPokemonState state = viewModel.getState();

        state.setErrorMessage("");

        state.setGuessesLeft(outputData.getGuessesLeft());

        state.setSameMainType(outputData.isSameMainType());

        state.setMult0(outputData.isMult0());
        state.setMult025(outputData.isMult025());
        state.setMult05(outputData.isMult05());
        state.setMult1(outputData.isMult1());
        state.setMult2(outputData.isMult2());
        state.setMult4(outputData.isMult4());

        state.setSameLegendaryStatus(outputData.isSameLegendaryStatus());
        state.setSameMythicalStatus(outputData.isSameMythicalStatus());

        state.setTbsLess(outputData.isTbsLess());
        state.setTbsSame(outputData.isTbsSame());
        state.setTbsMore(outputData.isTbsMore());

        state.setGameOver(outputData.isGameOver());
        state.setPlayerWon(outputData.isCorrect());
        state.setAnswerName(outputData.getAnswerName());
        state.setAnswerSpriteUrl(outputData.getAnswerSpriteUrl());

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        MysteryPokemonState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
    }
}
