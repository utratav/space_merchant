package spacemerchant.service;

import spacemerchant.exception.CrewFullException;
import spacemerchant.exception.NotEnoughCreditsException;
import spacemerchant.exception.NotEnoughSpaceException;
import spacemerchant.model.Ship;
import spacemerchant.model.ShipModel;

public class ShipService {

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

    public boolean isCurrentModel(Ship ship, ShipModel model) {
        if (ship.getShipModel() == null || model == null) {
            return false;
        }
        return ship.getShipModel().getId().equals(model.getId());
    }
}
