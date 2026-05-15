package spacemerchant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spacemerchant.exception.NotEnoughCreditsException;
import spacemerchant.exception.NotEnoughSpaceException;
import spacemerchant.model.EconomyType;
import spacemerchant.model.Item;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;

import static org.junit.jupiter.api.Assertions.*;

class MarketServiceTest {

    private MarketService marketService;
    private Ship testShip;
    private Location testLocation;
    private Item testItem;

    @BeforeEach
    void setUp() {
        marketService = new MarketService();
        testLocation = new Location("Ziemia", 0, 0, EconomyType.INDUSTRIAL,true);
        testItem = new Item("WOD", "Woda", 100.0, 10.0);
        testShip = new Ship("Prometeusz", 1000.0, 100.0, 50.0, testLocation, 100, 4, 1.0);
    }

    @Test
    void shouldSuccessfullyBuyItemAndDeductCredits() {
        marketService.buyItem(testShip, testItem, 2, testLocation);

        assertEquals(780.0, testShip.getCredits(), "Kredyty nie zostały poprawnie odjęte!");
        assertEquals(20.0, testShip.getCargo().getTotalWeight(), "Waga ładowni się nie zgadza!");
    }

    @Test
    void shouldThrowExceptionWhenNotEnoughCredits() {
        testShip.setMaxCargoWeight(500.0);

        assertThrows(NotEnoughCreditsException.class, () -> {
            marketService.buyItem(testShip, testItem, 10, testLocation);
        });

        // Upewnienie się że mimo błedu kredyty nie zniknęły
        assertEquals(1000.0, testShip.getCredits());
    }

    @Test
    void shouldThrowExceptionWhenNotEnoughSpace() {
        assertThrows(NotEnoughSpaceException.class, () -> {
            marketService.buyItem(testShip, testItem, 6, testLocation);
        });
    }
}