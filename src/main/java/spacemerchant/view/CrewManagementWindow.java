package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Ship;
import spacemerchant.service.CrewService;

public class CrewManagementWindow extends BasicWindow {

    private GuiManager guiManager;
    private Ship ship;
    private CrewService crewService;
    private String errorMessage = "";

    public CrewManagementWindow(GuiManager guiManager, Ship ship) {
        super("Zarządzanie Załogą");
        this.guiManager = guiManager;
        this.ship = ship;
        this.crewService = new CrewService();

        // Inicjalne zbudowanie interfejsu
        refreshUI();
    }

    // Metoda odświeżająca interfejs po interakcji (np. leczeniu)
    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));

        // Nagłówek
        rootPanel.addComponent(new Label(String.format("Dostępne fundusze: %.2f cr", ship.getCredits())));
        rootPanel.addComponent(new EmptySpace());

        // Panel wyświetlający kafle z członkami załogi (2 kolumny dla czytelności)
        Panel crewGridPanel = new Panel(new GridLayout(2));

        if (ship.getCrew().isEmpty()) {
            crewGridPanel.addComponent(new Label("Brak załogi na pokładzie. Odwiedź kantynę."));
        } else {
            for (CrewMember member : ship.getCrew()) {
                Panel memberPanel = new Panel(new GridLayout(1));

                // Podstawowe info
                memberPanel.addComponent(new Label(member.getName() + " - " + member.getRole().toUpperCase()));

                String hpBar = UIUtils.drawProgressBar(member.getHp(), member.getMaxHp(), 10);
                memberPanel.addComponent(new Label("HP: " + hpBar + " " + member.getHp() + "/" + member.getMaxHp()));

                memberPanel.addComponent(new EmptySpace());

                // Statystyki gwiazdkowe
                memberPanel.addComponent(new Label("Pilotaż:   " + UIUtils.drawStars(member.getPiloting())));
                memberPanel.addComponent(new Label("Walka:     " + UIUtils.drawStars(member.getCombat())));
                memberPanel.addComponent(new Label("Inżynieria:" + UIUtils.drawStars(member.getEngineering())));
                memberPanel.addComponent(new Label("Handel:    " + UIUtils.drawStars(member.getTrade())));

                memberPanel.addComponent(new EmptySpace());

                // Akcje na załogancie (Leczenie)
                double healCost = 100.0; // Bazowy koszt leczenia
                Button healButton = new Button("Ambulatorium (" + healCost + " cr)", () -> {
                    try {
                        crewService.healCrewMember(ship, member, healCost);
                        errorMessage = ""; // Wyczyść ewentualne poprzednie błędy
                        refreshUI();
                    } catch (RuntimeException e) {
                        errorMessage = e.getMessage();
                        refreshUI();
                    }
                });

                // Zablokuj leczenie, jeśli ma pełne HP
                if (member.getHp() >= member.getMaxHp()) {
                    healButton.setEnabled(false);
                    healButton.setLabel("Zdrowie w normie");
                }

                memberPanel.addComponent(healButton);

                // Dodanie ramki do pojedynczego wałoganta i wrzucenie go do siatki
                crewGridPanel.addComponent(memberPanel.withBorder(Borders.singleLine()));
            }
        }

        rootPanel.addComponent(crewGridPanel);
        rootPanel.addComponent(new EmptySpace());

        // Wyświetlanie potencjalnych błędów
        if (!errorMessage.isEmpty()) {
            rootPanel.addComponent(new Label("UWAGA: " + errorMessage));
            rootPanel.addComponent(new EmptySpace());
        }

        // Przycisk wyjścia
        rootPanel.addComponent(new Button("Wróć do kokpitu", this::close));

        this.setComponent(rootPanel);
    }
}