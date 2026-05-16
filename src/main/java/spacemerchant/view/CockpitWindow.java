package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.Ship;

public class CockpitWindow extends BasicWindow {

    private GuiManager guiManager;
    private Ship ship;

    public CockpitWindow(GuiManager guiManager, Ship ship) {
        super("Space Merchant - Kokpit");
        this.guiManager = guiManager;
        this.ship = ship;

        Panel mainPanel = new Panel(new GridLayout(2));

        // Lewa strona okna
        Panel actionMenuPanel = new Panel(new GridLayout(1));
        actionMenuPanel.addComponent(new EmptySpace());

        // Przyciski
        actionMenuPanel.addComponent(new Button("[1] Otwórz Ekwipunek i Ładownię", () -> {
            // Tutaj w przyszłości odpalimy InventoryWindow
        }));
        actionMenuPanel.addComponent(new Button("[2] Mapa Gwiezdna", () -> {
            // Otworzy StarMapWindow
        }));
        actionMenuPanel.addComponent(new Button("[3] Zarządzanie Załogą", () -> {
            // Otworzy CrewManagementWindow
        }));
        actionMenuPanel.addComponent(new Button("[4] Zobacz schemat statku", () -> {
            // Otworzy SchematicWindow
        }));

        mainPanel.addComponent(actionMenuPanel.withBorder(Borders.singleLine("WYBIERZ AKCJĘ")));

        // Prawa strona
        Panel statsPanel = new Panel();
        statsPanel.addComponent(new Label("Statystyki"));
        mainPanel.addComponent(statsPanel.withBorder(Borders.singleLine("STATUS")));

        // Ustawienie głównego panelu jako fizyczna zawartość tego okna
        this.setComponent(mainPanel);
    }
}