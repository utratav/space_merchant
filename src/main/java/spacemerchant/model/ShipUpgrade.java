package spacemerchant.model;

public class ShipUpgrade {
    private String id;
    private String name;
    private double price;
    private int hpBonus;
    private double fuelBonus;
    private double cargoBonus;

    public ShipUpgrade(String id, String name, double price, int hpBonus, double fuelBonus, double cargoBonus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.hpBonus = hpBonus;
        this.fuelBonus = fuelBonus;
        this.cargoBonus = cargoBonus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getHpBonus() {
        return hpBonus;
    }

    public void setHpBonus(int hpBonus) {
        this.hpBonus = hpBonus;
    }

    public double getFuelBonus() {
        return fuelBonus;
    }

    public void setFuelBonus(double fuelBonus) {
        this.fuelBonus = fuelBonus;
    }

    public double getCargoBonus() {
        return cargoBonus;
    }

    public void setCargoBonus(double cargoBonus) {
        this.cargoBonus = cargoBonus;
    }
}