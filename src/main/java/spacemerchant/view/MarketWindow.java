package spacemerchant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.Item;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.service.MarketService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MarketWindow extends BasicWindow {

    private GuiManager guiManager;
    private Ship ship;
    private MarketService marketService;
    private Location currentLocation;
    private String errorMessage = ""; // Zmienna przechowująca ewentualne komunikaty o błędach

    // Tymczasowa lista asortymentu dostępnego na każdej stacji (do czasu wdrożenia UniverseData)
    private List<Item> stationOffers = Arrays.asList(
            new Item("WOD", "Czysta Woda", 10.0, 1.0),
            new Item("ZEL", "Ruda Żelaza", 50.0, 5.0),
            new Item("LEK", "Lekarstwa", 200.0, 0.5)
    );

    public MarketWindow(GuiManager guiManager, Ship ship) {
        super("Giełda Towarowa");
        this.guiManager = guiManager;
        this.ship = ship;
        this.marketService = new MarketService();
        this.currentLocation = ship.getCurrentLocation();

        // Inicjalne zbudowanie interfejsu
        refreshUI();
    }

    // Metoda przebudowująca ekran po każdej transakcji
    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));

        // Górny pasek z podstawowymi informacjami
        Panel headerPanel = new Panel(new GridLayout(2));
        headerPanel.addComponent(new Label("Stan konta: " + String.format("%.2f cr", ship.getCredits())));

        double currentWeight = ship.getCargo().getTotalWeight();
        double maxWeight = ship.getMaxCargoWeight();
        headerPanel.addComponent(new Label("Ładownia: " + String.format("%.1f/%.1f t", currentWeight, maxWeight)));

        rootPanel.addComponent(headerPanel);
        rootPanel.addComponent(new EmptySpace());

        // Główny, dwukolumnowy panel ofert
        Panel columnsPanel = new Panel(new GridLayout(2));

        // --- LEWA KOLUMNA (KUPNO) ---
        Panel buyPanel = new Panel(new GridLayout(1));
        for (Item item : stationOffers) {
            double buyPrice = marketService.calculateBuyPrice(ship, item, currentLocation);

            Panel itemPanel = new Panel(new GridLayout(1));
            itemPanel.addComponent(new Label(item.getName() + " (" + item.getWeight() + "t)"));
            itemPanel.addComponent(new Label(String.format("Cena: %.2f cr", buyPrice)));

            itemPanel.addComponent(new Button("Kup 1 szt.", () -> {
                try {
                    marketService.buyItem(ship, item, 1, currentLocation);
                    errorMessage = ""; // Czyszczenie błędu po udanej transakcji
                    refreshUI();
                } catch (RuntimeException e) {
                    errorMessage = e.getMessage();
                    refreshUI();
                }
            }));
            buyPanel.addComponent(itemPanel.withBorder(Borders.singleLine()));
        }
        columnsPanel.addComponent(buyPanel.withBorder(Borders.singleLine("OFERTA STACJI")));

        // --- PRAWA KOLUMNA (SPRZEDAŻ) ---
        Panel sellPanel = new Panel(new GridLayout(1));
        Map<Item, Integer> playerItems = ship.getCargo().getItems();

        if (playerItems.isEmpty()) {
            sellPanel.addComponent(new Label("Twoja ładownia jest pusta."));
        } else {
            for (Map.Entry<Item, Integer> entry : playerItems.entrySet()) {
                Item item = entry.getKey();
                int amount = entry.getValue();
                double sellPrice = marketService.calculateSellPrice(ship, item, currentLocation);

                Panel itemPanel = new Panel(new GridLayout(1));
                itemPanel.addComponent(new Label(item.getName() + " x" + amount));
                itemPanel.addComponent(new Label(String.format("Skup: %.2f cr", sellPrice)));

                itemPanel.addComponent(new Button("Sprzedaj 1 szt.", () -> {
                    try {
                        marketService.sellItem(ship, item, 1, currentLocation);
                        errorMessage = "";
                        refreshUI();
                    } catch (RuntimeException e) {
                        errorMessage = e.getMessage();
                        refreshUI();
                    }
                }));
                sellPanel.addComponent(itemPanel.withBorder(Borders.singleLine()));
            }
        }
        // Upewniamy się, że prawa kolumna zajmuje podobną szerokość, nawet jak jest pusta
        sellPanel.setPreferredSize(new TerminalSize(35, 10));
        columnsPanel.addComponent(sellPanel.withBorder(Borders.singleLine("TWOJA ŁADOWNIA")));

        rootPanel.addComponent(columnsPanel);
        rootPanel.addComponent(new EmptySpace());

        // Panel na powiadomienia i błędy (np. brak kasy)
        if (!errorMessage.isEmpty()) {
            rootPanel.addComponent(new Label("UWAGA: " + errorMessage));
            rootPanel.addComponent(new EmptySpace());
        }

        // Powrót
        rootPanel.addComponent(new Button("Wróć do menu stacji", this::close));

        // Podpinamy wygenerowany ekran
        this.setComponent(rootPanel);
    }
}