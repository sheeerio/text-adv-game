package model;

import model.items.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Inventory {
    private Item currentItem;
    Set<Item> items;
    ArrayList<String> itemNames;
    ArrayList<Integer> numberOfItems;

    public Inventory() {
        currentItem = null;
        items = new HashSet<>();
        itemNames = new ArrayList<>();
        numberOfItems = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
        String tempName = item.getName();
        if (itemNames.contains(tempName)) {
            int idx = itemNames.indexOf(tempName);
            numberOfItems.set(idx, numberOfItems.get(idx) + 1);
        } else {
            items.add(item);
            itemNames.add(tempName);
            numberOfItems.add(1);
        }

    }

    // REQUIRES: item must be in the list
    public void removeItemBunch(Item item) {
        int idx = itemNames.indexOf(item.getName());
        items.remove(item);
        itemNames.remove(idx);
        numberOfItems.remove(idx);
    }

    // REQUIRES: item must be in the list
    public void removeNItems(Item item, Integer number) {
        int idx = itemNames.indexOf(item.getName());
        if (number >= numberOfItems.get(idx)) {
            removeItemBunch(item);
        } else {
            numberOfItems.set(idx, numberOfItems.get(idx) - number);
        }
    }
}
