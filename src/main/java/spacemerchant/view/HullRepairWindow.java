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

public class HullRepairWindow extends BasicWindow {
    private static final Pattern REPAIR_AMOUNT_PATTERN = Pattern.compile("\\d*");

    private final Ship ship;
    private final ShipService shipService;
    private String message = "";

    public HullRepairWindow(Ship ship) {
        super("Warsztat portowy - Naprawa kadłuba");
        this.ship = ship;
        this.shipService = new ShipService();
        refreshUI();
    }

    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));
        double unitPrice = calculateRepairPrice();
        int missingHp = ship.getMaxHp() - ship.getCurrentHp();

        Panel statusPanel = new Panel(new GridLayout(1));
        statusPanel.addComponent(new Label(String.format("Kredyty: %.2f cr", ship.getCredits())));
        statusPanel.addComponent(new Label("Kadłub: " + ship.getCurrentHp() + "/" + ship.getMaxHp()));
        statusPanel.addComponent(new Label(String.format("Cena naprawy: %.2f cr / HP", unitPrice)));
        statusPanel.addComponent(new Label("Uszkodzenia do naprawy: " + missingHp + " HP"));
        rootPanel.addComponent(statusPanel.withBorder(Borders.singleLine("DIAGNOSTYKA KADŁUBA")));
        rootPanel.addComponent(new EmptySpace());

        TextBox amountBox = new TextBox(new TerminalSize(8, 1), "10")
                .setValidationPattern(REPAIR_AMOUNT_PATTERN)
                .setHorizontalFocusSwitching(true)
                .setVerticalFocusSwitching(true);

        Panel repairPanel = new Panel(new GridLayout(2));
        repairPanel.addComponent(new Label("Punkty kadłuba:"));
        repairPanel.addComponent(amountBox);
        repairPanel.addComponent(new Button("Napraw wskazaną ilość", () -> repair(readAmount(amountBox), unitPrice)));
        repairPanel.addComponent(new Button("Napraw do pełna", () -> repair(missingHp, unitPrice)));
        rootPanel.addComponent(repairPanel.withBorder(Borders.singleLine("USŁUGA PORTOWA")));

        if (!message.isEmpty()) {
            rootPanel.addComponent(new EmptySpace());
            rootPanel.addComponent(new Label(message));
        }

        rootPanel.addComponent(new EmptySpace());
        rootPanel.addComponent(new Button("Wróć do menu stacji", this::close));
        this.setComponent(rootPanel);
    }

    private void repair(int amount, double unitPrice) {
        try {
            shipService.repairHull(ship, amount, unitPrice);
            message = "Naprawa kadłuba zakończona.";
            refreshUI();
        } catch (RuntimeException e) {
            message = "UWAGA: " + e.getMessage();
            refreshUI();
        }
    }

    private int readAmount(TextBox amountBox) {
        String rawAmount = amountBox.getText().trim();
        if (rawAmount.isEmpty()) {
            throw new IllegalArgumentException("Podaj liczbę punktów kadłuba większą od zera.");
        }

        return Integer.parseInt(rawAmount);
    }

    private double calculateRepairPrice() {
        if (ship.getCurrentLocation() == null) {
            return 8.0;
        }

        EconomyType economy = ship.getCurrentLocation().getEconomy();
        return switch (economy) {
            case INDUSTRIAL -> 6.0;
            case MINING -> 7.0;
            case HIGH_TECH -> 8.0;
            case AGRICULTURAL -> 9.0;
        };
    }
}
