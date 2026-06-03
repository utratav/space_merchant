package spacemerchant.model;

import java.util.ArrayList;
import java.util.List;

public class ShipModel {
    private String id;
    private String name;
    private String description;
    private double price;
    private int maxHp;
    private double maxFuel;
    private double maxCargoWeight;
    private int maxCrew;
    private double fuelPerTurn;
    private List<String> schematicLines;

    public ShipModel() {
        this.schematicLines = new ArrayList<>();
    }

    public ShipModel(String id, String name, String description, double price, int maxHp,
                     double maxFuel, double maxCargoWeight, int maxCrew, double fuelPerTurn,
                     List<String> schematicLines) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.maxHp = maxHp;
        this.maxFuel = maxFuel;
        this.maxCargoWeight = maxCargoWeight;
        this.maxCrew = maxCrew;
        this.fuelPerTurn = fuelPerTurn;
        this.schematicLines = new ArrayList<>(schematicLines);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public double getMaxFuel() {
        return maxFuel;
    }

    public void setMaxFuel(double maxFuel) {
        this.maxFuel = maxFuel;
    }

    public double getMaxCargoWeight() {
        return maxCargoWeight;
    }

    public void setMaxCargoWeight(double maxCargoWeight) {
        this.maxCargoWeight = maxCargoWeight;
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

    public List<String> getSchematicLines() {
        return schematicLines;
    }

    public void setSchematicLines(List<String> schematicLines) {
        this.schematicLines = new ArrayList<>(schematicLines);
    }
}
