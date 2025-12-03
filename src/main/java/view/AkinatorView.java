package view;

import data_access.PokeApiGateway;
import interface_adapter.ViewManagerModel;
import interface_adapter.akinator.AkinatorController;
import interface_adapter.akinator.AkinatorState;
import interface_adapter.akinator.AkinatorViewModel;
import interface_adapter.themes.Theme;
import interface_adapter.themes.ThemeManager;
import interface_adapter.themes.ThemeUtil;
import interface_adapter.themes.ThemedView;
import use_case.akinator.AkinatorOutputData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

/**
 * The view for displaying the Akinator screen.
 */
public class AkinatorView extends JPanel implements PropertyChangeListener, ThemedView {

    private final String viewName = "akinator";
    private final AkinatorViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private AkinatorController controller;

    private final JLabel promptLabel = new JLabel("Press Start to begin.", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton yesButton = new JButton("Yes");
    private final JButton noButton = new JButton("No");
    private final JButton unknownButton = new JButton("I don't know");
    private final JButton startButton = new JButton("Start");
    private final JButton resetButton = new JButton("Reset");
    private final JButton backButton = new JButton("Back to Dashboard");
    private final JPanel guessPanel = new JPanel(new BorderLayout());
    private final JLabel guessText = new JLabel("", SwingConstants.CENTER);
    private final JLabel spriteLabel = new JLabel("No artwork", SwingConstants.CENTER);
    private final JButton guessYes = new JButton("Yes!");
    private final JButton guessNo = new JButton("Nope");
    private final JPanel revealPanel = new JPanel(new BorderLayout(5, 5));
    private final JTextField revealInput = new JTextField();
    private final JButton revealSubmit = new JButton("Reveal");
    private int lastRevealPromptId = -1;

    public AkinatorView(AkinatorViewModel viewModel, ViewManagerModel viewManagerModel, ThemeManager themeManager) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.viewModel.addPropertyChangeListener(this);

        // Colour Theme Changer
        themeManager.registerView(this);
        applyTheme(themeManager.getActiveTheme());

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 255));

        promptLabel.setFont(promptLabel.getFont().deriveFont(Font.BOLD, 22f));
        statusLabel.setForeground(Color.DARK_GRAY);

        JPanel buttonRow = new JPanel(new FlowLayout());
        buttonRow.add(yesButton);
        buttonRow.add(noButton);
        buttonRow.add(unknownButton);

        JPanel controlRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlRow.add(startButton);
        controlRow.add(resetButton);
        controlRow.add(backButton);

        guessPanel.setBorder(BorderFactory.createTitledBorder("My guess"));
        JPanel guessButtons = new JPanel(new FlowLayout());
        guessButtons.add(guessYes);
        guessButtons.add(guessNo);
        guessPanel.add(guessText, BorderLayout.NORTH);
        guessPanel.add(spriteLabel, BorderLayout.CENTER);
        guessPanel.add(guessButtons, BorderLayout.SOUTH);
        guessPanel.setVisible(false);

        revealPanel.setBorder(BorderFactory.createTitledBorder("Tell me the Pokémon"));
        JLabel revealLabel = new JLabel("Name:");
        JPanel revealInputRow = new JPanel(new BorderLayout(5, 5));
        revealInputRow.add(revealLabel, BorderLayout.WEST);
        revealInputRow.add(revealInput, BorderLayout.CENTER);
        revealInputRow.add(revealSubmit, BorderLayout.EAST);
        revealPanel.add(revealInputRow, BorderLayout.CENTER);
        revealPanel.setVisible(false);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(buttonRow, BorderLayout.CENTER);
        centerPanel.add(revealPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(controlRow, BorderLayout.EAST);

        add(promptLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(guessPanel, BorderLayout.EAST);

        wireActions();
    }

    private void wireActions() {
        yesButton.addActionListener(e -> withController(AkinatorController::answerYes));
        noButton.addActionListener(e -> withController(AkinatorController::answerNo));
        unknownButton.addActionListener(e -> withController(AkinatorController::answerUnknown));
        guessYes.addActionListener(e -> withController(controller -> controller.confirmGuess(true)));
        guessNo.addActionListener(e -> withController(controller -> controller.confirmGuess(false)));
        revealSubmit.addActionListener(e -> submitRevealInput());
        revealInput.addActionListener(e -> submitRevealInput());
        startButton.addActionListener(e -> withController(AkinatorController::start));
        resetButton.addActionListener(e -> withController(AkinatorController::reset));
        backButton.addActionListener(e -> {
            viewManagerModel.setState("dashboard");
            viewManagerModel.firePropertyChange();
        });
    }

    public void setController(AkinatorController controller) {
        this.controller = controller;
    }

    /**
     * Listens for property change events.
     *
     * @param evt the property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("error")) {
            String message = viewModel.getState().getErrorMessage();
            if (message != null && !message.isBlank()) {
                JOptionPane.showMessageDialog(this, message, "Pokénator", JOptionPane.WARNING_MESSAGE);
            }
            return;
        }
        if (!evt.getPropertyName().equals("state")) {
            return;
        }
        AkinatorState state = (AkinatorState) evt.getNewValue();
        promptLabel.setText(state.getPrompt());
        String status = state.getStatus();
        if (state.getQuestionLimit() > 0 && state.getQuestionsAsked() > 0) {
            String counter = String.format(" (Question %d of %d)",
                    state.getQuestionsAsked(),
                    state.getQuestionLimit());
            status = (status == null || status.isBlank()) ? counter.trim() : status + counter;
        }
        statusLabel.setText(status == null ? "" : status);
        boolean guessing = state.isAwaitingGuess();
        boolean awaitingReveal = state.isAwaitingReveal();
        guessPanel.setVisible(state.isGuessVisible());
        boolean controllerReady = controller != null;
        guessYes.setEnabled(guessing && controllerReady);
        guessNo.setEnabled(guessing && controllerReady);
        boolean canAnswer = controllerReady
                && !guessing && !awaitingReveal
                && state.isRoundActive()
                && state.getStep() == AkinatorOutputData.Step.QUESTION;
        yesButton.setEnabled(canAnswer);
        noButton.setEnabled(canAnswer);
        unknownButton.setEnabled(canAnswer);
        startButton.setEnabled(controllerReady && !state.isRoundActive());
        boolean revealEnabled = awaitingReveal && controllerReady;
        revealPanel.setVisible(awaitingReveal);
        revealSubmit.setEnabled(revealEnabled);
        revealInput.setEnabled(revealEnabled);
        if (awaitingReveal && state.getRevealPromptId() != lastRevealPromptId) {
            lastRevealPromptId = state.getRevealPromptId();
            revealInput.setText("");
            SwingUtilities.invokeLater(revealInput::requestFocusInWindow);
        } else if (!awaitingReveal) {
            revealInput.setText("");
        }

        if (state.getGuessInfo() != null) {
            updateGuessInfo(state.getGuessInfo());
        } else {
            guessText.setText("");
            spriteLabel.setIcon(null);
            spriteLabel.setText("No artwork");
        }
    }

    private void updateGuessInfo(PokeApiGateway.PokemonApiInfo info) {
        guessText.setText(String.format(
                "<html>Is your Pokémon <b>%s</b>?<br/>Types: %s<br/>Height: %.2fm, Weight: %.1fkg</html>",
                info.getDisplayName(),
                info.getTypes().isEmpty() ? "-" : String.join(", ", info.getTypes()),
                info.getHeightMeters(),
                info.getWeightKg()));

        if (info.getSpriteUrl() != null) {
            try {
                Image image = ImageIO.read(new URL(info.getSpriteUrl()));
                spriteLabel.setIcon(new ImageIcon(image.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                spriteLabel.setText("");
            } catch (IOException e) {
                spriteLabel.setIcon(null);
                spriteLabel.setText("Couldn’t load sprite");
            }
        } else {
            spriteLabel.setIcon(null);
            spriteLabel.setText("No sprite from API");
        }
    }

    public String getViewName() {
        return viewName;
    }

    private void withController(java.util.function.Consumer<AkinatorController> task) {
        if (controller != null) {
            task.accept(controller);
        }
    }

    private void submitRevealInput() {
        if (controller == null) {
            return;
        }
        String text = revealInput.getText();
        if (text == null) {
            text = "";
        }
        final String trimmed = text.trim();
        revealInput.setText("");
        controller.revealPokemon(trimmed);
    }

    /**
     * Applies a chosen theme to the Akinator game.
     *
     * @param theme the theme to apply
     */
    public void applyTheme(Theme theme) {
        ThemeUtil.applyTheme(this, theme);
    }
}
