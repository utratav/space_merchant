package spacemerchant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import spacemerchant.controller.GuiManager;
import spacemerchant.data.SaveManager;
import spacemerchant.data.ShipData;
import spacemerchant.data.UniverseData;
import spacemerchant.model.Ship;

import java.util.List;

public class MainMenuWindow extends BasicWindow {
    private final GuiManager guiManager;
    private final SaveManager saveManager;
    private String message = "";

    public MainMenuWindow(GuiManager guiManager) {
        super("Space Merchant - Menu Główne");
        this.guiManager = guiManager;
        this.saveManager = new SaveManager();
        setHints(List.of(Window.Hint.CENTERED));

        refreshUI();
    }

    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));
        rootPanel.setPreferredSize(new TerminalSize(96, 24));

        Panel titlePanel = new Panel(new GridLayout(1));
        titlePanel.addComponent(new Label("  _____ ____     _    ____ _____   __  __ _____ ____   ____ _   _    _    _   _ _____ "));
        titlePanel.addComponent(new Label(" / ____|  _ \\   / \\  / ___| ____| |  \\/  | ____|  _ \\ / ___| | | |  / \\  | \\ | |_   _|"));
        titlePanel.addComponent(new Label(" \\___ \\| |_) | / _ \\| |   |  _|   | |\\/| |  _| | |_) | |   | |_| | / _ \\ |  \\| | | |  "));
        titlePanel.addComponent(new Label("  ___) |  __/ / ___ \\ |___| |___  | |  | | |___|  _ <| |___|  _  |/ ___ \\| |\\  | | |  "));
        titlePanel.addComponent(new Label(" |____/|_|   /_/   \\_\\____|_____| |_|  |_|_____|_| \\_\\\\____|_| |_/_/   \\_\\_| \\_| |_|  "));
        titlePanel.addComponent(new EmptySpace());
        titlePanel.addComponent(new Label("Handel, zaloga i dalekie szlaki nadprzestrzenne"));
        titlePanel.addComponent(new Label("Czarno-bialy kokpit kapitana: kupuj, rekrutuj, ulepszaj i przetrwaj trase."));
        rootPanel.addComponent(titlePanel.withBorder(Borders.singleLine("TRANSMISJA STARTOWA")));
        rootPanel.addComponent(new EmptySpace());

        Panel buttonsPanel = new Panel(new GridLayout(1));
        buttonsPanel.addComponent(new Button("Nowa Gra", () -> startGame(createNewGame())));

        Button loadButton = new Button("Wczytaj", this::loadSavedGame);
        if (!saveManager.saveExists()) {
            loadButton.setEnabled(false);
            loadButton.setLabel("Wczytaj (brak zapisu)");
        }
        buttonsPanel.addComponent(loadButton);

        buttonsPanel.addComponent(new Button("Wyjdź", this::close));
        rootPanel.addComponent(buttonsPanel.withBorder(Borders.singleLine("MENU")));

        if (!message.isEmpty()) {
            rootPanel.addComponent(new EmptySpace());
            rootPanel.addComponent(new Label(message));
        }

        this.setComponent(rootPanel);
    }

    private Ship createNewGame() {
        return new Ship(ShipData.getStartingModel(), 1000.0, UniverseData.getStartingLocation());
    }

    private void loadSavedGame() {
        try {
            startGame(saveManager.loadGame());
        } catch (RuntimeException e) {
            message = e.getMessage();
            refreshUI();
        }
    }

    private void startGame(Ship ship) {
        this.close();
        guiManager.showWindow(new CockpitWindow(guiManager, ship));
    }
}
