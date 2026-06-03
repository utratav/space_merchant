package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.Item;
import spacemerchant.model.Ship;

import java.util.Map;

public class InventoryWindow extends BasicWindow {

    public InventoryWindow(GuiManager guiManager, Ship ship) {
        super("Space Merchant - Ekwipunek i Ładownia");

        Panel mainPanel = new Panel(new GridLayout(1));

        // --- Licznik zajętości ładowni ---
        Panel headerPanel = new Panel(new GridLayout(2));
        double currentWeight = ship.getCargo().getTotalWeight();
        double maxWeight = ship.getMaxCargoWeight();

        headerPanel.addComponent(new Label("Zajętość przestrzeni: "));
        String cargoBar = UIUtils.drawProgressBar((int) currentWeight, (int) maxWeight, 20);
        headerPanel.addComponent(new Label(cargoBar + String.format(" %.1f / %.1f t", currentWeight, maxWeight)));

        mainPanel.addComponent(headerPanel);
        mainPanel.addComponent(new EmptySpace());

        // --- Tabela asortymentu ---
        Table<String> inventoryTable = new Table<>("Przedmiot", "Ilość", "Wartość", "Opis");

        Map<Item, Integer> items = ship.getCargo().getItems();

        if (items.isEmpty()) {
            inventoryTable.getTableModel().addRow("Brak towarów", "-", "-", "-");
        } else {
            fillInventoryTable(items, inventoryTable);
        }

        mainPanel.addComponent(inventoryTable.withBorder(Borders.singleLine("ZAWARTOŚĆ ŁADOWNI")));
        mainPanel.addComponent(new EmptySpace());

        // --- Powrót ---
        mainPanel.addComponent(new Button("Wróć do kokpitu", this::close));

        this.setComponent(mainPanel);
    }

    private static void fillInventoryTable(Map<Item, Integer> items, Table<String> inventoryTable) {
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int amount = entry.getValue();

            inventoryTable.getTableModel().addRow(
                    item.getName() + " [" + item.getId() + "]",
                    String.valueOf(amount),
                    String.format("%.2f cr", item.getBasePrice() * amount),
                    String.format("Waga: %.2f t/szt.", item.getWeight())
            );
        }
    }
}