package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.Ship;
import spacemerchant.model.ShipUpgrade;
import spacemerchant.service.UpgradeService;

import java.util.Arrays;
import java.util.List;

public class ShipyardWindow extends BasicWindow {
    private GuiManager guiManager;
    private Ship ship;
    private UpgradeService upgradeService;
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

        refreshUI();
    }

    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));

        Panel headerPanel = new Panel(new GridLayout(2));
        headerPanel.addComponent(new Label(String.format("Kredyty: %.2f cr", ship.getCredits())));
        headerPanel.addComponent(new Label("Kadłub (HP): " + ship.getCurrentHp() + "/" + ship.getMaxHp()));
        headerPanel.addComponent(new Label("Poj. Ładowni: " + ship.getMaxCargoWeight() + " t"));
        headerPanel.addComponent(new Label("Poj. Paliwa: " + ship.getMaxFuel() + " j."));
        rootPanel.addComponent(headerPanel.withBorder(Borders.singleLine("OBECNE PARAMETRY STATKU")));
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