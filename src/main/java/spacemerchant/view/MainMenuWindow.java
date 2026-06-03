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
import spacemerchant.service.CampaignService;

import java.util.List;

public class MainMenuWindow extends BasicWindow {
    private final GuiManager guiManager;
    private final SaveManager saveManager;
    private final CampaignService campaignService;
    private String message = "";

    public MainMenuWindow(GuiManager guiManager) {
        super("Space Merchant - Menu Główne");
        this.guiManager = guiManager;
        this.saveManager = new SaveManager();
        this.campaignService = new CampaignService();
        setHints(List.of(Window.Hint.CENTERED));

        refreshUI();
    }

    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(3));
        rootPanel.setPreferredSize(new TerminalSize(130, 30));

        rootPanel.addComponent(new EmptySpace(new TerminalSize(14, 1)));

        Panel centerPanel = new Panel(new GridLayout(1));
        centerPanel.setPreferredSize(new TerminalSize(98, 30));

        Panel titlePanel = new Panel(new GridLayout(1));
        titlePanel.addComponent(new Label("  _____ ____     _    ____ _____   __  __ _____ ____   ____ _   _    _    _   _ _____ "));
        titlePanel.addComponent(new Label(" / ____|  _ \\   / \\  / ___| ____| |  \\/  | ____|  _ \\ / ___| | | |  / \\  | \\ | |_   _|"));
        titlePanel.addComponent(new Label(" \\___ \\| |_) | / _ \\| |   |  _|   | |\\/| |  _| | |_) | |   | |_| | / _ \\ |  \\| | | |  "));
        titlePanel.addComponent(new Label("  ___) |  __/ / ___ \\ |___| |___  | |  | | |___|  _ <| |___|  _  |/ ___ \\| |\\  | | |  "));
        titlePanel.addComponent(new Label(" |____/|_|   /_/   \\_\\____|_____| |_|  |_|_____|_| \\_\\\\____|_| |_/_/   \\_\\_| \\_| |_|  "));
        titlePanel.addComponent(new EmptySpace());
        titlePanel.addComponent(new Label("Handel, zaloga i dalekie szlaki nadprzestrzenne"));
        titlePanel.addComponent(new Label("Czarno-bialy kokpit kapitana: kupuj, rekrutuj, ulepszaj i przetrwaj trase."));
        centerPanel.addComponent(titlePanel.withBorder(Borders.singleLine("TRANSMISJA STARTOWA")));
        centerPanel.addComponent(new EmptySpace());

        Panel objectivesPanel = new Panel(new GridLayout(1));
        objectivesPanel.addComponent(new Label("Aby wygrać kampanię, spełnij wszystkie cele:"));
        for (String objective : campaignService.getObjectiveDescriptions()) {
            objectivesPanel.addComponent(new Label("- " + objective));
        }
        centerPanel.addComponent(objectivesPanel.withBorder(Borders.singleLine("CELE KAMPANII")));
        centerPanel.addComponent(new EmptySpace());

        Panel buttonsPanel = new Panel(new GridLayout(1));
        buttonsPanel.addComponent(new Button("Nowa Gra", () -> startGame(createNewGame())));

        Button loadButton = new Button("Wczytaj", this::loadSavedGame);
        if (!saveManager.saveExists()) {
            loadButton.setEnabled(false);
            loadButton.setLabel("Wczytaj (brak zapisu)");
        }
        buttonsPanel.addComponent(loadButton);

        buttonsPanel.addComponent(new Button("Wyjdź", this::close));
        centerPanel.addComponent(buttonsPanel.withBorder(Borders.singleLine("MENU")));

        if (!message.isEmpty()) {
            centerPanel.addComponent(new EmptySpace());
            centerPanel.addComponent(new Label(message));
        }

        rootPanel.addComponent(centerPanel);
        rootPanel.addComponent(new EmptySpace(new TerminalSize(14, 1)));

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
