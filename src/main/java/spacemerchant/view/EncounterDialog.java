package spacemerchant.view;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.Ship;
import spacemerchant.service.events.EncounterEvent;

public class EncounterDialog extends BasicWindow {

    public EncounterDialog(GuiManager guiManager, Ship ship, EncounterEvent event) {
        super("!!! CZERWONY ALARM !!!");

        Panel mainPanel = new Panel(new GridLayout(1));

        Label alertLabel = new Label("AWARYJNE WYJŚCIE Z NADPRZESTRZENI!");
        // Kolorujemy tekst na czerwono dla dramatyzmu
        alertLabel.setForegroundColor(TextColor.ANSI.RED);

        mainPanel.addComponent(alertLabel);
        mainPanel.addComponent(new EmptySpace());
        mainPanel.addComponent(new Label("Komputer pokładowy wykrył krytyczną anomalię na trasie przelotu."));
        mainPanel.addComponent(new Label("Wyrzucenie z korytarza czasoprzestrzennego zakończone."));
        mainPanel.addComponent(new EmptySpace());

        // Ten przycisk zamyka alarm i fizycznie ładuje wybrane zdarzenie na ekran
        mainPanel.addComponent(new Button("Sprawdź odczyty czujników", () -> {
            this.close();
            event.trigger(ship, guiManager);
        }));

        this.setComponent(mainPanel);
        // Hint MODAL zapobiega klikaniu poza oknem
        this.setHints(java.util.Collections.singletonList(Window.Hint.MODAL));
    }
}