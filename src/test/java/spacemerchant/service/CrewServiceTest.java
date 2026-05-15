package spacemerchant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spacemerchant.exception.CrewFullException;
import spacemerchant.exception.NotEnoughCreditsException;
import spacemerchant.model.CrewMember;
import spacemerchant.model.EconomyType;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;

import static org.junit.jupiter.api.Assertions.*;

class CrewServiceTest {

    private CrewService crewService;
    private Ship testShip;
    private CrewMember testMember;

    @BeforeEach
    void setUp() {
        crewService = new CrewService();
        Location testLocation = new Location("Ziemia", 0, 0, EconomyType.INDUSTRIAL, true);

        testShip = new Ship("Prometeusz", 1000.0, 100.0, 50.0, testLocation, 100, 2, 1.0);

        testMember = new CrewMember("Jan", "Pilot", 100, 50, 5, 2, 1, 3);
    }

    @Test
    void shouldSuccessfullyRecruitMemberAndDeductCredits() {
        double recruitmentCost = 200.0;
        crewService.recruitMember(testShip, testMember, recruitmentCost);

        assertEquals(800.0, testShip.getCredits(), "Nie pobrano opłaty rekrutacyjnej!");
        assertEquals(1, testShip.getCrew().size(), "Załogant nie został dodany do listy!");
    }

    @Test
    void shouldThrowExceptionWhenCrewIsFull() {
        crewService.recruitMember(testShip, new CrewMember("Anna", "Inżynier", 100, 50, 2, 1, 5, 2), 0);
        crewService.recruitMember(testShip, new CrewMember("Max", "Strzelec", 100, 50, 3, 5, 1, 1), 0);

        assertThrows(CrewFullException.class, () -> {
            crewService.recruitMember(testShip, testMember, 0);
        });
    }

    @Test
    void shouldHealCrewMemberAndDeductCredits() {
        testMember.setHp(30);
        double healCost = 100.0;

        crewService.healCrewMember(testShip, testMember, healCost);

        assertEquals(100, testMember.getHp(), "Załogant nie został wyleczony do maksymalnego HP!");
        assertEquals(900.0, testShip.getCredits(), "Nie pobrano opłaty za leczenie!");
    }

    @Test
    void shouldPaySalariesCorrectly() {
        crewService.recruitMember(testShip, new CrewMember("Anna", "Inżynier", 100, 40, 2, 1, 5, 2), 0);
        crewService.recruitMember(testShip, new CrewMember("Max", "Strzelec", 100, 60, 3, 5, 1, 1), 0);

        crewService.paySalaries(testShip);

        assertEquals(900.0, testShip.getCredits(), "Żołd nie został poprawnie potrącony!");
    }
}