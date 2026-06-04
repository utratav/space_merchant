package spacemerchant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import spacemerchant.model.EconomyType;
import spacemerchant.model.Ship;
import spacemerchant.service.ShipService;

import java.util.regex.Pattern;

public class RefuelWindow extends BasicWindow {
    private static final Pattern FUEL_AMOUNT_PATTERN = Pattern.compile("\\d*");

    private final Ship ship;
    private final ShipService shipService;
    private String message = "";

    public RefuelWindow(Ship ship) {
        super("Port paliwowy - Tankowanie");
        this.ship = ship;
        this.shipService = new ShipService();
        refreshUI();
    }

    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));
        double unitPrice = calculateFuelPrice();
        double missingFuel = ship.getMaxFuel() - ship.getCurrentFuel();

        Panel statusPanel = new Panel(new GridLayout(1));
        statusPanel.addComponent(new Label(String.format("Kredyty: %.2f cr", ship.getCredits())));
        statusPanel.addComponent(new Label(String.format("Paliwo: %.1f/%.1f j.", ship.getCurrentFuel(), ship.getMaxFuel())));
        statusPanel.addComponent(new Label(String.format("Cena paliwa: %.2f cr / j.", unitPrice)));
        statusPanel.addComponent(new Label(String.format("Brakuje do pełna: %.1f j.", missingFuel)));
        rootPanel.addComponent(statusPanel.withBorder(Borders.singleLine("STATUS TANKOWANIA")));
        rootPanel.addComponent(new EmptySpace());

        TextBox amountBox = new TextBox(new TerminalSize(8, 1), "10")
                .setValidationPattern(FUEL_AMOUNT_PATTERN)
                .setHorizontalFocusSwitching(true)
                .setVerticalFocusSwitching(true);

        Panel customPanel = new Panel(new GridLayout(2));
        customPanel.addComponent(new Label("Ilość paliwa:"));
        customPanel.addComponent(amountBox);
        customPanel.addComponent(new Button("Zatankuj wskazaną ilość", () -> refuel(readAmount(amountBox), unitPrice)));
        customPanel.addComponent(new Button("Zatankuj do pełna", () -> refuel(missingFuel, unitPrice)));
        rootPanel.addComponent(customPanel.withBorder(Borders.singleLine("USŁUGA PORTOWA")));

        if (!message.isEmpty()) {
            rootPanel.addComponent(new EmptySpace());
            rootPanel.addComponent(new Label(message));
        }

        rootPanel.addComponent(new EmptySpace());
        rootPanel.addComponent(new Button("Wróć do menu stacji", this::close));
        this.setComponent(rootPanel);
    }

    private void refuel(double amount, double unitPrice) {
        try {
            shipService.refuel(ship, amount, unitPrice);
            message = "Tankowanie zakończone.";
            refreshUI();
        } catch (RuntimeException e) {
            message = "UWAGA: " + e.getMessage();
            refreshUI();
        }
    }

    private double readAmount(TextBox amountBox) {
        String rawAmount = amountBox.getText().trim();
        if (rawAmount.isEmpty()) {
            throw new IllegalArgumentException("Podaj ilość paliwa większą od zera.");
        }

        return Double.parseDouble(rawAmount);
    }

    private double calculateFuelPrice() {
        if (ship.getCurrentLocation() == null) {
            return 6.0;
        }

        EconomyType economy = ship.getCurrentLocation().getEconomy();
        return switch (economy) {
            case MINING -> 4.0;
            case INDUSTRIAL -> 5.0;
            case AGRICULTURAL -> 5.5;
            case HIGH_TECH -> 6.5;
        };
    }
}
