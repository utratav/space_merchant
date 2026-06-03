package spacemerchant.model;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private ShipModel shipModel;
    private String name;
    private double credits;
    private double currentFuel;
    private double maxFuel;
    private double maxCargoWeight;
    private Inventory cargo;
    private Location currentLocation;
    private int currentHp;
    private int maxHp;
    private int maxCrew;
    private double fuelPerTurn;
    private List<ShipUpgrade> activeUpgrades;
    private List<CrewMember> crew;
    private boolean defeated;
    private String defeatReason;

    public Ship(String name, double credits, double maxFuel, double maxCargoWeight, Location currentLocation, int maxHp, int maxCrew, double fuelPerTurn) {
        this(new ShipModel("custom", name, "Niestandardowy model statku.", 0.0, maxHp,
                maxFuel, maxCargoWeight, maxCrew, fuelPerTurn, List.of()), credits, currentLocation);
    }

    public Ship(ShipModel shipModel, double credits, Location currentLocation) {
        this.activeUpgrades = new ArrayList<>();
        this.crew = new ArrayList<>();
        this.cargo = new Inventory();
        this.credits = credits;
        this.currentLocation = currentLocation;
        this.defeated = false;
        this.defeatReason = "";
        applyShipModel(shipModel);
    }

    public boolean hasEnoughCredits(double amount) {
        return this.credits >= amount;
    }

    public boolean hasAvailableCargoSpace(double additionalWeight) {
        return (this.cargo.getTotalWeight() + additionalWeight) <= this.maxCargoWeight;
    }

    public void addUpgrade(ShipUpgrade upgrade) {
        this.activeUpgrades.add(upgrade);
    }

    public void applyShipModel(ShipModel shipModel) {
        if (shipModel == null) {
            throw new IllegalArgumentException("Model statku nie moze byc pusty.");
        }

        this.shipModel = shipModel;
        this.name = shipModel.getName();
        this.maxHp = shipModel.getMaxHp();
        this.currentHp = shipModel.getMaxHp();
        this.maxFuel = shipModel.getMaxFuel();
        this.currentFuel = shipModel.getMaxFuel();
        this.maxCargoWeight = shipModel.getMaxCargoWeight();
        this.maxCrew = shipModel.getMaxCrew();
        this.fuelPerTurn = shipModel.getFuelPerTurn();
        this.activeUpgrades.clear();
    }

    public ShipModel getShipModel() {
        return shipModel;
    }

    public List<ShipUpgrade> getActiveUpgrades() {
        return activeUpgrades;
    }

    public List<CrewMember> getCrew() {return crew;}

    public void addCrewMember(CrewMember member) {this.crew.add(member);}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }

    public void setCurrentFuel(double currentFuel) {
        if (currentFuel > maxFuel) {
            this.currentFuel = maxFuel;
        } else if (currentFuel < 0) {
            this.currentFuel = 0;
        } else {
            this.currentFuel = currentFuel;
        }
    }

    public double getMaxFuel() {
        return maxFuel;
    }

    public double getMaxCargoWeight() {
        return maxCargoWeight;
    }

    public Inventory getCargo() {
        return cargo;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        if (this.currentHp > this.maxHp) {
            this.currentHp = this.maxHp;
        }
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        if (currentHp < 0) {
            this.currentHp = 0;
        } else if (currentHp > this.maxHp) {
            this.currentHp = this.maxHp;
        } else {
            this.currentHp = currentHp;
        }

        if (this.currentHp == 0) {
            markDefeated("Kadlub statku zostal zniszczony. Misja zakonczona porazka.");
        }
    }

    public int getMaxCrew() {
        return maxCrew;
    }

    public void setMaxCrew(int maxCrew) {
        this.maxCrew = maxCrew;
    }

    public double getFuelPerTurn() {
        return fuelPerTurn;
    }

    public void setFuelPerTurn(double fuelPerTurn) {
        this.fuelPerTurn = fuelPerTurn;
    }

    public void setMaxFuel(double maxFuel) {this.maxFuel = maxFuel;}

    public void setMaxCargoWeight(double maxCargoWeight) {this.maxCargoWeight = maxCargoWeight;}

    public boolean isDefeated() {
        return defeated;
    }

    public String getDefeatReason() {
        return defeatReason;
    }

    public void markDefeated(String defeatReason) {
        this.defeated = true;
        this.defeatReason = defeatReason;
    }
}