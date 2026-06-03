package spacemerchant.service;

import spacemerchant.exception.CrewFullException;
import spacemerchant.exception.NotEnoughCreditsException;
import spacemerchant.model.CrewMember;
import spacemerchant.model.CrewSkill;
import spacemerchant.model.Item;
import spacemerchant.model.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CrewService {
    public static final String ROLE_PILOT = "Pilot";
    public static final String ROLE_NAVIGATOR = "Nawigator";
    public static final String ROLE_ENGINEER = "Inzynier";
    public static final String ROLE_MECHANIC = "Mechanik";
    public static final String ROLE_TECHNICIAN = "Technik";
    public static final String ROLE_TRADER = "Handlarz";
    public static final String ROLE_NEGOTIATOR = "Negocjator";
    public static final String ROLE_GUNNER = "Strzelec";
    public static final String ROLE_GUARD = "Ochroniarz";
    private final Random random = new Random();

    public void recruitMember(Ship ship, CrewMember member, double recruitmentCost) {
        ensureStationServiceAvailable(ship);

        // Sprawdzenie limitu miejsc
        if (ship.getCrew().size() >= ship.getMaxCrew()) {
            throw new CrewFullException("Brak wolnych koi na statku. Maksymalna załoga: " + ship.getMaxCrew());
        }

        // Sprawdzenie funduszy na rekrutację
        if (!ship.hasEnoughCredits(recruitmentCost)) {
            throw new NotEnoughCreditsException("Niewystarczające środki na zwerbowanie " + member.getName() +
                    ". Potrzeba: " + recruitmentCost);
        }

        // Finalizacja rekrutacji
        ship.setCredits(ship.getCredits() - recruitmentCost);
        ship.addCrewMember(member);
    }


     // Leczy członka załogi do pełnego poziomu HP.
    public void healCrewMember(Ship ship, CrewMember member, double cost) {
        ensureStationServiceAvailable(ship);

        if (!ship.hasEnoughCredits(cost)) {
            throw new NotEnoughCreditsException("Brak kredytów na leczenie w ambulatorium.");
        }

        if (member.getHp() >= member.getMaxHp()) {
            return;
        }

        ship.setCredits(ship.getCredits() - cost);
        member.setHp(member.getMaxHp());
    }

    public void trainCrewMember(Ship ship, CrewMember member, CrewSkill skill, double cost) {
        ensureStationServiceAvailable(ship);

        if (!isAlive(member)) {
            throw new IllegalStateException("Nie można trenować nieprzytomnego członka załogi. Najpierw użyj ambulatorium.");
        }

        if (!member.canImprove(skill)) {
            throw new IllegalArgumentException(member.getName() + " ma już maksymalny poziom: " + skill.getDisplayName());
        }

        if (!ship.hasEnoughCredits(cost)) {
            throw new NotEnoughCreditsException("Brak kredytów na trening. Potrzeba: " + cost);
        }

        ship.setCredits(ship.getCredits() - cost);
        member.increaseSkill(skill);
    }

    // Wypłaty dla załogi
    public void paySalaries(Ship ship) {
        double totalSalaries = 0;

        for (CrewMember member : ship.getCrew()) {
            totalSalaries += member.getSalary();
        }

        if (totalSalaries > 0) {
            double newBalance = ship.getCredits() - totalSalaries;

            ship.setCredits(newBalance);
        }

        if (!ship.isDefeated()) {
            resolveDebtMorale(ship);
        }
    }

    private void resolveDebtMorale(Ship ship) {
        if (ship.getCredits() >= 0 || ship.getCrew().isEmpty()) {
            ship.clearCrewIncidentMessage();
            return;
        }

        double debtPressure = Math.min(0.75, Math.abs(ship.getCredits()) / 650.0);
        if (random.nextDouble() > 0.25 + debtPressure) {
            ship.setCrewIncidentMessage("Załoga narzeka na zaległy żołd. Na razie utrzymujesz dyscyplinę.");
            return;
        }

        int event = random.nextInt(4);
        switch (event) {
            case 0 -> damageShipDuringMutiny(ship);
            case 1 -> injureCrewDuringMutiny(ship);
            case 2 -> dumpCargoDuringMutiny(ship);
            default -> crewMemberLeaves(ship);
        }
    }

    private void damageShipDuringMutiny(Ship ship) {
        int damage = 5 + random.nextInt(16);
        ship.setCurrentHp(ship.getCurrentHp() - damage);
        ship.setCrewIncidentMessage("Bunt załogi: ktoś sabotował instalację. Kadłub -" + damage + " HP.");
    }

    private void injureCrewDuringMutiny(Ship ship) {
        CrewMember victim = randomCrewMember(ship);
        if (victim == null) {
            ship.clearCrewIncidentMessage();
            return;
        }

        int damage = 10 + random.nextInt(21);
        victim.setHp(victim.getHp() - damage);
        ship.setCrewIncidentMessage("Bunt załogi: bójka na pokładzie. " + victim.getName() + " traci " + damage + " HP.");
    }

    private void dumpCargoDuringMutiny(Ship ship) {
        List<Map.Entry<Item, Integer>> cargoEntries = new ArrayList<>(ship.getCargo().getItems().entrySet());
        if (cargoEntries.isEmpty()) {
            damageShipDuringMutiny(ship);
            return;
        }

        Map.Entry<Item, Integer> entry = cargoEntries.get(random.nextInt(cargoEntries.size()));
        int removedAmount = 1 + random.nextInt(Math.max(1, entry.getValue()));
        ship.getCargo().removeItem(entry.getKey(), removedAmount);
        ship.setCrewIncidentMessage("Bunt załogi: część ładunku wyrzucono za burtę. Strata: "
                + entry.getKey().getName() + " x" + removedAmount + ".");
    }

    private void crewMemberLeaves(Ship ship) {
        if (ship.getCrew().size() <= 1) {
            injureCrewDuringMutiny(ship);
            return;
        }

        CrewMember deserter = ship.getCrew().remove(random.nextInt(ship.getCrew().size()));
        ship.setCrewIncidentMessage("Bunt załogi: " + deserter.getName() + " opuszcza statek przy najbliższym śluzie.");
    }

    private CrewMember randomCrewMember(Ship ship) {
        List<CrewMember> livingCrew = new ArrayList<>();
        for (CrewMember member : ship.getCrew()) {
            if (isAlive(member)) {
                livingCrew.add(member);
            }
        }

        if (livingCrew.isEmpty()) {
            return null;
        }

        return livingCrew.get(random.nextInt(livingCrew.size()));
    }

    public boolean hasLivingCrewWithRole(Ship ship, String requiredRole) {
        if (ship == null || ship.getCrew() == null) {
            return false;
        }

        for (CrewMember member : ship.getCrew()) {
            if (isAlive(member) && hasRole(member, requiredRole)) {
                return true;
            }
        }

        return false;
    }

    public boolean isAlive(CrewMember member) {
        return member != null && member.getHp() > 0;
    }

    public void grantPilotingExperience(Ship ship, int amount) {
        grantExperienceForRoles(ship, CrewSkill.PILOTING, amount, ROLE_PILOT, ROLE_NAVIGATOR);
    }

    public void grantCombatExperience(Ship ship, int amount) {
        grantExperienceForRoles(ship, CrewSkill.COMBAT, amount, ROLE_GUNNER, ROLE_GUARD);
    }

    public void grantEngineeringExperience(Ship ship, int amount) {
        grantExperienceForRoles(ship, CrewSkill.ENGINEERING, amount, ROLE_ENGINEER, ROLE_MECHANIC, ROLE_TECHNICIAN);
    }

    public void grantTradeExperience(Ship ship, int amount) {
        grantExperienceForRoles(ship, CrewSkill.TRADE, amount, ROLE_TRADER, ROLE_NEGOTIATOR);
    }

    public void grantExperienceForRoles(Ship ship, CrewSkill skill, int amount, String... roles) {
        if (ship == null || ship.getCrew() == null) {
            return;
        }

        for (CrewMember member : ship.getCrew()) {
            if (!isAlive(member)) {
                continue;
            }

            for (String role : roles) {
                if (hasRole(member, role)) {
                    member.addExperience(skill, amount);
                    break;
                }
            }
        }
    }

    private void ensureStationServiceAvailable(Ship ship) {
        if (ship.getCurrentLocation() == null || !ship.getCurrentLocation().hasStation()) {
            throw new IllegalStateException("Usługa dostępna wyłącznie po zadokowaniu na stacji.");
        }
    }

    private boolean hasRole(CrewMember member, String requiredRole) {
        return member.getRole() != null && member.getRole().equalsIgnoreCase(requiredRole);
    }
}