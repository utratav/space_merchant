package spacemerchant.controller;

import spacemerchant.model.EconomyType;
import spacemerchant.model.Item;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import java.util.Scanner;

public class GameEngine {
    private Ship playerShip;
    private Location startingLocation;
    private Item testItem;

    public GameEngine() {
        this.startingLocation = new Location("Ziemia", 0, 0, EconomyType.INDUSTRIAL,true);
        this.testItem = new Item("WOD", "Woda", 10.0, 1.0);

        this.playerShip = new Ship("Prometeusz", 1000.0, 100.0, 50.0, startingLocation, 100, 4, 1);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Witaj w Space Merchant!");
        System.out.println("Statek: " + playerShip.getName());
        System.out.println("Lokalizacja: " + playerShip.getCurrentLocation().getName());

        while (true) {
            System.out.println("\n--- STATUS STATKU ---");
            System.out.println("Kredyty: " + playerShip.getCredits());
            System.out.println("Ładownia (waga): " + playerShip.getCargo().getTotalWeight() + "/" + playerShip.getMaxCargoWeight());

            System.out.println("\n[ Wciśnij Enter, aby kontynuować... ]");
            scanner.nextLine();

            System.out.println("Pętla wykonała kolejny obrót!");
        }
    }
}