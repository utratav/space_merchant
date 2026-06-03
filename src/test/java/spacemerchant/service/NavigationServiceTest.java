package spacemerchant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spacemerchant.exception.NotEnoughFuelException;
import spacemerchant.exception.ShipDestroyedException;
import spacemerchant.model.CrewMember;
import spacemerchant.model.EconomyType;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;

import static org.junit.jupiter.api.Assertions.*;

class NavigationServiceTest {

    private NavigationService navigationService;
    private CrewService crewService;
    private Ship testShip;
    private Location locA;
    private Location locB;
    private Location locC;

    @BeforeEach
    void setUp() {
        crewService = new CrewService();
        navigationService = new NavigationService(crewService);

        locA = new Location("Ziemia", 0, 0, EconomyType.INDUSTRIAL, true);
        locB = new Location("Mars", 10, 10, EconomyType.MINING, true);
        locC = new Location("Baza Piratów", 50, 50, EconomyType.AGRICULTURAL, false);

        locA.addPath(locB, 15.0);

        testShip = new Ship("Prometeusz", 1000.0, 100.0, 50.0, locA, 100, 2, 1.0);

        crewService.recruitMember(testShip, new CrewMember("Jan", "Pilot", 100, 50, 5, 2, 1, 3), 0);
    }

    @Test
    void shouldSuccessfullyTravelToConnectedLocation() {
        navigationService.travel(testShip, locB);

        assertEquals(locB, testShip.getCurrentLocation(), "Statek nie zmienił swojej lokacji!");
        assertEquals(85.0, testShip.getCurrentFuel(), "Paliwo nie zostało odjęte zgodnie z kosztem trasy!");

        assertEquals(950.0, testShip.getCredits(), "Żołd za przelot (turę) nie został wypłacony!");
    }

    @Test
    void shouldThrowExceptionWhenPathDoesNotExist() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            navigationService.travel(testShip, locC);
        });

        assertTrue(exception.getMessage().contains("Brak szlaku"), "Zły komunikat błędu!");
        assertEquals(locA, testShip.getCurrentLocation(), "Statek nie powinien się ruszyć!");
    }

    @Test
    void shouldThrowExceptionWhenNotEnoughFuel() {
        testShip.setCurrentFuel(10.0);

        assertThrows(NotEnoughFuelException.class, () -> {
            navigationService.travel(testShip, locB);
        });

        assertEquals(locA, testShip.getCurrentLocation(), "Statek nie powinien się ruszyć w przypadku braku paliwa!");
    }

    @Test
    void shouldRequireLivingPilotBeforeTravel() {
        Ship shipWithoutPilot = new Ship("Prometeusz", 1000.0, 100.0, 50.0, locA, 100, 2, 1.0);

        assertThrows(IllegalStateException.class, () -> {
            navigationService.travel(shipWithoutPilot, locB);
        });

        assertEquals(locA, shipWithoutPilot.getCurrentLocation());
    }

    @Test
    void shouldDefeatShipWithoutPilotOutsideStation() {
        Ship shipWithoutPilot = new Ship("Prometeusz", 1000.0, 100.0, 50.0, locC, 100, 2, 1.0);
        locC.addPath(locB, 5.0);

        assertThrows(ShipDestroyedException.class, () -> {
            navigationService.travel(shipWithoutPilot, locB);
        });

        assertTrue(shipWithoutPilot.isDefeated());
    }
}