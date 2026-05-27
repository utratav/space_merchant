package spacemerchant.service.events;

import spacemerchant.controller.GuiManager;
import spacemerchant.model.Ship;

public interface EncounterEvent {
    // Metoda wyzwalająca zdarzenie, z dostępem do statku i GUI
    void trigger(Ship ship, GuiManager guiManager);
}