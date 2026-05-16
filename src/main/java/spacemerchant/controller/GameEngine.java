package spacemerchant.controller;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import spacemerchant.model.EconomyType;
import spacemerchant.model.Item;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;

public class GameEngine {
    private GuiManager guiManager;
    private Ship playerShip;
    private Location startingLocation;
    private Item testItem;

    public GameEngine() {

        this.guiManager = new GuiManager();

        this.startingLocation = new Location("Ziemia", 0, 0, EconomyType.INDUSTRIAL, true);
        this.testItem = new Item("WOD", "Woda", 10.0, 1.0);
        this.playerShip = new Ship("Prometeusz", 1000.0, 100.0, 50.0, startingLocation, 100, 4, 1.0);
    }

    public void start() {

        Window mainWindow = new BasicWindow("Space Merchant - Kokpit");

        guiManager.showWindow(mainWindow);

        guiManager.stop();
    }
}