package spacemerchant.model;

public class Planet {
    private String name;
    private int x;
    private int y;
    private EconomyType economy;

    public Planet(String name, int x, int y, EconomyType economy) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.economy = economy;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public EconomyType getEconomy() {
        return economy;
    }
}