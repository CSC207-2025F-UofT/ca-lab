package view;

import data_access.FileUserDataAccessObject;
import data_access.PokeApiGateway;
import interface_adapter.ViewManagerModel;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonController;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonState;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonViewModel;
import interface_adapter.themes.Theme;
import interface_adapter.themes.ThemeManager;
import interface_adapter.themes.ThemeUtil;
import interface_adapter.themes.ThemedView;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * The View for when the user is choosing their favourite Pokémon to display on profile.
 */
public class ChooseFavPokemonView extends JPanel implements ActionListener, PropertyChangeListener,
                                                            ThemedView {

    private ChooseFavPokemonController chooseFavPokemonController;

    private final PokeApiGateway pokeApiGateway;

    private final int NUM_ROWS = 16;
    private int page = 0;
    private final ArrayList<String> pokemonList;
    private final ArrayList<JPanel> listDisplay;

    private final JLabel chosenLabel;

    private final JButton nextPage;
    private final JButton previousPage;
    private final JButton cancel;
    private final JButton save;

    private String chosenPokemon;

    public ChooseFavPokemonView(ChooseFavPokemonViewModel chooseFavPokemonViewModel, ViewManagerModel viewManagerModel,
                                GameDashboard gameDashboard, FileUserDataAccessObject DAO,
                                PokeApiGateway pokeApiGateway, ThemeManager themeManager) {

        themeManager.registerView(this);
        applyTheme(themeManager.getActiveTheme());

        this.pokeApiGateway = pokeApiGateway;
        new ChooseFavPokemonState(DAO.get(gameDashboard.getCurrentUser()));

        gameDashboard.setCFPV(this);

        this.setBackground(new Color(54, 57, 63));

        this.pokemonList = getPokemonList();
        this.chosenPokemon = "None";

        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.X_AXIS));

        chosenLabel = new JLabel("Selected Pokemon: " + this.chosenPokemon);
        chosenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chosenLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        chosenLabel.setForeground(Color.WHITE);
        chosenLabel.setBackground(new Color(54, 57, 63));

        listDisplay = new ArrayList<>();
        for (int i = 0; i < pokemonList.size(); i++) {
            if (page * NUM_ROWS + i < pokemonList.size()) {
                String pokemonName = pokemonList.get(page * NUM_ROWS + i);
                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                row.setBackground(new Color(54, 57, 63));
                JButton label = getJButton(chooseFavPokemonViewModel, pokemonName);

                row.add(label);
                row.add(new JLabel(" "));
                JLabel pokeImage = new JLabel("      ");
                getPokeImage(pokeImage, pokemonName);
                row.add(pokeImage);
                listDisplay.add(row);
            }
        }

        rowHelper(rows);
        rows.setBackground(new Color(54, 57, 63));

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setBackground(new Color(54, 57, 63));

        //buttons
        previousPage = new JButton("Previous");
        buttonBGHelper(previousPage);
        previousPage.addActionListener(
                evt -> {
                    if (evt.getSource().equals(previousPage)) {
                        if (page != 0) {
                            page -= 1;
                            rowHelper(rows);
                        }
                    }
                }
        );
        nextPage = new JButton("Next");
        buttonBGHelper(nextPage);
        nextPage.addActionListener(
                evt -> {
                    if (evt.getSource().equals(nextPage)) {
                        if ((1 + page) * NUM_ROWS < pokemonList.size()) {
                            page += 1;
                            rowHelper(rows);
                        }
                    }
                }
        );
        cancel = new JButton("Cancel");
        buttonBGHelper(cancel);
        cancel.addActionListener(
                evt -> {
                    if (evt.getSource().equals(cancel)) {
                        reset();
                        viewManagerModel.setState("User Profile");
                        viewManagerModel.firePropertyChange();
                    }
                }
        );
        save = new JButton("Save");
        buttonBGHelper(save);
        save.setBackground(new Color(88, 101, 242));
        save.addActionListener(
                evt -> {
                    if (evt.getSource().equals(save)) {
                        final ChooseFavPokemonState currentState = chooseFavPokemonViewModel.getState();

                        if (currentState.getFav_pokemon().equals("None")) {
                            currentState.setFav_pokemon("None");
                        }

                        chooseFavPokemonController.execute(
                                currentState.getUsername(),
                                currentState.getPassword(),
                                currentState.getScore(),
                                currentState.getBio(),
                                currentState.getFav_pokemon()
                        );
                        viewManagerModel.setState("User Profile");
                        viewManagerModel.firePropertyChange();
                    }
                }
        );

        buttons.add(previousPage);
        buttons.add(nextPage);
        buttons.add(cancel);
        buttons.add(save);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(chosenLabel);
        this.add(rows);
        this.add(buttons);

        this.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    @NotNull
    private JButton getJButton(ChooseFavPokemonViewModel chooseFavPokemonViewModel, String pokemonName) {
        JButton label = new JButton(pokemonName);

        label.setBackground(new Color(88, 101, 242)); // Discord blurple
        label.setForeground(Color.WHITE);
        label.setFocusPainted(false);
        label.setBorderPainted(false);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addActionListener(
                evt -> {
                    if (evt.getSource().equals(label)) {
                        setChosenPokemon(pokemonName);
                        chooseFavPokemonViewModel.getState().setFav_pokemon(pokemonName);
                        chosenLabel.setText("Selected Pokemon: " + pokemonName);
                    }
                }
        );
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) { }

    @Override
    public void propertyChange(PropertyChangeEvent evt) { }

    private void rowHelper(JPanel rows) {
        rows.removeAll();
        JPanel column1 = new JPanel();
        column1.setLayout(new BoxLayout(column1, BoxLayout.Y_AXIS));
        column1.setAlignmentX(Component.LEFT_ALIGNMENT);
        column1.setBackground(new Color(54, 57, 63));

        JPanel column2 = new JPanel();
        column2.setLayout(new BoxLayout(column2, BoxLayout.Y_AXIS));
        column2.setAlignmentX(Component.LEFT_ALIGNMENT);
        column2.setBackground(new Color(54, 57, 63));

        for (int i = 0; i < NUM_ROWS; i++) {
            if (page * NUM_ROWS + i < pokemonList.size()) {
                if (i % 2 == 0)
                    column1.add(listDisplay.get(NUM_ROWS * page + i));
                else
                    column2.add(listDisplay.get(NUM_ROWS * page + i));
            }
        }
        rows.add(column1);
        rows.add(column2);
        rows.revalidate();
        rows.repaint();
    }

    private void buttonBGHelper(JButton button) {
        button.setBackground(new Color(79, 84, 92)); // Discord blurple
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private ArrayList<String> getPokemonList() {
        ArrayList<String> pokemonList = new ArrayList<>();
        pokemonList.add("None");
        try {
            File jsonFile = new File("gen1Pokemon.json");
            JSONArray json = new JSONArray(new String(Files.readAllBytes(jsonFile.toPath())));
            for (int i = 0; i < json.length(); i++) {
                String name = json.getJSONObject(i).getString("name");
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                pokemonList.add(name);
            }
        } catch (Exception e) { System.out.println(e); }
        return  pokemonList;
    }

    private void getPokeImage(JLabel pokeImage, String pokemonName) {
        try {
            if (pokemonName.equals("None")) {
                pokeImage.setIcon(new ImageIcon(ImageIO.read(new File("nonepokemon.jpg")).
                        getScaledInstance(100,100,Image.SCALE_SMOOTH)));
            } else {
                Image image = ImageIO.read(new URL(pokeApiGateway.fetchPokemon(pokemonName).getSpriteUrl()));
                pokeImage.setIcon(new ImageIcon(image.getScaledInstance(
                        100, 100, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) { System.out.println(e); }
    }

    /**
     * Resets the screen to navigate back to the first page.
     */
    public void reset() {
        this.page = 0;
        this.chosenPokemon = "None";
    }

    public void setChosenPokemon(String chosenPokemon) {
        this.chosenPokemon = chosenPokemon;
    }

    public String getViewName() {
        return "Choose Favourite Pokemon";
    }

    public void setChooseFavPokemonController(ChooseFavPokemonController chooseFavPokemonController) {
        this.chooseFavPokemonController = chooseFavPokemonController;
    }

    /**
     * Applies the chosen theme to the choose Pokémon view.
     *
     * @param theme the theme to apply
     */
    public void applyTheme(Theme theme) {
        ThemeUtil.applyTheme(this, theme);
    }
}
