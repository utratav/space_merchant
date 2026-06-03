package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.CrewMember;
import spacemerchant.model.CrewSkill;
import spacemerchant.model.Ship;
import spacemerchant.service.CrewService;

public class CrewManagementWindow extends BasicWindow {

    private GuiManager guiManager;
    private Ship ship;
    private CrewService crewService;
    private String errorMessage = "";
    private boolean stationServicesAvailable;

    public CrewManagementWindow(GuiManager guiManager, Ship ship) {
        super("Ambulatorium i Trening Załogi");
        this.guiManager = guiManager;
        this.ship = ship;
        this.crewService = new CrewService();

        // Inicjalne zbudowanie interfejsu
        refreshUI();
    }

    // Metoda odświeżająca interfejs po interakcji (np. leczeniu)
    private void refreshUI() {
        Panel rootPanel = new Panel(new GridLayout(1));
        stationServicesAvailable = ship.getCurrentLocation() != null && ship.getCurrentLocation().hasStation();

        // Nagłówek
        rootPanel.addComponent(new Label(String.format("Dostępne fundusze: %.2f cr", ship.getCredits())));
        rootPanel.addComponent(new Label(stationServicesAvailable
                ? "Status: zadokowano - ambulatorium i sale treningowe aktywne."
                : "Status: poza stacją - tylko podgląd załogi, usługi niedostępne."));
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
                memberPanel.addComponent(new Label(skillLine(member, CrewSkill.PILOTING)));
                memberPanel.addComponent(new Label(skillLine(member, CrewSkill.COMBAT)));
                memberPanel.addComponent(new Label(skillLine(member, CrewSkill.ENGINEERING)));
                memberPanel.addComponent(new Label(skillLine(member, CrewSkill.TRADE)));

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
                if (!stationServicesAvailable) {
                    healButton.setEnabled(false);
                    healButton.setLabel("Ambulatorium tylko na stacji");
                }

                memberPanel.addComponent(healButton);
                memberPanel.addComponent(new EmptySpace());
                memberPanel.addComponent(new Label("Trening: 250 cr za +1 do wybranej statystyki"));
                memberPanel.addComponent(createTrainingButton(member, CrewSkill.PILOTING));
                memberPanel.addComponent(createTrainingButton(member, CrewSkill.COMBAT));
                memberPanel.addComponent(createTrainingButton(member, CrewSkill.ENGINEERING));
                memberPanel.addComponent(createTrainingButton(member, CrewSkill.TRADE));

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

    private Button createTrainingButton(CrewMember member, CrewSkill skill) {
        double trainingCost = 250.0;
        Button trainingButton = new Button("Trenuj " + skill.getDisplayName(), () -> {
            try {
                crewService.trainCrewMember(ship, member, skill, trainingCost);
                errorMessage = "";
                refreshUI();
            } catch (RuntimeException e) {
                errorMessage = e.getMessage();
                refreshUI();
            }
        });

        if (!stationServicesAvailable) {
            trainingButton.setEnabled(false);
            trainingButton.setLabel("Trening tylko na stacji");
        } else if (!member.canImprove(skill)) {
            trainingButton.setEnabled(false);
            trainingButton.setLabel(skill.getDisplayName() + " na maksimum");
        } else if (member.getHp() <= 0) {
            trainingButton.setEnabled(false);
            trainingButton.setLabel("Najpierw ambulatorium");
        }

        return trainingButton;
    }

    private String skillLine(CrewMember member, CrewSkill skill) {
        return String.format("%-11s %s XP %d/100",
                skill.getDisplayName() + ":",
                UIUtils.drawStars(member.getSkillValue(skill)),
                member.getExperience(skill));
    }
}