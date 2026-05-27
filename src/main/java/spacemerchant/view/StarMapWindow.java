package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.service.CrewService;
import spacemerchant.service.NavigationService;

import java.util.Map;

public class StarMapWindow extends BasicWindow {
    private GuiManager guiManager;
    private Ship ship;
    private NavigationService navigationService;
    private String errorMessage = "";

    public StarMapWindow(GuiManager guiManager, Ship ship) {
        super("Mapa Gwiezdna - Nawigacja");
        this.guiManager = guiManager;
        this.ship = ship;
        // NavigationService potrzebuje CrewService do opłacania załogi podczas podróży
        this.navigationService = new NavigationService(new CrewService());

        refreshUI();
    }

    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));

        Location currentLoc = ship.getCurrentLocation();

        // Górny panel informacyjny
        Panel headerPanel = new Panel(new GridLayout(1));
        headerPanel.addComponent(new Label(String.format("Dostępne paliwo: %.2f j.", ship.getCurrentFuel())));
        headerPanel.addComponent(new Label("Obecny sektor: " + (currentLoc != null ? currentLoc.getName() : "Lot w toku...")));
        rootPanel.addComponent(headerPanel.withBorder(Borders.singleLine("STATUS NAWIGACJI")));
        rootPanel.addComponent(new EmptySpace());

        // Panel dostępnych szlaków
        Panel destinationsPanel = new Panel(new GridLayout(1));

        if (currentLoc != null) {
            Map<Location, Double> paths = currentLoc.getConnectedPaths();

            if (paths.isEmpty()) {
                destinationsPanel.addComponent(new Label("Brak zbadanych szlaków z tego sektora. Jesteś uwięziony!"));
            } else {
                for (Map.Entry<Location, Double> entry : paths.entrySet()) {
                    Location destination = entry.getKey();
                    Double fuelCost = entry.getValue();

                    Panel pathPanel = new Panel(new GridLayout(2));
                    pathPanel.addComponent(new Label(destination.getName() + " (Ekonomia: " + destination.getEconomy() + ")"));

                    Button jumpButton = new Button(String.format("Skok (%.1f paliwa)", fuelCost), () -> {
                        try {
                            // TUTAJ ZMIANA: dodajemy guiManager jako 3 argument
                            navigationService.travel(ship, destination, guiManager);
                            this.close();
                        } catch (RuntimeException e) {
                            errorMessage = e.getMessage();
                            refreshUI();
                        }
                    });

                    // Zabezpieczenie wizualne - blokada jeśli brakuje paliwa
                    if (ship.getCurrentFuel() < fuelCost) {
                        jumpButton.setEnabled(false);
                        jumpButton.setLabel("Brak paliwa");
                    }

                    pathPanel.addComponent(jumpButton);
                    destinationsPanel.addComponent(pathPanel);
                }
            }
        }

        rootPanel.addComponent(destinationsPanel.withBorder(Borders.singleLine("ZNANE SZLAKI NADPRZESTRZENNE")));
        rootPanel.addComponent(new EmptySpace());

        if (!errorMessage.isEmpty()) {
            rootPanel.addComponent(new Label("BŁĄD KOMPUTERA POKŁADOWEGO: " + errorMessage));
            rootPanel.addComponent(new EmptySpace());
        }

        rootPanel.addComponent(new Button("Wróć do kokpitu", this::close));
        this.setComponent(rootPanel);
    }
}