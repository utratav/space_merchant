package spacemerchant.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import spacemerchant.model.CrewMember;
import spacemerchant.model.CrewSkill;
import spacemerchant.model.Item;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.model.ShipModel;
import spacemerchant.model.ShipUpgrade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaveManager {
    private static final String DEFAULT_SAVE_PATH = "savegame.json";

    private final ObjectMapper objectMapper;

    public SaveManager() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveGame(Ship ship) {
        saveGame(ship, DEFAULT_SAVE_PATH);
    }

    public void saveGame(Ship ship, String path) {
        try {
            objectMapper.writeValue(new File(path), ShipSaveData.fromShip(ship));
        } catch (IOException e) {
            throw new RuntimeException("Nie udalo sie zapisac gry do pliku: " + path, e);
        }
    }

    public Ship loadGame() {
        return loadGame(DEFAULT_SAVE_PATH);
    }

    public Ship loadGame(String path) {
        try {
            ShipSaveData saveData = objectMapper.readValue(new File(path), ShipSaveData.class);
            return saveData.toShip();
        } catch (IOException e) {
            throw new RuntimeException("Nie udalo sie wczytac gry z pliku: " + path, e);
        }
    }

    public boolean saveExists() {
        return saveExists(DEFAULT_SAVE_PATH);
    }

    public boolean saveExists(String path) {
        return new File(path).exists();
    }

    public static class ShipSaveData {
        public String shipModelId;
        public String shipName;
        public String currentLocationName;
        public double credits;
        public double currentFuel;
        public double maxFuel;
        public int currentHp;
        public int maxHp;
        public double maxCargoWeight;
        public int maxCrew;
        public double fuelPerTurn;
        public List<CargoItemSaveData> cargo = new ArrayList<>();
        public List<CrewMemberSaveData> crew = new ArrayList<>();
        public List<ShipUpgradeSaveData> activeUpgrades = new ArrayList<>();

        public static ShipSaveData fromShip(Ship ship) {
            ShipSaveData saveData = new ShipSaveData();
            saveData.shipModelId = ship.getShipModel() != null ? ship.getShipModel().getId() : null;
            saveData.shipName = ship.getName();
            saveData.currentLocationName = ship.getCurrentLocation() != null ? ship.getCurrentLocation().getName() : null;
            saveData.credits = ship.getCredits();
            saveData.currentFuel = ship.getCurrentFuel();
            saveData.maxFuel = ship.getMaxFuel();
            saveData.currentHp = ship.getCurrentHp();
            saveData.maxHp = ship.getMaxHp();
            saveData.maxCargoWeight = ship.getMaxCargoWeight();
            saveData.maxCrew = ship.getMaxCrew();
            saveData.fuelPerTurn = ship.getFuelPerTurn();

            for (Map.Entry<Item, Integer> entry : ship.getCargo().getItems().entrySet()) {
                saveData.cargo.add(CargoItemSaveData.fromItem(entry.getKey(), entry.getValue()));
            }

            for (CrewMember member : ship.getCrew()) {
                saveData.crew.add(CrewMemberSaveData.fromCrewMember(member));
            }

            for (ShipUpgrade upgrade : ship.getActiveUpgrades()) {
                saveData.activeUpgrades.add(ShipUpgradeSaveData.fromShipUpgrade(upgrade));
            }

            return saveData;
        }

        public Ship toShip() {
            ShipModel model = findShipModel(shipModelId);
            Location location = findLocation(currentLocationName);
            Ship ship = new Ship(model, credits, location);
            ship.setName(shipName);
            ship.setMaxHp(maxHp);
            ship.setMaxFuel(maxFuel);
            ship.setMaxCargoWeight(maxCargoWeight);
            ship.setMaxCrew(maxCrew);
            ship.setFuelPerTurn(fuelPerTurn);
            ship.setCurrentFuel(currentFuel);
            ship.setCurrentHp(currentHp);

            for (CargoItemSaveData cargoItem : cargo) {
                ship.getCargo().addItem(cargoItem.toItem(), cargoItem.amount);
            }

            for (CrewMemberSaveData crewMember : crew) {
                ship.addCrewMember(crewMember.toCrewMember());
            }

            for (ShipUpgradeSaveData upgrade : activeUpgrades) {
                ship.addUpgrade(upgrade.toShipUpgrade());
            }

            return ship;
        }

        private ShipModel findShipModel(String shipModelId) {
            if (shipModelId != null) {
                for (ShipModel model : ShipData.getAvailableModels()) {
                    if (model.getId().equals(shipModelId)) {
                        return model;
                    }
                }
            }
            return ShipData.getStartingModel();
        }

        private Location findLocation(String locationName) {
            if (locationName != null) {
                for (Location location : UniverseData.getLocations()) {
                    if (location.getName().equals(locationName)) {
                        return location;
                    }
                }
            }
            return UniverseData.getStartingLocation();
        }
    }

    public static class CargoItemSaveData {
        public String id;
        public String name;
        public double basePrice;
        public double weight;
        public int amount;

        public static CargoItemSaveData fromItem(Item item, int amount) {
            CargoItemSaveData saveData = new CargoItemSaveData();
            saveData.id = item.getId();
            saveData.name = item.getName();
            saveData.basePrice = item.getBasePrice();
            saveData.weight = item.getWeight();
            saveData.amount = amount;
            return saveData;
        }

        public Item toItem() {
            return new Item(id, name, basePrice, weight);
        }
    }

    public static class CrewMemberSaveData {
        public String name;
        public String role;
        public int hp;
        public int maxHp;
        public int salary;
        public int piloting;
        public int combat;
        public int engineering;
        public int trade;
        public int pilotingExperience;
        public int combatExperience;
        public int engineeringExperience;
        public int tradeExperience;

        public static CrewMemberSaveData fromCrewMember(CrewMember member) {
            CrewMemberSaveData saveData = new CrewMemberSaveData();
            saveData.name = member.getName();
            saveData.role = member.getRole();
            saveData.hp = member.getHp();
            saveData.maxHp = member.getMaxHp();
            saveData.salary = member.getSalary();
            saveData.piloting = member.getPiloting();
            saveData.combat = member.getCombat();
            saveData.engineering = member.getEngineering();
            saveData.trade = member.getTrade();
            saveData.pilotingExperience = member.getExperience(CrewSkill.PILOTING);
            saveData.combatExperience = member.getExperience(CrewSkill.COMBAT);
            saveData.engineeringExperience = member.getExperience(CrewSkill.ENGINEERING);
            saveData.tradeExperience = member.getExperience(CrewSkill.TRADE);
            return saveData;
        }

        public CrewMember toCrewMember() {
            CrewMember member = new CrewMember(name, role, maxHp, salary, piloting, combat, engineering, trade);
            member.setHp(hp);
            member.setExperience(CrewSkill.PILOTING, pilotingExperience);
            member.setExperience(CrewSkill.COMBAT, combatExperience);
            member.setExperience(CrewSkill.ENGINEERING, engineeringExperience);
            member.setExperience(CrewSkill.TRADE, tradeExperience);
            return member;
        }
    }

    public static class ShipUpgradeSaveData {
        public String id;
        public String name;
        public double price;
        public int hpBonus;
        public double fuelBonus;
        public double cargoBonus;

        public static ShipUpgradeSaveData fromShipUpgrade(ShipUpgrade upgrade) {
            ShipUpgradeSaveData saveData = new ShipUpgradeSaveData();
            saveData.id = upgrade.getId();
            saveData.name = upgrade.getName();
            saveData.price = upgrade.getPrice();
            saveData.hpBonus = upgrade.getHpBonus();
            saveData.fuelBonus = upgrade.getFuelBonus();
            saveData.cargoBonus = upgrade.getCargoBonus();
            return saveData;
        }

        public ShipUpgrade toShipUpgrade() {
            return new ShipUpgrade(id, name, price, hpBonus, fuelBonus, cargoBonus);
        }
    }
}
