package spacemerchant.controller;

import spacemerchant.view.MainMenuWindow;

public class GameEngine {
    private GuiManager guiManager;

    public GameEngine() {

        this.guiManager = new GuiManager();
    }

    public void start() {

        guiManager.showWindow(new MainMenuWindow(guiManager));
        guiManager.stop();
    }
}