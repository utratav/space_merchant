package spacemerchant.model;

public class Ship {
    private String name;
    private double credits;
    private double currentFuel;
    private double maxFuel;
    private double maxCargoWeight;
    private Inventory cargo;
    private Planet currentLocation;

    public Ship(String name, double credits, double maxFuel, double maxCargoWeight, Planet currentLocation) {
        this.name = name;
        this.credits = credits;
        this.maxFuel = maxFuel;
        this.currentFuel = maxFuel;
        this.maxCargoWeight = maxCargoWeight;
        this.currentLocation = currentLocation;
        this.cargo = new Inventory();
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
}