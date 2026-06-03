package spacemerchant.service;

import spacemerchant.exception.CrewFullException;
import spacemerchant.exception.NotEnoughCreditsException;
import spacemerchant.model.CrewMember;
import spacemerchant.model.Ship;

public class CrewService {
    public static final String ROLE_PILOT = "Pilot";

    public void recruitMember(Ship ship, CrewMember member, double recruitmentCost) {

        // Zabezpieczenie przed akcjami w trakcie lotu kosmicznego
        if (ship.getCurrentLocation() == null) {
            throw new IllegalStateException("Odmowa dostępu. Statek znajduje się w przestrzeni kosmicznej!");
        }

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

        // Zabezpieczenie przed akcjami w trakcie lotu kosmicznego
        if (ship.getCurrentLocation() == null) {
            throw new IllegalStateException("Odmowa dostępu. Statek znajduje się w przestrzeni kosmicznej!");
        }

        if (!ship.hasEnoughCredits(cost)) {
            throw new NotEnoughCreditsException("Brak kredytów na leczenie w ambulatorium.");
        }

        if (member.getHp() >= member.getMaxHp()) {
            return;
        }

        ship.setCredits(ship.getCredits() - cost);
        member.setHp(member.getMaxHp());
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

    private boolean hasRole(CrewMember member, String requiredRole) {
        return member.getRole() != null && member.getRole().equalsIgnoreCase(requiredRole);
    }
}