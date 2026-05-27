package spacemerchant.controller;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import spacemerchant.model.EconomyType;
import spacemerchant.model.Item;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.view.CockpitWindow;

public class GameEngine {
    private GuiManager guiManager;
    private Ship playerShip;
    private Location startingLocation;
    private Item testItem;

    public GameEngine() {

        this.guiManager = new GuiManager();

        this.startingLocation = new Location("Ziemia", 0, 0, EconomyType.INDUSTRIAL, true);

        Location Mars = new Location("Mars", 10, 10, EconomyType.MINING, true);
        this.startingLocation.addPath(Mars, 15.0);

        this.testItem = new Item("WOD", "Woda", 10.0, 1.0);
        this.playerShip = new Ship("Prometeusz", 1000.0, 100.0, 50.0, startingLocation, 100, 4, 1.0);
    }

    public void start() {

        guiManager.showWindow(new CockpitWindow(guiManager, playerShip));
        guiManager.stop();;
    }
}