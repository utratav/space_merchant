package spacemerchant.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Location {
    private String name;
    private int x;
    private int y;
    private EconomyType economy;
    private boolean hasStation; // True dla planet/stacji, False dla pustych rozgałęzień

    // Klucz: Połączona lokacja | Wartość: Koszt paliwa za przelot tą trasą
    private Map<Location, Double> connectedPaths;

    public Location(String name, int x, int y, EconomyType economy, boolean hasStation) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.economy = economy;
        this.hasStation = hasStation;
        this.connectedPaths = new LinkedHashMap<>();
    }

    // Metoda do budowania szlaku
    public void addPath(Location destination, double fuelCost) {
        this.connectedPaths.put(destination, fuelCost);
        destination.getConnectedPaths().put(this, fuelCost);
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public EconomyType getEconomy() { return economy; }
    public boolean hasStation() { return hasStation; }
    public Map<Location, Double> getConnectedPaths() { return connectedPaths; }
}