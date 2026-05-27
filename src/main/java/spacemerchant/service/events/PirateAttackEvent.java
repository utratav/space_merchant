package spacemerchant.service.events;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Ship;

public class PirateAttackEvent implements EncounterEvent {

    @Override
    public void trigger(Ship ship, GuiManager guiManager) {
        // Podsumowanie statystyk załogi potrzebnych do podjęcia decyzji
        int totalCombat = ship.getCrew().stream().mapToInt(CrewMember::getCombat).sum();
        int totalPiloting = ship.getCrew().stream().mapToInt(CrewMember::getPiloting).sum();

        BasicWindow eventWindow = new BasicWindow("ALERT SYSTEMOWY: Atak Piratów!");
        Panel mainPanel = new Panel(new GridLayout(1));

        mainPanel.addComponent(new Label("Z nadprzestrzeni wyskoczył wrogi okręt. Otwierają ogień!"));
        mainPanel.addComponent(new EmptySpace());
        mainPanel.addComponent(new Label("--- ZDOLNOŚCI TWOJEJ ZAŁOGI ---"));
        mainPanel.addComponent(new Label("Siła ognia (Walka): " + totalCombat));
        mainPanel.addComponent(new Label("Manewrowanie (Pilotaż): " + totalPiloting));
        mainPanel.addComponent(new EmptySpace());

        Panel actionsPanel = new Panel(new GridLayout(1));

        // --- OPCJA 1: WALKA ---
        Button fightBtn = new Button("1. Podejmij walkę (Wymaga min. 5 Walki)", () -> {
            if (totalCombat >= 5) {
                ship.setCredits(ship.getCredits() + 500);
                showResult(eventWindow, guiManager, "Zwycięstwo! Zniszczono statek wroga. Zabrano 500 cr łupu.");
            } else {
                ship.setCurrentHp(ship.getCurrentHp() - 30);
                showResult(eventWindow, guiManager, "Porażka! Załoga nie poradziła sobie w walce. Kadłub uszkodzony (-30 HP).");
            }
        });

        // --- OPCJA 2: UCIECZKA ---
        Button fleeBtn = new Button("2. Próba ucieczki (Wymaga min. 5 Pilotażu)", () -> {
            if (totalPiloting >= 5) {
                showResult(eventWindow, guiManager, "Ucieczka udana! Pilot wymanewrował wroga i odskoczył.");
            } else {
                ship.setCurrentHp(ship.getCurrentHp() - 15);
                ship.setCredits(Math.max(0, ship.getCredits() - 200));
                showResult(eventWindow, guiManager, "Nie udało się uciec! Piraci dogonili statek, zadali uszkodzenia i ukradli 200 cr.");
            }
        });

        // --- OPCJA 3: OKUP ---
        Button surrenderBtn = new Button("3. Zapłać okup (Strata 300 cr, bezpiecznie)", () -> {
            ship.setCredits(Math.max(0, ship.getCredits() - 300));
            showResult(eventWindow, guiManager, "Zapłacono okup. Piraci odlecieli bez oddawania strzału.");
        });

        actionsPanel.addComponent(fightBtn);
        actionsPanel.addComponent(fleeBtn);
        actionsPanel.addComponent(surrenderBtn);

        mainPanel.addComponent(actionsPanel.withBorder(Borders.singleLine("WYBIERZ ROZKAZ")));

        eventWindow.setComponent(mainPanel);

        // Zablokuj powrót z okna, dopóki gracz nie podejmie decyzji
        eventWindow.setHints(java.util.Collections.singletonList(Window.Hint.MODAL));
        guiManager.showWindow(eventWindow);
    }

    // Wewnętrzna metoda podmieniająca zawartość okna na raport z potyczki
    // Nowa metoda zamykająca okno walki i otwierająca okno raportu
    private void showResult(BasicWindow oldWindow, GuiManager guiManager, String resultMessage) {
        oldWindow.close(); // Bezpiecznie zamykamy okno ataku

        BasicWindow reportWindow = new BasicWindow("Raport Taktyczny");
        Panel panel = new Panel(new GridLayout(1));

        panel.addComponent(new Label(resultMessage));
        panel.addComponent(new EmptySpace());
        panel.addComponent(new Button("Kontynuuj podróż", reportWindow::close));

        reportWindow.setComponent(panel);
        reportWindow.setHints(java.util.Collections.singletonList(Window.Hint.MODAL));

        guiManager.showWindow(reportWindow); // Otwieramy czysty raport
    }
}