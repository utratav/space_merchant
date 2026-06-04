package spacemerchant.service;

import spacemerchant.controller.GuiManager;
import spacemerchant.exception.NotEnoughFuelException;
import spacemerchant.exception.ShipDestroyedException;
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

    public void travel(Ship ship, Location destination) {
        travel(ship, destination, null);
    }

    public void travel(Ship ship, Location destination, GuiManager guiManager) {
        validatePilotReady(ship);

        double cost = ship.getCurrentLocation().getConnectedPaths().getOrDefault(destination, -1.0);

        if (cost < 0) {
            throw new IllegalArgumentException("Brak szlaku do tego systemu!");
        }
        if (ship.getCurrentFuel() < cost) {
            throw new NotEnoughFuelException("Za mało paliwa na ten skok!");
        }

        // Logika biznesowa: Opłaty, spalanie paliwa i zmiana lokacji
        crewService.paySalaries(ship);
        ship.setCurrentFuel(ship.getCurrentFuel() - cost);
        ship.setCurrentLocation(destination);
        crewService.grantPilotingExperience(ship, 25);

        // --- MECHANIKA ZDARZEŃ LOSOWYCH ---
        // Generujemy 35% szansy na napotkanie zdarzenia po drodze
        if (guiManager != null && random.nextDouble() < 0.35) {
            // W przyszłości można tu wylosować event z całej listy. Na razie dajemy Piratów.
            EncounterEvent randomEvent = new PirateAttackEvent();

            // Wrzucamy okno awaryjne na ekran
            guiManager.showWindow(new EncounterDialog(guiManager, ship, randomEvent));
        }
    }

    private void validatePilotReady(Ship ship) {
        if (crewService.hasLivingCrewWithRole(ship, CrewService.ROLE_PILOT)) {
            return;
        }

        Location currentLocation = ship.getCurrentLocation();
        if (currentLocation != null && currentLocation.hasStation()) {
            throw new IllegalStateException("Brak żywego pilota na pokładzie. Zwerbuj pilota w kantynie.");
        }

        String defeatReason = "Statek dryfuje poza stacją bez żywego pilota. Załoga nie jest w stanie wykonać skoku.";
        ship.markDefeated(defeatReason);
        throw new ShipDestroyedException(defeatReason);
    }
}