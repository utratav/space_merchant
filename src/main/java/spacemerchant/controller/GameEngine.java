package spacemerchant.controller;

import spacemerchant.data.ShipData;
import spacemerchant.data.UniverseData;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.view.CockpitWindow;

public class GameEngine {
    private GuiManager guiManager;
    private Ship playerShip;
    private Location startingLocation;

    public GameEngine() {

        this.guiManager = new GuiManager();

        this.startingLocation = UniverseData.getStartingLocation();
        this.playerShip = new Ship(ShipData.getStartingModel(), 1000.0, startingLocation);
    }

    public void start() {

        guiManager.showWindow(new CockpitWindow(guiManager, playerShip));
        guiManager.stop();
    }
}