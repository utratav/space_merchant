package spacemerchant.controller;

import spacemerchant.data.ShipData;
import spacemerchant.model.EconomyType;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.view.CockpitWindow;

public class GameEngine {
    private GuiManager guiManager;
    private Ship playerShip;
    private Location startingLocation;

    public GameEngine() {

        this.guiManager = new GuiManager();

        this.startingLocation = new Location("Ziemia", 0, 0, EconomyType.INDUSTRIAL, true);

        Location Mars = new Location("Mars", 10, 10, EconomyType.MINING, true);
        this.startingLocation.addPath(Mars, 15.0);

        this.playerShip = new Ship(ShipData.getStartingModel(), 1000.0, startingLocation);
    }

    public void start() {

        guiManager.showWindow(new CockpitWindow(guiManager, playerShip));
        guiManager.stop();
    }
}