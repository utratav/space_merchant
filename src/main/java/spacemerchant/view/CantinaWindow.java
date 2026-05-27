package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Ship;
import spacemerchant.service.CrewService;

import java.util.Arrays;
import java.util.List;

public class CantinaWindow extends BasicWindow {
    private GuiManager guiManager;
    private Ship ship;
    private CrewService crewService;
    private String errorMessage = "";

    // Tymczasowa lista dostępnych najemników na stacji
    private List<CrewMember> availableRecruits = Arrays.asList(
            new CrewMember("Han", "Pilot", 100, 50, 5, 3, 1, 2),
            new CrewMember("Chewie", "Inżynier", 150, 70, 3, 4, 5, 1),
            new CrewMember("Lando", "Handlarz", 80, 100, 4, 2, 1, 5)
    );

    public CantinaWindow(GuiManager guiManager, Ship ship) {
        super("Kantyna - Werbunek Najemników");
        this.guiManager = guiManager;
        this.ship = ship;
        this.crewService = new CrewService();

        refreshUI();
    }

    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));

        Panel headerPanel = new Panel(new GridLayout(2));
        headerPanel.addComponent(new Label(String.format("Dostępne fundusze: %.2f cr", ship.getCredits())));
        headerPanel.addComponent(new Label(String.format("Obecna załoga: %d/%d", ship.getCrew().size(), ship.getMaxCrew())));
        rootPanel.addComponent(headerPanel);
        rootPanel.addComponent(new EmptySpace());

        // Siatka postaci w dwóch kolumnach
        Panel recruitsPanel = new Panel(new GridLayout(2));

        for (CrewMember recruit : availableRecruits) {
            Panel card = new Panel(new GridLayout(1));
            card.addComponent(new Label(recruit.getName() + " - " + recruit.getRole().toUpperCase()));
            card.addComponent(new Label("HP: " + recruit.getMaxHp() + " | Żołd: " + recruit.getSalary() + " cr/skok"));
            card.addComponent(new Label("Pilotaż:   " + UIUtils.drawStars(recruit.getPiloting())));
            card.addComponent(new Label("Walka:     " + UIUtils.drawStars(recruit.getCombat())));
            card.addComponent(new Label("Inżynieria:" + UIUtils.drawStars(recruit.getEngineering())));
            card.addComponent(new Label("Handel:    " + UIUtils.drawStars(recruit.getTrade())));

            double recruitCost = recruit.getSalary() * 2.0; // Prowizja za werbunek = 2x żołd
            Button recruitBtn = new Button(String.format("Zwerbuj (%.2f cr)", recruitCost), () -> {
                try {
                    crewService.recruitMember(ship, recruit, recruitCost);
                    errorMessage = "";
                    refreshUI();
                } catch (RuntimeException e) {
                    errorMessage = e.getMessage();
                    refreshUI();
                }
            });

            // Weryfikacja wizualna ilości miejsc
            if (ship.getCrew().size() >= ship.getMaxCrew()) {
                recruitBtn.setEnabled(false);
                recruitBtn.setLabel("Brak miejsc w kojach");
            }

            card.addComponent(recruitBtn);
            recruitsPanel.addComponent(card.withBorder(Borders.singleLine()));
        }

        rootPanel.addComponent(recruitsPanel.withBorder(Borders.singleLine("DOSTĘPNI W KANTYNIE")));
        rootPanel.addComponent(new EmptySpace());

        if (!errorMessage.isEmpty()) {
            rootPanel.addComponent(new Label("UWAGA: " + errorMessage));
            rootPanel.addComponent(new EmptySpace());
        }

        rootPanel.addComponent(new Button("Wróć do menu stacji", this::close));
        this.setComponent(rootPanel);
    }
}