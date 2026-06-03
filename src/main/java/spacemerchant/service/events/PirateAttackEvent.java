package spacemerchant.service.events;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Ship;
import spacemerchant.service.CrewService;

import java.util.Random;

public class PirateAttackEvent implements EncounterEvent {
    private final Random random = new Random();
    private final CrewService crewService = new CrewService();

    @Override
    public void trigger(Ship ship, GuiManager guiManager) {
        // Podsumowanie statystyk załogi potrzebnych do podjęcia decyzji
        int totalCombat = ship.getCrew().stream()
                .filter(member -> member.getHp() > 0)
                .mapToInt(CrewMember::getCombat)
                .sum();
        int totalPiloting = ship.getCrew().stream()
                .filter(member -> member.getHp() > 0)
                .mapToInt(CrewMember::getPiloting)
                .sum();
        boolean hasPilot = ship.getCrew().stream()
                .anyMatch(member -> member.getHp() > 0 && CrewService.ROLE_PILOT.equalsIgnoreCase(member.getRole()));
        PirateStats pirateStats = generatePirateStats();
        int fightChance = calculateChance(totalCombat, pirateStats.combat(), 45);
        int fleeChance = hasPilot ? calculateChance(totalPiloting, pirateStats.piloting(), 40) : 0;
        int ransomCost = pirateStats.ransomCost();
        int loot = pirateStats.loot();
        int fightDamage = pirateStats.fightDamage();
        int fleeDamage = pirateStats.fleeDamage();
        int stolenCredits = pirateStats.stolenCredits();

        BasicWindow eventWindow = new BasicWindow("ALERT SYSTEMOWY: Atak Piratów!");
        Panel mainPanel = new Panel(new GridLayout(1));

        mainPanel.addComponent(new Label("Z nadprzestrzeni wyskoczył wrogi okręt. Otwierają ogień!"));
        mainPanel.addComponent(new EmptySpace());
        Panel scannerPanel = new Panel(new GridLayout(2));
        scannerPanel.addComponent(new Label("--- TWOJ STATEK ---"));
        scannerPanel.addComponent(new Label("--- PIRACI ---"));
        scannerPanel.addComponent(new Label("Walka: " + totalCombat));
        scannerPanel.addComponent(new Label("Walka: " + pirateStats.combat()));
        scannerPanel.addComponent(new Label("Pilotaż: " + totalPiloting + (hasPilot ? "" : " (brak pilota!)")));
        scannerPanel.addComponent(new Label("Manewry: " + pirateStats.piloting()));
        scannerPanel.addComponent(new Label("Kadłub: " + ship.getCurrentHp() + "/" + ship.getMaxHp()));
        scannerPanel.addComponent(new Label("Pancerz: " + pirateStats.hull()));
        mainPanel.addComponent(scannerPanel.withBorder(Borders.singleLine("ODCZYT TAKTYCZNY")));
        mainPanel.addComponent(new EmptySpace());
        mainPanel.addComponent(new Label("--- SZANSE POWODZENIA ---"));
        mainPanel.addComponent(new Label("Walka: " + fightChance + "%  | sukces: +" + loot + " cr | porażka: -" + fightDamage + " kadłuba"));
        mainPanel.addComponent(new Label("Ucieczka: " + fleeChance + "% | porażka: -" + fleeDamage + " kadłuba i -" + stolenCredits + " cr"));
        mainPanel.addComponent(new Label("Okup: 100% uniknięcia walki, koszt: " + ransomCost + " cr"));
        mainPanel.addComponent(new EmptySpace());

        Panel actionsPanel = new Panel(new GridLayout(1));

        // --- OPCJA 1: WALKA ---
        Button fightBtn = new Button("1. Podejmij walkę (" + fightChance + "%)", () -> {
            crewService.grantCombatExperience(ship, 35);
            if (rollSuccess(fightChance)) {
                ship.setCredits(ship.getCredits() + loot);
                showResult(eventWindow, guiManager, "Zwycięstwo! Piraci rozbici. Zabrano " + loot + " cr łupu.");
            } else {
                ship.setCurrentHp(ship.getCurrentHp() - fightDamage);
                showResult(eventWindow, guiManager, "Porażka! Piraci przebili osłony. Kadłub uszkodzony (-" + fightDamage + " HP).");
            }
        });

        // --- OPCJA 2: UCIECZKA ---
        Button fleeBtn = new Button("2. Próba ucieczki (" + fleeChance + "%)", () -> {
            crewService.grantPilotingExperience(ship, 30);
            if (rollSuccess(fleeChance)) {
                showResult(eventWindow, guiManager, "Ucieczka udana! Pilot wymanewrował wroga i odskoczył.");
            } else {
                ship.setCurrentHp(ship.getCurrentHp() - fleeDamage);
                ship.setCredits(Math.max(0, ship.getCredits() - stolenCredits));
                showResult(eventWindow, guiManager, "Nie udało się uciec! Piraci dogonili statek, zadali uszkodzenia i ukradli " + stolenCredits + " cr.");
            }
        });
        if (!hasPilot) {
            fleeBtn.setEnabled(false);
            fleeBtn.setLabel("2. Ucieczka niemożliwa - brak pilota");
        }

        // --- OPCJA 3: OKUP ---
        Button surrenderBtn = new Button("3. Zapłać okup (" + ransomCost + " cr, 100%)", () -> {
            ship.setCredits(Math.max(0, ship.getCredits() - ransomCost));
            showResult(eventWindow, guiManager, "Zapłacono okup (" + ransomCost + " cr). Piraci odlecieli bez oddawania strzału.");
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

    private void showResult(BasicWindow oldWindow, GuiManager guiManager, String resultMessage) {
        oldWindow.close(); // Bezpiecznie zamykamy okno ataku

        BasicWindow reportWindow = new BasicWindow("Raport Taktyczny");
        Panel panel = new Panel(new GridLayout(1));

        panel.addComponent(new Label(resultMessage));
        if (guiManager != null) {
            panel.addComponent(new Label("Aktualny kadłub: sprawdź status w kokpicie."));
        }
        panel.addComponent(new EmptySpace());
        panel.addComponent(new Button("Zamknij raport", reportWindow::close));

        reportWindow.setComponent(panel);
        reportWindow.setHints(java.util.Collections.singletonList(Window.Hint.MODAL));

        guiManager.showWindow(reportWindow); // Otwieramy czysty raport
    }

    private PirateStats generatePirateStats() {
        int combat = 3 + random.nextInt(8);
        int piloting = 3 + random.nextInt(8);
        int hull = 60 + random.nextInt(61);
        int ransomCost = 180 + combat * 25 + random.nextInt(80);
        int loot = 250 + combat * 55 + random.nextInt(120);
        int fightDamage = 15 + combat * 4;
        int fleeDamage = 8 + piloting * 3;
        int stolenCredits = 80 + combat * 25;
        return new PirateStats(combat, piloting, hull, ransomCost, loot, fightDamage, fleeDamage, stolenCredits);
    }

    private int calculateChance(int playerScore, int pirateScore, int baseChance) {
        return clamp(baseChance + (playerScore - pirateScore) * 8, 5, 95);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private boolean rollSuccess(int chance) {
        return random.nextInt(100) < chance;
    }

    private record PirateStats(
            int combat,
            int piloting,
            int hull,
            int ransomCost,
            int loot,
            int fightDamage,
            int fleeDamage,
            int stolenCredits
    ) {
    }
}