package spacemerchant.service;

import spacemerchant.exception.NotEnoughCreditsException;
import spacemerchant.model.Ship;
import spacemerchant.model.ShipUpgrade;

public class UpgradeService {
    private final CrewService crewService = new CrewService();

    public void installUpgrade(Ship ship, ShipUpgrade upgrade) {
        // Sprawdzenie funduszy
        if (!ship.hasEnoughCredits(upgrade.getPrice())) {
            throw new NotEnoughCreditsException("Niewystarczające środki na instalację modułu: "
                    + upgrade.getName() + ". Potrzeba: " + upgrade.getPrice());
        }

        // Pobranie opłaty za ulepszenie
        ship.setCredits(ship.getCredits() - upgrade.getPrice());

        // Zapisanie ulepszenia na liście aktywnych modułów statku
        ship.addUpgrade(upgrade);

        // Aplikacja bonusów do głównych statystyk statku
        if (upgrade.getHpBonus() > 0) {
            ship.setMaxHp(ship.getMaxHp() + upgrade.getHpBonus());
            // Ulepszenie panczerza dodatkowo trochę leczy hp statku
            ship.setCurrentHp(ship.getCurrentHp() + upgrade.getHpBonus());
        }

        if (upgrade.getFuelBonus() > 0) {
            ship.setMaxFuel(ship.getMaxFuel() + upgrade.getFuelBonus());
        }

        if (upgrade.getCargoBonus() > 0) {
            ship.setMaxCargoWeight(ship.getMaxCargoWeight() + upgrade.getCargoBonus());
        }

        crewService.grantEngineeringExperience(ship, 30);
    }
}