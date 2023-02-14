package model;

import model.items.Item;
import model.items.*;

import java.util.*;


public class Inventory {
    private String currentItem;
    LinkedList<Item> items;
    ArrayList<String> itemNames;
    ArrayList<Integer> numberOfItems;
    Scanner in = new Scanner(System.in);

    // Initialization of all blocks
    Item woodenPlank = new Block("Wooden Planks", null);
    Item sticks = new Misc("Sticks");
    Item woodenSword = new Weapon("Wooden Sword", "sword");

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

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void printInventory() {
        boolean running = true;

        while (running) {
            for (String i : itemNames) {
                System.out.println(itemNames.indexOf(i) + 1 + ".  " + i + " ("
                        + numberOfItems.get(itemNames.indexOf(i)) + ")");
            }
            printCurrentItem();
            System.out.println("Press the item number to change current item.");
            System.out.println("Press E to close the inventory");
            Scanner in = new Scanner(System.in);
            String numToChange = in.nextLine();
            if (numToChange.equals("E")) {
                break;
            } else if (numToChange.equals("1") || numToChange.equals("2") || numToChange.equals("3")
                    || numToChange.equals("4")) {
                setCurrentItemTo(Integer.valueOf(numToChange) - 1);
                System.out.println("Press R to throw away " + currentItem);
                System.out.println("Press E to close your inventory.");
                getRecipeOfCurrentItem();
                String closeInv = in.nextLine();

                if (closeInv.equals("E")) {
                    System.out.println("" + " ");
                    break;
                } else if (closeInv.equals("R")) {
                    removeItemBunch(items.get(itemNames.indexOf(currentItem)));
                    printInventory();
                    printCurrentItem();
                } else {
                    continue;
                }
            } else {
                setCurrentItemTo(Integer.valueOf(numToChange) - 1);
                printInventory();
                System.out.println("\n\n");
                printCurrentItem();
            }
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

    public void printCurrentItem() {
        System.out.println("Your current item is " + currentItem);
        getRecipeOfCurrentItem();
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void getRecipeOfCurrentItem() {
        if (currentItem == "Wood") {
            System.out.println("Press B to create Wooden Planks (4)");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 4; i++) {
                    addItem(woodenPlank);
                    setCurrentItemTo(items.indexOf(woodenPlank));
                    printInventory();
                }
            }
        } else if (currentItem == "Wooden Planks" && numberOfItems.get(itemNames.indexOf("Wooden Planks")) >= 2) {
            System.out.println("Press B to create Sticks (2)");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 2; i++) {
                    addItem(sticks);
                }
                removeNItems(woodenPlank, 2);
                setCurrentItemTo(items.indexOf(sticks));
                printInventory();
            }
        } else if (Objects.equals(currentItem, "Sticks")
                && numberOfItems.get(itemNames.indexOf("Wooden Planks")) >= 2
                && numberOfItems.get(itemNames.indexOf("Sticks")) > 0) {
            System.out.println("Press B to create Wooden Sword");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 1; i++) {
                    this.addItem(woodenSword);
                    removeNItems(woodenPlank, 2);
                    removeNItems(sticks, 1);
                    setCurrentItemTo(items.indexOf(woodenSword));
                    printInventory();
                }
            }
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
