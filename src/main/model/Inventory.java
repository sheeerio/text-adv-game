package model;

import model.items.Item;
import model.items.*;

import java.util.*;


public class Inventory {
    private String currentItem;
    LinkedList<Item> items;
    ArrayList<String> itemNames;
    ArrayList<Integer> numberOfItems;

    // Initialization of all blocks

    // EFFECTS: creates an empty inventory
    public Inventory() {
        currentItem = null;
        items = new LinkedList<>();
        itemNames = new ArrayList<>();
        numberOfItems = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds the item to the inventory item and name list
    // and appends 1 to the number list if item not already in inv.
    // otherwise adds 1 to the number of the item.
    // Sets current item to be last item added
    public void addItem(Item item) {
        String tempName = item.getName();
        if (items.contains(item)) {
            int idx = itemNames.indexOf(tempName);
            numberOfItems.set(idx, numberOfItems.get(idx) + 1);
        } else {
            items.add(item);
            itemNames.add(tempName);
            numberOfItems.add(1);
        }
        setCurrentItemDefault();
    }

    // REQUIRES: item must be in the list
    // MODIFIES: this
    // EFFECTS: removes item from all inventory lists
    // and sets current item to last item in itemNames list
    public void removeItemBunch(Item item) {

        int idx = items.indexOf(item);
        items.remove(idx);
        itemNames.remove(idx);
        numberOfItems.remove(idx);
        setCurrentItemDefault();
    }

    // REQUIRES: item must be in the list
    // MODIFIES: this
    // EFFECTS: if number < item number, subtracts number
    // from item number in item number list
    // else removes item bunch from lists
    public void removeNItems(Item item, Integer number) {
        int idx = itemNames.indexOf(item.getName());
        boolean temp = false;
        if (number >= numberOfItems.get(idx)) {
            removeItemBunch(item);
            temp = true;
        } else {
            numberOfItems.set(idx, numberOfItems.get(idx) - number);
        }
        if (temp) {
            setCurrentItemDefault();
        }
    }

    // EFFECTS: returns the number of that item if it exists
    // else returns 0
    public Integer count(Item item) {
        if (items.contains(item)) {
            int i = new ArrayList<>(items).indexOf(item);
            return numberOfItems.get(i);
        } else {
            return 0;
        }
    }

    // REQUIRES: index must be in range of itemNames.size()
    // MODIFES: this
    // EFFECTS: sets current item to item at index in list
    public void setCurrentItemTo(int idx) {
        currentItem = itemNames.get(idx);
    }

    // MODIFIES: this
    // EFFECTS: sets current item to item at last index in list
    public void setCurrentItemDefault() {
        if (!items.isEmpty()) {
            currentItem = itemNames.get(itemNames.size() - 1);
        } else {
            currentItem = null;
        }
    }


    public ArrayList<String> getItemNames() {
        return itemNames;
    }

    public String getCurrentItem() {
        return currentItem;
    }

    public ArrayList<Integer> getNumberOfItems() {
        return numberOfItems;
    }

    public LinkedList<Item> getItems() {
        return items;
    }

}
