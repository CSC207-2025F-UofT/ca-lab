package view;

import interface_adapter.ViewManagerModel;
import data_access.Gen1Loader;
import interface_adapter.mysterypokemon.MysteryPokemonController;
import interface_adapter.mysterypokemon.MysteryPokemonState;
import interface_adapter.mysterypokemon.MysteryPokemonViewModel;
import interface_adapter.themes.Theme;
import interface_adapter.themes.ThemeUtil;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MysteryPokemonView extends JPanel implements PropertyChangeListener {

    private final String ViewName = "mysterypokemon";
    private final Gen1Loader gen1loader;
    private final MysteryPokemonViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private MysteryPokemonController controller;

    private final JLabel startLabel = new JLabel("Press start to begin:");
    private final JLabel guessPrompt = new JLabel("Make your guess:");
    private final JLabel guessesLeftPrompt = new JLabel("Guesses left:");
    private final JLabel guessesLeftValue = new JLabel("10");

    private final JComboBox<String> playerguess = new JComboBox<>();

    private final JButton startButton = new JButton("start");
    private final JButton confirmButton = new JButton("OK");
    private final JButton quitButton = new JButton("quit");
    private final JButton resetButton = new JButton("reset");

    private final JLabel sameMainType = new JLabel("same main type");
    private final JLabel mult0 = new JLabel("damage multiplier: ×0");
    private final JLabel mult025 = new JLabel("damage multiplier: ×0.25");
    private final JLabel mult05 = new JLabel("damage multiplier: ×0.5");
    private final JLabel mult1 = new JLabel("damage multiplier: ×1");
    private final JLabel mult2 = new JLabel("damage multiplier: ×2");
    private final JLabel mult4 = new JLabel("damage multiplier: ×4");
    private final JLabel legendary = new JLabel("same legendary status");
    private final JLabel mythical  = new JLabel("same mythical status");
    private final JLabel lessTBS = new JLabel("Total base stats: too small");
    private final JLabel sameTBS = new JLabel("Total base stats: same!");
    private final JLabel moreTBS = new JLabel("Total base stats: too large");

    private final JLabel errorLabel = new JLabel("");
    private boolean lastGameOver = false;

    public MysteryPokemonView(Gen1Loader gen1loader, MysteryPokemonViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.gen1loader = gen1loader;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        setupLayout();
        actionListeners();
        initVisibility();

        }

    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(startLabel);
        add(startButton);

        add(guessPrompt);

        ArrayList<String> pokemons = gen1loader.loadPokemonNames("src/main/resources/gen1Pokemon.json");
        for (int i = 0; i < pokemons.size(); i++){
            String pokemon = pokemons.get(i);
            playerguess.addItem(pokemon);
        }
        add(playerguess);

        JPanel buttons = new JPanel();
        buttons.add(confirmButton);
        buttons.add(resetButton);
        buttons.add(quitButton);
        add(buttons);

        JPanel guessesPanel = new JPanel();
        guessesPanel.add(guessesLeftPrompt);
        guessesPanel.add(guessesLeftValue);
        add(guessesPanel);

        add(sameMainType);
        add(mult0);
        add(mult025);
        add(mult05);
        add(mult1);
        add(mult2);
        add(mult4);
        add(legendary);
        add(mythical);
        add(lessTBS);
        add(sameTBS);
        add(moreTBS);

        errorLabel.setForeground(Color.RED);
        add(errorLabel);
    }

    private void initVisibility() {
        sameMainType.setVisible(false);
        mult0.setVisible(false);
        mult025.setVisible(false);
        mult05.setVisible(false);
        mult1.setVisible(false);
        mult2.setVisible(false);
        mult4.setVisible(false);
        legendary.setVisible(false);
        mythical.setVisible(false);
        lessTBS.setVisible(false);
        sameTBS.setVisible(false);
        moreTBS.setVisible(false);
    }

    private void actionListeners() {
        startButton.addActionListener(e -> controller.start());

        confirmButton.addActionListener(e -> {
            String guess = (String) playerguess.getSelectedItem();
            try {
                controller.confirm(guess);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        quitButton.addActionListener(e -> {
            viewManagerModel.setState("dashboard");
            viewManagerModel.firePropertyChange();
            }
        );
        resetButton.addActionListener(e -> controller.reset());
    }

    public void setMysteryPokemonController(MysteryPokemonController controller){
        this.controller = controller;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            MysteryPokemonState state = (MysteryPokemonState) evt.getNewValue();
            updateFromState(state);
        }
    }

    private void updateFromState(MysteryPokemonState state) {
        guessesLeftValue.setText(String.valueOf(state.getGuessesLeft()));
        errorLabel.setText(state.getErrorMessage());

        sameMainType.setVisible(state.isSameMainType());

        mult0.setVisible(state.isMult0());
        mult025.setVisible(state.isMult025());
        mult05.setVisible(state.isMult05());
        mult1.setVisible(state.isMult1());
        mult2.setVisible(state.isMult2());
        mult4.setVisible(state.isMult4());

        legendary.setVisible(state.isSameLegendaryStatus());
        mythical.setVisible(state.isSameMythicalStatus());

        lessTBS.setVisible(state.isTbsLess());
        sameTBS.setVisible(state.isTbsSame());
        moreTBS.setVisible(state.isTbsMore());

        if (state.isGameOver() && !lastGameOver) {
            showResultDialog(state);
        }
        lastGameOver = state.isGameOver();
    }

    public String getViewName() {
        return ViewName;
    }

    private void showResultDialog(MysteryPokemonState state) {
        String title;
        String message;

        if (state.isPlayerWon()) {
            message = "You win!";
            title = "Your guess is correct. The Pokemon is " + state.getAnswerName() + ".";
        } else {
            message = "You lost!";
            title = "Your guess is incorrect. The correct answer is " + state.getAnswerName() + ".";
        }

        if (state.getAnswerSpriteUrl() != null && !state.getAnswerSpriteUrl().isEmpty()) {
            String url = state.getAnswerSpriteUrl();

            try {
                ImageIcon icon = new ImageIcon(new URL(url));
                Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaled);

                JOptionPane.showMessageDialog(
                        this,
                        title,
                        message,
                        JOptionPane.INFORMATION_MESSAGE,
                        icon
                );
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(
                        this,
                        title,
                        message,
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    message,
                    title,
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void applyTheme(Theme theme) {
        ThemeUtil.applyTheme(this, theme);
    }

}
