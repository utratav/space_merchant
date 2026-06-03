package spacemerchant.service;

import spacemerchant.exception.CrewFullException;
import spacemerchant.exception.NotEnoughCreditsException;
import spacemerchant.model.CrewMember;
import spacemerchant.model.CrewSkill;
import spacemerchant.model.Ship;

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