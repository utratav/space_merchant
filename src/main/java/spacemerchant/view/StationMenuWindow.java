package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;

public class StationMenuWindow extends BasicWindow {

    public StationMenuWindow(GuiManager guiManager, Ship ship) {
        super("Space Merchant - Menu Stacji");

        Panel mainPanel = new Panel(new GridLayout(1));
        Location currentLocation = ship.getCurrentLocation();

        // Weryfikacja obecności stacji - blokada okna
        if (currentLocation == null || !currentLocation.hasStation()) {
            mainPanel.addComponent(new Label("ODMOWA DOSTĘPU!"));
            mainPanel.addComponent(new Label("W tej lokalizacji nie znaleziono stacji dokującej ani planety."));
            mainPanel.addComponent(new Label("Usługi giełdy, stoczni oraz kantyny są niedostępne."));
            mainPanel.addComponent(new EmptySpace());
            mainPanel.addComponent(new Button("Wróć do kokpitu", this::close));

            this.setComponent(mainPanel);
            return;
        }

        // Standardowy widok w przypadku wykrycia stacji
        mainPanel.addComponent(new Label("Zadokowano w systemie: " + currentLocation.getName()));
        mainPanel.addComponent(new EmptySpace());

        Panel servicesPanel = new Panel(new GridLayout(1));

        servicesPanel.addComponent(new Button("[1] Giełda Towarowa", () -> {
            guiManager.showWindow(new MarketWindow(guiManager, ship));
        }));

        servicesPanel.addComponent(new Button("[2] Stocznia (Ulepszenia)", () -> {
            guiManager.showWindow(new ShipyardWindow(guiManager, ship));
        }));

        servicesPanel.addComponent(new Button("[3] Kantyna (Załoga)", () -> {
            guiManager.showWindow(new CantinaWindow(guiManager, ship));
        }));

        mainPanel.addComponent(servicesPanel.withBorder(Borders.singleLine("DOSTĘPNE USŁUGI PORTOWE")));
        mainPanel.addComponent(new EmptySpace());

        // Przycisk wyjścia ze stacji
        mainPanel.addComponent(new Button("Odroluj od stacji (Powrót do kokpitu)", this::close));

        this.setComponent(mainPanel);
    }
}