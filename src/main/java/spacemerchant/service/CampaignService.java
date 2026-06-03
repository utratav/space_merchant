package spacemerchant.service;

import spacemerchant.data.ShipData;
import spacemerchant.data.UniverseData;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.model.ShipModel;

import java.util.ArrayList;
import java.util.List;

public class CampaignService {
    public static final String FINAL_DESTINATION = "Nowy Eden";
    public static final double REQUIRED_CREDITS = 10000.0;
    public static final int REQUIRED_UPGRADES = 3;

    public List<String> getObjectiveDescriptions() {
        return List.of(
                "Dotrzyj do kolonii " + FINAL_DESTINATION + ".",
                "Odkryj całą mapę szlaków.",
                "Zgromadź " + String.format("%.0f", REQUIRED_CREDITS) + " cr kapitału.",
                "Kup najlepszy dostępny kadłub statku.",
                "Zainstaluj komplet " + REQUIRED_UPGRADES + " modułów ulepszeń.",
                "Utrzymaj przy życiu co najmniej jednego członka załogi."
        );
    }

    public List<String> getObjectiveProgress(Ship ship) {
        List<String> progress = new ArrayList<>();
        progress.add(statusLine(isAtFinalDestination(ship), "Dotrzyj do " + FINAL_DESTINATION));
        progress.add(statusLine(hasDiscoveredWholeMap(ship), "Odkryj mapę: "
                + ship.getVisitedLocationNames().size() + "/" + UniverseData.getLocations().size()));
        progress.add(statusLine(hasRequiredCredits(ship), String.format("Kapitał: %.0f/%.0f cr",
                ship.getCredits(), REQUIRED_CREDITS)));
        progress.add(statusLine(hasBestShip(ship), "Najlepszy kadłub: " + getBestShipModel().getName()));
        progress.add(statusLine(hasRequiredUpgrades(ship), "Moduły: "
                + ship.getActiveUpgrades().size() + "/" + REQUIRED_UPGRADES));
        progress.add(statusLine(hasLivingCrew(ship), "Żywa załoga na pokładzie"));
        return progress;
    }

    public boolean checkVictory(Ship ship) {
        if (ship.isVictorious()) {
            return true;
        }

        boolean victory = isAtFinalDestination(ship)
                && hasDiscoveredWholeMap(ship)
                && hasRequiredCredits(ship)
                && hasBestShip(ship)
                && hasRequiredUpgrades(ship)
                && hasLivingCrew(ship);

        if (victory) {
            ship.markVictorious("Kontrakt galaktyczny zakończony. Dotarłeś do Nowego Edenu z elitarnym statkiem, pełną mapą i kapitałem.");
        }

        return victory;
    }

    private boolean isAtFinalDestination(Ship ship) {
        Location location = ship.getCurrentLocation();
        return location != null && FINAL_DESTINATION.equals(location.getName());
    }

    private boolean hasDiscoveredWholeMap(Ship ship) {
        return ship.getVisitedLocationNames().size() >= UniverseData.getLocations().size();
    }

    private boolean hasRequiredCredits(Ship ship) {
        return ship.getCredits() >= REQUIRED_CREDITS;
    }

    private boolean hasBestShip(Ship ship) {
        return ship.getShipModel() != null && ship.getShipModel().getId().equals(getBestShipModel().getId());
    }

    private ShipModel getBestShipModel() {
        ShipModel bestModel = ShipData.getStartingModel();
        for (ShipModel model : ShipData.getAvailableModels()) {
            if (model.getPrice() > bestModel.getPrice()) {
                bestModel = model;
            }
        }
        return bestModel;
    }

    private boolean hasRequiredUpgrades(Ship ship) {
        return ship.getActiveUpgrades().size() >= REQUIRED_UPGRADES;
    }

    private boolean hasLivingCrew(Ship ship) {
        for (CrewMember member : ship.getCrew()) {
            if (member.getHp() > 0) {
                return true;
            }
        }
        return false;
    }

    private String statusLine(boolean complete, String text) {
        return (complete ? "[X] " : "[ ] ") + text;
    }
}
