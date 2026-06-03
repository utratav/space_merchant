package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.data.SaveManager;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.service.CampaignService;

public class CockpitWindow extends BasicWindow {
    private GuiManager guiManager;
    private Ship ship;
    private SaveManager saveManager;
    private CampaignService campaignService;
    private String message = "";

    public CockpitWindow(GuiManager guiManager, Ship ship) {
        super("Space Merchant - Kokpit");
        this.guiManager = guiManager;
        this.ship = ship;
        this.saveManager = new SaveManager();
        this.campaignService = new CampaignService();

        // Budujemy interfejs po raz pierwszy
        refreshUI();
    }

    // Metoda przebudowująca kokpit, wywoływana za każdym razem, gdy wracasz z innego okna
    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(2));
        Location currentLocation = ship.getCurrentLocation();

        if (ship.isDefeated()) {
            renderDefeatScreen();
            return;
        }

        campaignService.checkVictory(ship);
        if (ship.isVictorious()) {
            renderVictoryScreen();
            return;
        }

        // --- LEWY PANEL: MENU AKCJI ---
        Panel actionMenuPanel = new Panel(new GridLayout(1));

        actionMenuPanel.addComponent(new Button("[1] Otwórz Ekwipunek i Ładownię", () -> {
            guiManager.showWindow(new InventoryWindow(guiManager, ship));
            refreshUI(); // Odśwież kokpit po powrocie z ładowni
        }));

        actionMenuPanel.addComponent(new Button("[2] Mapa Gwiezdna", () -> {
            guiManager.showWindow(new StarMapWindow(guiManager, ship));
            refreshUI(); // Odśwież kokpit po powrocie ze skoku nadprzestrzennego
        }));

        actionMenuPanel.addComponent(new Button("[3] Zarządzanie Załogą", () -> {
            guiManager.showWindow(new CrewManagementWindow(guiManager, ship));
            refreshUI(); // Odśwież kokpit po wyleczeniu załogi
        }));

        Button dockButton = new Button("[4] Połącz ze Stacją Kosmiczną", () -> {
            guiManager.showWindow(new StationMenuWindow(guiManager, ship));
            refreshUI(); // Odśwież kokpit po zakupach na giełdzie
        });

        if (currentLocation == null || !currentLocation.hasStation()) {
            dockButton.setEnabled(false);
            dockButton.setLabel("[4] -- BRAK STACJI --");
        }

        actionMenuPanel.addComponent(dockButton);
        if (currentLocation == null || !currentLocation.hasStation()) {
            actionMenuPanel.addComponent(new Label("    Dokowanie niedostepne poza stacja."));
        }

        actionMenuPanel.addComponent(new Button("[5] Schemat Statku", () -> {
            guiManager.showWindow(new SchematicWindow(ship));
            refreshUI(); // Odśwież kokpit po obejrzeniu schematu statku
        }));

        actionMenuPanel.addComponent(new EmptySpace());
        actionMenuPanel.addComponent(new Button("Zapisz i wyjdź", this::saveAndExit));
        actionMenuPanel.addComponent(new Button("Wyjście z gry", this::close));

        if (!message.isEmpty()) {
            actionMenuPanel.addComponent(new EmptySpace());
            actionMenuPanel.addComponent(new Label(message));
        }

        if (!ship.getCrewIncidentMessage().isEmpty()) {
            actionMenuPanel.addComponent(new EmptySpace());
            actionMenuPanel.addComponent(new Label("INCYDENT: " + ship.getCrewIncidentMessage()));
        }

        actionMenuPanel.addComponent(new EmptySpace());
        Panel objectivesPanel = new Panel(new GridLayout(1));
        for (String objective : campaignService.getObjectiveProgress(ship)) {
            objectivesPanel.addComponent(new Label(objective));
        }
        actionMenuPanel.addComponent(objectivesPanel.withBorder(Borders.singleLine("CELE KAMPANII")));

        rootPanel.addComponent(actionMenuPanel.withBorder(Borders.singleLine("WYBIERZ AKCJĘ")));

        // --- PRAWY PANEL: STATUS POKŁADOWY ---
        Panel statusPanel = new Panel(new GridLayout(1));
        statusPanel.addComponent(new Label("--- STATEK: " + ship.getName() + " ---"));
        statusPanel.addComponent(new Label("Lokacja: " + (currentLocation != null ? currentLocation.getName() : "Lot w toku")));
        statusPanel.addComponent(new Label("Dokowanie: " + (currentLocation != null && currentLocation.hasStation() ? "dostępne" : "brak stacji")));
        statusPanel.addComponent(new Label(String.format("Kredyty: %.2f cr", ship.getCredits())));

        String hpBar = UIUtils.drawProgressBar((int) ship.getCurrentHp(), (int) ship.getMaxHp(), 20);
        statusPanel.addComponent(new Label("Kadłub:  " + hpBar + " " + ship.getCurrentHp() + "/" + ship.getMaxHp()));

        String fuelBar = UIUtils.drawProgressBar((int) ship.getCurrentFuel(), (int) ship.getMaxFuel(), 20);
        statusPanel.addComponent(new Label("Paliwo:  " + fuelBar + " " + ship.getCurrentFuel() + "/" + ship.getMaxFuel()));

        double cargoWeight = ship.getCargo().getTotalWeight();
        String cargoBar = UIUtils.drawProgressBar((int) cargoWeight, (int) ship.getMaxCargoWeight(), 20);
        statusPanel.addComponent(new Label("Ładownia:" + cargoBar + " " + cargoWeight + "/" + ship.getMaxCargoWeight()));

        statusPanel.addComponent(new EmptySpace());
        statusPanel.addComponent(new Label(String.format("--- ZAŁOGA (%d/%d) ---", ship.getCrew().size(), ship.getMaxCrew())));

        if (ship.getCrew().isEmpty()) {
            statusPanel.addComponent(new Label("Brak załogi na pokładzie."));
        } else {
            for (CrewMember member : ship.getCrew()) {
                String crewHpBar = UIUtils.drawProgressBar(member.getHp(), member.getMaxHp(), 10);
                statusPanel.addComponent(new Label("- " + member.getName() + " (" + member.getRole() + ")"));
                statusPanel.addComponent(new Label("  HP: " + crewHpBar + " " + member.getHp() + "/" + member.getMaxHp()));
            }
        }

        rootPanel.addComponent(statusPanel.withBorder(Borders.singleLine("STATUS POKŁADOWY")));

        this.setComponent(rootPanel);
    }

    private void renderDefeatScreen() {
        Panel rootPanel = new Panel(new GridLayout(1));
        rootPanel.addComponent(new Label("KONIEC GRY"));
        rootPanel.addComponent(new EmptySpace());
        rootPanel.addComponent(new Label(ship.getDefeatReason()));
        rootPanel.addComponent(new EmptySpace());
        rootPanel.addComponent(new Label("Statek zostal utracony. Wczytaj zapis albo rozpocznij nowa gre."));
        rootPanel.addComponent(new EmptySpace());
        rootPanel.addComponent(new Button("Wyjście z gry", this::close));
        this.setComponent(rootPanel.withBorder(Borders.singleLine("RAPORT KONCOWY")));
    }

    private void renderVictoryScreen() {
        Panel rootPanel = new Panel(new GridLayout(1));
        rootPanel.addComponent(new Label("ZWYCIĘSTWO"));
        rootPanel.addComponent(new EmptySpace());
        rootPanel.addComponent(new Label(ship.getVictoryReason()));
        rootPanel.addComponent(new EmptySpace());
        rootPanel.addComponent(new Label("Twoja załoga zapisała się w historii szlaków nadprzestrzennych."));
        rootPanel.addComponent(new EmptySpace());
        rootPanel.addComponent(new Button("Zapisz i wyjdź", this::saveAndExit));
        rootPanel.addComponent(new Button("Wyjście z gry", this::close));
        this.setComponent(rootPanel.withBorder(Borders.singleLine("RAPORT ZWYCIESTWA")));
    }

    private void saveAndExit() {
        try {
            saveManager.saveGame(ship);
            this.close();
        } catch (RuntimeException e) {
            message = e.getMessage();
            refreshUI();
        }
    }
}