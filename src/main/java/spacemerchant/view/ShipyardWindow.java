package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.data.ShipData;
import spacemerchant.model.Ship;
import spacemerchant.model.ShipModel;
import spacemerchant.model.ShipUpgrade;
import spacemerchant.service.ShipService;
import spacemerchant.service.UpgradeService;

import java.util.Arrays;
import java.util.List;

public class ShipyardWindow extends BasicWindow {
    private GuiManager guiManager;
    private Ship ship;
    private UpgradeService upgradeService;
    private ShipService shipService;
    private String errorMessage = "";

    // Tymczasowa lista dostępnych modułów statku
    private List<ShipUpgrade> availableUpgrades = Arrays.asList(
            new ShipUpgrade("UPG_CARGO", "Powiększona Ładownia", 500.0, 0, 0.0, 50.0),
            new ShipUpgrade("UPG_HULL", "Wzmocniony Pancerz", 800.0, 50, 0.0, 0.0),
            new ShipUpgrade("UPG_FUEL", "Zbiorniki Zapasowe", 600.0, 0, 100.0, 0.0)
    );

    public ShipyardWindow(GuiManager guiManager, Ship ship) {
        super("Stocznia - Ulepszenia Statku");
        this.guiManager = guiManager;
        this.ship = ship;
        this.upgradeService = new UpgradeService();
        this.shipService = new ShipService();

        refreshUI();
    }

    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));

        Panel headerPanel = new Panel(new GridLayout(2));
        headerPanel.addComponent(new Label(String.format("Kredyty: %.2f cr", ship.getCredits())));
        headerPanel.addComponent(new Label("Model: " + ship.getName()));
        headerPanel.addComponent(new Label("Kadłub (HP): " + ship.getCurrentHp() + "/" + ship.getMaxHp()));
        headerPanel.addComponent(new Label("Poj. Ładowni: " + ship.getMaxCargoWeight() + " t"));
        headerPanel.addComponent(new Label("Poj. Paliwa: " + ship.getMaxFuel() + " j."));
        headerPanel.addComponent(new Label("Miejsca załogi: " + ship.getCrew().size() + "/" + ship.getMaxCrew()));
        rootPanel.addComponent(headerPanel.withBorder(Borders.singleLine("OBECNE PARAMETRY STATKU")));
        rootPanel.addComponent(new EmptySpace());

        Panel shipModelsPanel = new Panel(new GridLayout(2));
        for (ShipModel model : ShipData.getAvailableModels()) {
            Panel card = new Panel(new GridLayout(1));
            card.addComponent(new Label(model.getName()));
            card.addComponent(new Label(model.getDescription()));
            card.addComponent(new Label(String.format("Cena: %.2f cr", model.getPrice())));
            card.addComponent(new Label("HP: " + model.getMaxHp()
                    + " | Paliwo: " + model.getMaxFuel()
                    + " | Ladownia: " + model.getMaxCargoWeight() + " t"));
            card.addComponent(new Label("Zaloga: " + model.getMaxCrew()
                    + " | Spalanie: " + model.getFuelPerTurn()));

            Button buyShipButton = new Button("Kup i przesiadz sie", () -> {
                try {
                    shipService.buyShipModel(ship, model);
                    errorMessage = "Przesiadka zakonczona. Ulepszenia starego kadluba zostaly zdemontowane.";
                    refreshUI();
                } catch (RuntimeException e) {
                    errorMessage = e.getMessage();
                    refreshUI();
                }
            });

            if (shipService.isCurrentModel(ship, model)) {
                buyShipButton.setEnabled(false);
                buyShipButton.setLabel("Obecny statek");
            }

            card.addComponent(buyShipButton);
            shipModelsPanel.addComponent(card.withBorder(Borders.singleLine()));
        }

        rootPanel.addComponent(shipModelsPanel.withBorder(Borders.singleLine("DOSTEPNE KADLUBY")));
        rootPanel.addComponent(new EmptySpace());

        Panel upgradesPanel = new Panel(new GridLayout(1));

        for (ShipUpgrade upgrade : availableUpgrades) {
            Panel card = new Panel(new GridLayout(1));
            card.addComponent(new Label(upgrade.getName()));

            // Budowa opisu bonusów
            String bonusInfo = "";
            if (upgrade.getCargoBonus() > 0) bonusInfo += "+ " + upgrade.getCargoBonus() + " t ładowni. ";
            if (upgrade.getHpBonus() > 0) bonusInfo += "+ " + upgrade.getHpBonus() + " max HP. ";
            if (upgrade.getFuelBonus() > 0) bonusInfo += "+ " + upgrade.getFuelBonus() + " j. paliwa.";
            card.addComponent(new Label("Efekt: " + bonusInfo));

            Button buyBtn = new Button(String.format("Zainstaluj (%.2f cr)", upgrade.getPrice()), () -> {
                try {
                    upgradeService.installUpgrade(ship, upgrade);
                    errorMessage = "";
                    refreshUI();
                } catch (RuntimeException e) {
                    errorMessage = e.getMessage();
                    refreshUI();
                }
            });

            // Weryfikacja czy statek posiada już ten moduł
            boolean alreadyHas = ship.getActiveUpgrades().stream()
                    .anyMatch(u -> u.getId().equals(upgrade.getId()));

            if (alreadyHas) {
                buyBtn.setEnabled(false);
                buyBtn.setLabel("Moduł zainstalowany");
            }

            card.addComponent(buyBtn);
            upgradesPanel.addComponent(card.withBorder(Borders.singleLine()));
        }

        rootPanel.addComponent(upgradesPanel.withBorder(Borders.singleLine("OFERTA STOCZNI")));
        rootPanel.addComponent(new EmptySpace());

        if (!errorMessage.isEmpty()) {
            rootPanel.addComponent(new Label("UWAGA: " + errorMessage));
            rootPanel.addComponent(new EmptySpace());
        }

        rootPanel.addComponent(new Button("Wróć do menu stacji", this::close));
        this.setComponent(rootPanel);
    }
}