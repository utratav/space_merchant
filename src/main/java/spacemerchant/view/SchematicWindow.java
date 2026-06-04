package spacemerchant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Ship;
import spacemerchant.service.CrewService;

import java.util.ArrayList;
import java.util.List;

public class SchematicWindow extends BasicWindow {

    public SchematicWindow(Ship ship) {
        super("Space Merchant - Schemat Statku");

        Panel rootPanel = new Panel(new GridLayout(1));

        Panel headerPanel = new Panel(new GridLayout(2));
        headerPanel.addComponent(new Label("STATEK: " + ship.getName()));
        headerPanel.addComponent(new Label("MODEL: " + (ship.getShipModel() != null ? ship.getShipModel().getName() : "Nieznany")));
        rootPanel.addComponent(headerPanel.withBorder(Borders.singleLine("SKAN TECHNICZNY 2.0")));
        rootPanel.addComponent(new EmptySpace());

        Panel bodyPanel = new Panel(new GridLayout(2));

        Panel schematicPanel = new Panel(new GridLayout(1));
        schematicPanel.setPreferredSize(new TerminalSize(48, 20));
        schematicPanel.addComponent(new Label("Widok kadluba / rozmieszczenie stanowisk"));
        schematicPanel.addComponent(new EmptySpace());
        for (String line : buildSchematic(ship)) {
            schematicPanel.addComponent(new Label(line));
        }

        Panel diagnosticsPanel = new Panel(new GridLayout(1));
        if (ship.getShipModel() != null) {
            diagnosticsPanel.addComponent(new Label(ship.getShipModel().getDescription()));
            diagnosticsPanel.addComponent(new EmptySpace());
        }
        diagnosticsPanel.addComponent(createStatusPanel(ship).withBorder(Borders.singleLine("PARAMETRY")));
        diagnosticsPanel.addComponent(new EmptySpace());
        diagnosticsPanel.addComponent(createSystemsPanel(ship).withBorder(Borders.singleLine("SYSTEMY")));
        diagnosticsPanel.addComponent(new EmptySpace());
        diagnosticsPanel.addComponent(createCrewPanel(ship).withBorder(Borders.singleLine("POKLAD ZALOGI")));
        diagnosticsPanel.addComponent(new EmptySpace());
        diagnosticsPanel.addComponent(new Button("Wroc do kokpitu", this::close));

        bodyPanel.addComponent(schematicPanel.withBorder(Borders.singleLine("RZUT Z GORY")));
        bodyPanel.addComponent(diagnosticsPanel.withBorder(Borders.singleLine("DIAGNOSTYKA")));
        rootPanel.addComponent(bodyPanel);

        this.setComponent(rootPanel);
    }

    private Panel createStatusPanel(Ship ship) {
        Panel statusPanel = new Panel(new GridLayout(1));
        statusPanel.addComponent(new Label("Kadlub:  " + UIUtils.drawProgressBar(ship.getCurrentHp(), ship.getMaxHp(), 18)
                + " " + ship.getCurrentHp() + "/" + ship.getMaxHp()));
        statusPanel.addComponent(new Label("Paliwo:  " + UIUtils.drawProgressBar((int) ship.getCurrentFuel(), (int) ship.getMaxFuel(), 18)
                + " " + String.format("%.1f/%.1f", ship.getCurrentFuel(), ship.getMaxFuel())));
        statusPanel.addComponent(new Label("Ladownia:" + UIUtils.drawProgressBar((int) ship.getCargo().getTotalWeight(), (int) ship.getMaxCargoWeight(), 18)
                + " " + String.format("%.1f/%.1f t", ship.getCargo().getTotalWeight(), ship.getMaxCargoWeight())));
        return statusPanel;
    }

    private Panel createCrewPanel(Ship ship) {
        Panel crewPanel = new Panel(new GridLayout(1));
        if (ship.getCrew().isEmpty()) {
            crewPanel.addComponent(new Label("Brak zalogi na pokladzie."));
        } else {
            for (int i = 0; i < ship.getCrew().size(); i++) {
                CrewMember member = ship.getCrew().get(i);
                String status = member.getHp() > 0 ? "OK" : "KRYTYCZNY";
                crewPanel.addComponent(new Label("C" + (i + 1) + " [" + status + "] " + member.getName()
                        + " (" + member.getRole() + ") HP " + member.getHp() + "/" + member.getMaxHp()));
                crewPanel.addComponent(new Label("   PIL " + member.getPiloting()
                        + " | WAL " + member.getCombat()
                        + " | ENG " + member.getEngineering()
                        + " | HAN " + member.getTrade()));
            }
        }
        return crewPanel;
    }

    private Panel createSystemsPanel(Ship ship) {
        Panel systemsPanel = new Panel(new GridLayout(1));
        systemsPanel.addComponent(new Label(systemLine("Mostek", hasLivingPilot(ship), "wymaga zywego pilota")));
        systemsPanel.addComponent(new Label(systemLine("Napęd", ship.getCurrentFuel() > 0, "paliwo w zbiornikach")));
        systemsPanel.addComponent(new Label(systemLine("Ładownia", ship.getCargo().getTotalWeight() <= ship.getMaxCargoWeight(), "limit masy")));
        systemsPanel.addComponent(new EmptySpace());
        systemsPanel.addComponent(new Label(moduleDescription("H+", hasHullUpgrade(ship), "wzmocniony kadlub")));
        systemsPanel.addComponent(new Label(moduleDescription("F+", hasFuelUpgrade(ship), "ulepszone zbiorniki")));
        systemsPanel.addComponent(new Label(moduleDescription("L+", hasCargoUpgrade(ship), "powiekszona ladownia")));
        systemsPanel.addComponent(new EmptySpace());
        systemsPanel.addComponent(new Label("Kody na rysunku: C1.. zaloga, H/F/L moduly."));
        systemsPanel.addComponent(new Label("XX na pokladzie oznacza czlonka zalogi poza walka."));
        return systemsPanel;
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
        if (ship.getCrew().get(index).getHp() <= 0) {
            return "XX";
        }
        return "C" + (index + 1);
    }

    private boolean hasLivingPilot(Ship ship) {
        return ship.getCrew().stream()
                .anyMatch(member -> member.getHp() > 0 && CrewService.ROLE_PILOT.equalsIgnoreCase(member.getRole()));
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

    private String systemLine(String systemName, boolean online, String note) {
        return systemName + ": " + (online ? "ONLINE" : "OFFLINE") + " (" + note + ")";
    }
}
