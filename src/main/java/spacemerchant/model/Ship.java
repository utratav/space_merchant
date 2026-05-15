package spacemerchant.model;

public class Ship {
    private String name;
    private double credits;
    private double currentFuel;
    private double maxFuel;
    private double maxCargoWeight;
    private Inventory cargo;
    private Planet currentLocation;
    private int currentHp;
    private int maxHp;
    private int maxCrew;
    private double fuelPerTurn;

    public Ship(String name, double credits, double maxFuel, double maxCargoWeight, Planet currentLocation, int maxHp, int maxCrew, double fuelPerTurn) {
        this.name = name;
        this.credits = credits;
        this.maxFuel = maxFuel;
        this.currentFuel = maxFuel;
        this.maxCargoWeight = maxCargoWeight;
        this.currentLocation = currentLocation;
        this.cargo = new Inventory();
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.maxCrew = maxCrew;
        this.fuelPerTurn = fuelPerTurn;
    }

    public boolean hasEnoughCredits(double amount) {
        return this.credits >= amount;
    }

    public boolean hasAvailableCargoSpace(double additionalWeight) {
        return (this.cargo.getTotalWeight() + additionalWeight) <= this.maxCargoWeight;
    }

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

    public Planet getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Planet currentLocation) {
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
}