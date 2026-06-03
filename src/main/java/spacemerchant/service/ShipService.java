package spacemerchant.service;

import spacemerchant.exception.CrewFullException;
import spacemerchant.exception.NotEnoughCreditsException;
import spacemerchant.exception.NotEnoughSpaceException;
import spacemerchant.model.Ship;
import spacemerchant.model.ShipModel;

public class ShipService {
    private final CrewService crewService = new CrewService();

    public void buyShipModel(Ship ship, ShipModel newModel) {
        if (isCurrentModel(ship, newModel)) {
            throw new IllegalArgumentException("Ten model statku jest juz aktywny.");
        }

        if (!ship.hasEnoughCredits(newModel.getPrice())) {
            throw new NotEnoughCreditsException("Brak kredytow na zakup statku: " + newModel.getName());
        }

        if (ship.getCargo().getTotalWeight() > newModel.getMaxCargoWeight()) {
            throw new NotEnoughSpaceException("Nowy statek ma za mala ladownie na obecny ladunek.");
        }

        if (ship.getCrew().size() > newModel.getMaxCrew()) {
            throw new CrewFullException("Nowy statek ma za malo koi dla obecnej zalogi.");
        }

        ship.setCredits(ship.getCredits() - newModel.getPrice());
        ship.applyShipModel(newModel);
    }

    public void refuel(Ship ship, double amount, double unitPrice) {
        if (ship.getCurrentLocation() == null || !ship.getCurrentLocation().hasStation()) {
            throw new IllegalStateException("Tankowanie jest dostępne wyłącznie w porcie.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Podaj ilość paliwa większą od zera.");
        }

        double missingFuel = ship.getMaxFuel() - ship.getCurrentFuel();
        double fuelToBuy = Math.min(amount, missingFuel);
        if (fuelToBuy <= 0) {
            throw new IllegalArgumentException("Zbiorniki są już pełne.");
        }

        double totalCost = fuelToBuy * unitPrice;
        if (!ship.hasEnoughCredits(totalCost)) {
            throw new NotEnoughCreditsException("Brak kredytów na tankowanie. Potrzeba: " + totalCost);
        }

        ship.setCredits(ship.getCredits() - totalCost);
        ship.setCurrentFuel(ship.getCurrentFuel() + fuelToBuy);
        crewService.grantEngineeringExperience(ship, 10);
    }

    public void repairHull(Ship ship, int amount, double unitPrice) {
        if (ship.getCurrentLocation() == null || !ship.getCurrentLocation().hasStation()) {
            throw new IllegalStateException("Naprawa kadłuba jest dostępna wyłącznie w porcie.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Podaj liczbę punktów kadłuba większą od zera.");
        }

        int missingHp = ship.getMaxHp() - ship.getCurrentHp();
        int hpToRepair = Math.min(amount, missingHp);
        if (hpToRepair <= 0) {
            throw new IllegalArgumentException("Kadłub jest już w pełni sprawny.");
        }

        double totalCost = hpToRepair * unitPrice;
        if (!ship.hasEnoughCredits(totalCost)) {
            throw new NotEnoughCreditsException("Brak kredytów na naprawę. Potrzeba: " + totalCost);
        }

        ship.setCredits(ship.getCredits() - totalCost);
        ship.setCurrentHp(ship.getCurrentHp() + hpToRepair);
        crewService.grantEngineeringExperience(ship, 15);
    }

    public boolean isCurrentModel(Ship ship, ShipModel model) {
        if (ship.getShipModel() == null || model == null) {
            return false;
        }
        return ship.getShipModel().getId().equals(model.getId());
    }
}
