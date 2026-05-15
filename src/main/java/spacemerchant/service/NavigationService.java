package spacemerchant.service;

import spacemerchant.exception.NotEnoughFuelException;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;

public class NavigationService {

    private CrewService crewService;

    public NavigationService(CrewService crewService) {
        this.crewService = crewService;
    }

    public void travel(Ship ship, Location destination) {
        Location current = ship.getCurrentLocation();

        if (current != null && current.equals(destination)) {
            throw new IllegalArgumentException("Kapitanie, już znajdujemy się na orbicie tej lokacji!");
        }

        // Sprawdzenie istnieje wyznaczony szlak z obecnego miejsca do celu
        if (!current.getConnectedPaths().containsKey(destination)) {
            throw new IllegalArgumentException("Brak szlaku nadprzestrzennego łączącego te dwa systemy!");
        }

        // Pobranie kosztu paliwa dla wybranej trasy
        double fuelCost = current.getConnectedPaths().get(destination);

        // Walidacja paliwa
        if (ship.getCurrentFuel() < fuelCost) {
            throw new NotEnoughFuelException(String.format("Brak paliwa na wykonanie skoku. Wymagane: %.2f j.", fuelCost));
        }

        // Odlot
        ship.setCurrentLocation(null);

        // Pobranie paliwa
        ship.setCurrentFuel(ship.getCurrentFuel() - fuelCost);

        crewService.paySalaries(ship);

        // Przylot
        ship.setCurrentLocation(destination);
    }
}