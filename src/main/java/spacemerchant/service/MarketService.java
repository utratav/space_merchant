package spacemerchant.service;

import spacemerchant.exception.NotEnoughCreditsException;
import spacemerchant.exception.NotEnoughSpaceException;
import spacemerchant.model.CrewMember;
import spacemerchant.model.EconomyType;
import spacemerchant.model.Item;
import spacemerchant.model.Planet;
import spacemerchant.model.Ship;

public class MarketService {

    // Obliczanie aktualnej ceny kupna dla gracza
    public double calculateBuyPrice(Ship ship, Item item, Planet planet) {
        double base = item.getBasePrice();
        double economyModifier = getEconomyModifier(planet.getEconomy());

        double rawPrice = base * economyModifier;

        // Zniżka z tytułu umiejętności handlowych załogi
        int tradeSkill = getHighestTradeSkill(ship);
        double discount = 1.0 - (tradeSkill * 0.02);

        return rawPrice * discount;
    }

    // Obliczanie aktualnej ceny sprzedaży dla gracza
    public double calculateSellPrice(Ship ship, Item item, Planet planet) {
        double base = item.getBasePrice();
        double economyModifier = getEconomyModifier(planet.getEconomy());


        double rawPrice = (base * economyModifier) * 0.8;

        // Bonus z tytułu umiejętności handlowych
        int tradeSkill = getHighestTradeSkill(ship);
        double bonus = 1.0 + (tradeSkill * 0.02);

        return rawPrice * bonus;
    }

    public void buyItem(Ship ship, Item item, int amount, Planet planet) {

        // Zabezpieczenie przed akcjami w trakcie lotu kosmicznego
        if (ship.getCurrentLocation() == null) {
            throw new IllegalStateException("Odmowa dostępu. Statek znajduje się w przestrzeni kosmicznej!");
        }

        if (amount <= 0) return;

        double unitPrice = calculateBuyPrice(ship, item, planet);
        double totalCost = unitPrice * amount;
        double totalWeight = item.getWeight() * amount;

        if (ship.getCredits() < totalCost) {
            throw new NotEnoughCreditsException("Niewystarczająca ilość kredytów. Potrzeba: " + totalCost);
        }

        if (!ship.hasAvailableCargoSpace(totalWeight)) {
            throw new NotEnoughSpaceException("Brak miejsca w ładowni na ten towar.");
        }

        // Finalizacja transakcji: pobranie opłaty i dodanie towaru
        ship.setCredits(ship.getCredits() - totalCost);
        ship.getCargo().addItem(item, amount);
    }

    public void sellItem(Ship ship, Item item, int amount, Planet planet) {

        // Zabezpieczenie przed akcjami w trakcie lotu kosmicznego
        if (ship.getCurrentLocation() == null) {
            throw new IllegalStateException("Odmowa dostępu. Statek znajduje się w przestrzeni kosmicznej!");
        }

        if (amount <= 0) return;

        // Weryfikacja czy gracz fizycznie posiada towar w ładowni
        Integer currentAmount = ship.getCargo().getItems().get(item);
        if (currentAmount == null || currentAmount < amount) {
            throw new IllegalArgumentException("Nie posiadasz wystarczającej ilości tego towaru do sprzedaży.");
        }

        double unitPrice = calculateSellPrice(ship, item, planet);
        double totalRevenue = unitPrice * amount;

        // Finalizacja transakcji: usunięcie towaru i przelew środków
        ship.getCargo().removeItem(item, amount);
        ship.setCredits(ship.getCredits() + totalRevenue);
    }

    // Modyfikator cen w zależności od typu planety
    private double getEconomyModifier(EconomyType economy) {
        return switch (economy) {
            case AGRICULTURAL -> 0.8; // Na rolniczych planetach jest taniej
            case INDUSTRIAL -> 1.1;   // Przemysłowe mają wyższe ceny
            case HIGH_TECH -> 1.3;    // Technologie windują ceny w górę
            case MINING -> 0.9;       // Górnicze kolonie są stosunkowo tanie
            default -> 1.0;
        };
    }

    // Szukanie członka załogi z najwyższym atrybutem handlu
    private int getHighestTradeSkill(Ship ship) {
        if (ship.getCrew() == null || ship.getCrew().isEmpty()) {
            return 0;
        }

        int maxTrade = 0;
        for (CrewMember member : ship.getCrew()) {
            // Zakładam, że pole umiejętności handlu w CrewMember nazywa się 'trade' i ma getter
            if (member.getTrade() > maxTrade) {
                maxTrade = member.getTrade();
            }
        }
        return maxTrade;
    }
}