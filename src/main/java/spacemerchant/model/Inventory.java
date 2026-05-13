package spacemerchant.model;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<Item, Integer> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    public void addItem(Item item, int amount) {
        if (amount > 0) {
            items.put(item, items.getOrDefault(item, 0) + amount);
        }
    }

    public void removeItem(Item item, int amount) {
        if (amount > 0 && items.containsKey(item)) {
            int currentAmount = items.get(item);
            int newAmount = currentAmount - amount;

            if (newAmount <= 0) {
                items.remove(item);
            } else {
                items.put(item, newAmount);
            }
        }
    }

    public double getTotalWeight() {
        double totalWeight = 0.0;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int amount = entry.getValue();
            totalWeight += item.getWeight() * amount;
        }
        return totalWeight;
    }
    
    public Map<Item, Integer> getItems() {
        return items;
    }
}