package spacemerchant.service;

import spacemerchant.controller.GuiManager;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.service.events.EncounterEvent;
import spacemerchant.service.events.PirateAttackEvent;
import spacemerchant.view.EncounterDialog;

import java.util.Random;

public class NavigationService {
    private CrewService crewService;
    private Random random;

    public NavigationService(CrewService crewService) {
        this.crewService = crewService;
        this.random = new Random();
    }

    // Dodano GuiManager do parametrów metody!
    public void travel(Ship ship, Location destination, GuiManager guiManager) {
        double cost = ship.getCurrentLocation().getConnectedPaths().getOrDefault(destination, -1.0);

        if (cost < 0) {
            throw new RuntimeException("Brak bezpośredniego szlaku do tego systemu!");
        }
        if (ship.getCurrentFuel() < cost) {
            throw new RuntimeException("Za mało paliwa na ten skok!");
        }

        // Logika biznesowa: Opłaty, spalanie paliwa i zmiana lokacji
        crewService.paySalaries(ship);
        ship.setCurrentFuel(ship.getCurrentFuel() - cost);
        ship.setCurrentLocation(destination);

        // --- MECHANIKA ZDARZEŃ LOSOWYCH ---
        // Generujemy 35% szansy na napotkanie zdarzenia po drodze
        if (random.nextDouble() < 0.35) {
            // W przyszłości można tu wylosować event z całej listy. Na razie dajemy Piratów.
            EncounterEvent randomEvent = new PirateAttackEvent();

            // Wrzucamy okno awaryjne na ekran
            guiManager.showWindow(new EncounterDialog(guiManager, ship, randomEvent));
        }
    }
}