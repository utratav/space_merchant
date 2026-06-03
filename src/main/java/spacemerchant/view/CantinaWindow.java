package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Ship;
import spacemerchant.service.CrewRecruitFactory;
import spacemerchant.service.CrewService;

import java.util.List;

public class CantinaWindow extends BasicWindow {
    private static final int RECRUITS_AVAILABLE_AT_ONCE = 6;

    private GuiManager guiManager;
    private Ship ship;
    private CrewService crewService;
    private CrewRecruitFactory crewRecruitFactory;
    private String errorMessage = "";
    private List<CrewMember> availableRecruits;

    public CantinaWindow(GuiManager guiManager, Ship ship) {
        super("Kantyna - Werbunek Najemników");
        this.guiManager = guiManager;
        this.ship = ship;
        this.crewService = new CrewService();
        this.crewRecruitFactory = new CrewRecruitFactory();
        this.availableRecruits = crewRecruitFactory.generateRecruitPool(RECRUITS_AVAILABLE_AT_ONCE);

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
                    availableRecruits.remove(recruit);
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