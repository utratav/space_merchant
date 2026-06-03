package spacemerchant.view;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Ship;

import java.util.ArrayList;
import java.util.List;

public class SchematicWindow extends BasicWindow {

    public SchematicWindow(Ship ship) {
        super("Space Merchant - Schemat Statku");

        Panel rootPanel = new Panel(new GridLayout(2));

        Panel schematicPanel = new Panel(new GridLayout(1));
        for (String line : buildSchematic(ship)) {
            schematicPanel.addComponent(new Label(line));
        }

        Panel legendPanel = new Panel(new GridLayout(1));
        legendPanel.addComponent(new Label("Statek: " + ship.getName()));
        if (ship.getShipModel() != null) {
            legendPanel.addComponent(new Label("Model: " + ship.getShipModel().getDescription()));
        }
        legendPanel.addComponent(new Label("Kadlub: " + ship.getCurrentHp() + "/" + ship.getMaxHp()));
        legendPanel.addComponent(new Label(String.format("Ladownia: %.1f/%.1f t",
                ship.getCargo().getTotalWeight(), ship.getMaxCargoWeight())));
        legendPanel.addComponent(new Label(String.format("Paliwo: %.1f/%.1f j.",
                ship.getCurrentFuel(), ship.getMaxFuel())));
        legendPanel.addComponent(new EmptySpace());

        legendPanel.addComponent(new Label("--- ZALOGA ---"));
        if (ship.getCrew().isEmpty()) {
            legendPanel.addComponent(new Label("Brak zalogi na pokladzie."));
        } else {
            for (int i = 0; i < ship.getCrew().size(); i++) {
                CrewMember member = ship.getCrew().get(i);
                legendPanel.addComponent(new Label("C" + (i + 1) + " - " + member.getName()
                        + " (" + member.getRole() + ")"));
            }
        }

        legendPanel.addComponent(new EmptySpace());
        legendPanel.addComponent(new Label("--- MODULY ---"));
        legendPanel.addComponent(new Label(moduleDescription("H+", hasHullUpgrade(ship), "wzmocniony kadlub")));
        legendPanel.addComponent(new Label(moduleDescription("F+", hasFuelUpgrade(ship), "ulepszone zbiorniki")));
        legendPanel.addComponent(new Label(moduleDescription("L+", hasCargoUpgrade(ship), "powiekszona ladownia")));
        legendPanel.addComponent(new EmptySpace());
        legendPanel.addComponent(new Button("Wroc do kokpitu", this::close));

        rootPanel.addComponent(schematicPanel.withBorder(Borders.singleLine("RZUT Z GORY")));
        rootPanel.addComponent(legendPanel.withBorder(Borders.singleLine("ZNACZNIKI")));

        this.setComponent(rootPanel);
    }

    private List<String> buildSchematic(Ship ship) {
        List<String> templateLines = getTemplateLines(ship);
        List<String> renderedLines = new ArrayList<>();
        int width = 0;

        for (String templateLine : templateLines) {
            String renderedLine = renderSchematicLine(ship, templateLine);
            renderedLines.add(renderedLine);
            width = Math.max(width, renderedLine.length());
        }

        List<String> paddedLines = new ArrayList<>();
        for (String renderedLine : renderedLines) {
            paddedLines.add(String.format("%-" + width + "s", renderedLine));
        }

        return paddedLines;
    }

    private List<String> getTemplateLines(Ship ship) {
        if (ship.getShipModel() != null && !ship.getShipModel().getSchematicLines().isEmpty()) {
            return ship.getShipModel().getSchematicLines();
        }

        return List.of(
                "              /--\\",
                "             /    \\",
                "            /      \\",
                "           || {C1} ||",
                "           ||      ||",
                "          /| {C2}  |\\",
                "         / |      | \\",
                "         | | {H}  | |",
                "         | | {L}  | |",
                "         | | {F}  | |",
                "         \\ | {C3} | /",
                "          \\|      |/",
                "           \\======/",
                "            / || \\",
                "              {C4}"
        );
    }

    private String renderSchematicLine(Ship ship, String templateLine) {
        String line = templateLine
                .replace("{H}", hasHullUpgrade(ship) ? "H+" : "##")
                .replace("{F}", hasFuelUpgrade(ship) ? "F+" : "##")
                .replace("{L}", hasCargoUpgrade(ship) ? "L+" : "##");

        for (int i = 0; i < ship.getMaxCrew(); i++) {
            line = line.replace("{C" + (i + 1) + "}", crewMarker(ship, i));
        }

        return line.replaceAll("\\{C\\d+}", "  ");
    }

    private String crewMarker(Ship ship, int index) {
        if (index >= ship.getCrew().size()) {
            return "--";
        }
        return "C" + (index + 1);
    }

    private boolean hasHullUpgrade(Ship ship) {
        return ship.getActiveUpgrades().stream().anyMatch(upgrade -> upgrade.getHpBonus() > 0);
    }

    private boolean hasFuelUpgrade(Ship ship) {
        return ship.getActiveUpgrades().stream().anyMatch(upgrade -> upgrade.getFuelBonus() > 0);
    }

    private boolean hasCargoUpgrade(Ship ship) {
        return ship.getActiveUpgrades().stream().anyMatch(upgrade -> upgrade.getCargoBonus() > 0);
    }

    private String moduleDescription(String marker, boolean installed, String name) {
        return marker + " - " + name + ": " + (installed ? "aktywny" : "brak");
    }
}
