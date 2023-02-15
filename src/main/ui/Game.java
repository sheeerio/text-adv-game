package ui;

import model.Inventory;
import model.Mob;
import model.Player;
import model.items.*;

import java.util.*;

import static model.Player.getAttack;


// Game represents information about the UI of game, including printing and scanning.
public class Game {
    // Initialize day
    private static int timeState = 1; // 1 : day; 2 : night

    // Initialize neutral mobs
    static Mob spider = new Mob("Spider", 16, 2, .4);
    static Mob wolf = new Mob("Wolf", 8, 4, 0.3);
    static Mob enderman = new Mob("Enderman", 40, 7, 0.0);
    static Mob ironGolem = new Mob("Iron Golem", 100, 21, 0.25);

    // Initialize hostile mobs
    static Mob blaze = new Mob("Blaze", 20, 6, 0.1);
    static Mob chickenJockey = new Mob("Chicken Jockey", 24, 3, .8);
    static Mob creeper = new Mob("Creeper", 20, 43, .4);
    static Mob skeleton = new Mob("Skeleton", 20, 4, 0.3);
    static Mob zombie = new Mob("Zombie", 20, 3, 0.23);

    // Initialize blocks
    static Block wood = new Block("Wood", null);
    static Food rawMeat = new Food("Raw Meat", 5);
    static Item woodenPlank = new Block("Wooden Planks", null);
    static Item sticks = new Misc("Sticks");
    static Item woodenSword = new Weapon("Wooden Sword", "sword");

    // Initialize inventory
    static Inventory inventory;

    // Intialize scanner
    static Scanner in = new Scanner(System.in);

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings", "checkstyle:LineLength"})
    public static void printInventory() {
        boolean running = true;

        while (running) {
            for (String i : inventory.getItemNames()) {
                System.out.println(inventory.getItemNames().indexOf(i) + 1 + ".  " + i + " ("
                        + inventory.getNumberOfItems().get(inventory.getItemNames().indexOf(i)) + ")");
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
                inventory.setCurrentItemTo(Integer.valueOf(numToChange) - 1);
                System.out.println("Press R to throw away " + inventory.getCurrentItem());
                System.out.println("Press E to close your inventory.");
                getRecipeOfCurrentItem();
                String closeInv = in.nextLine();

                if (closeInv.equals("E")) {
                    System.out.println("" + " ");
                    break;
                } else if (closeInv.equals("R")) {
                    inventory.removeItemBunch(inventory.getItems().get(inventory.getItemNames().indexOf(inventory.getCurrentItem())));
                    printInventory();
                    printCurrentItem();
                } else {
                    continue;
                }
            } else {
                inventory.setCurrentItemTo(Integer.valueOf(numToChange) - 1);
                printInventory();
                System.out.println("\n\n");
                printCurrentItem();
            }
        }
    }

    public static void printCurrentItem() {
        System.out.println("Your current item is " + inventory.getCurrentItem());
        getRecipeOfCurrentItem();
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings", "checkstyle:LineLength"})
    public static void getRecipeOfCurrentItem() {
        if (inventory.getCurrentItem() == "Wood") {
            System.out.println("Press B to create Wooden Planks (4)");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 4; i++) {
                    inventory.addItem(woodenPlank);
                    inventory.setCurrentItemTo(inventory.getItems().indexOf(woodenPlank));
                    printInventory();
                }
            }
        } else if (inventory.getCurrentItem() == "Wooden Planks" && inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Wooden Planks")) >= 2) {
            System.out.println("Press B to create Sticks (2)");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 2; i++) {
                    inventory.addItem(sticks);
                }
                inventory.removeNItems(woodenPlank, 2);
                inventory.setCurrentItemTo(inventory.getItems().indexOf(sticks));
                printInventory();
            }
        } else if (Objects.equals(inventory.getCurrentItem(), "Sticks")
                && inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Wooden Planks")) >= 2
                && inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Sticks")) > 0) {
            System.out.println("Press B to create Wooden Sword");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 1; i++) {
                    inventory.addItem(woodenSword);
                    inventory.removeNItems(woodenPlank, 2);
                    inventory.removeNItems(sticks, 1);
                    inventory.setCurrentItemTo(inventory.getItems().indexOf(woodenSword));
                    printInventory();
                }
            }
        }
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings", "checkstyle:LineLength"})
    public static void main(String[] args) {
        String difficulty = "normal";

        ArrayList<Mob> hostileMobList = new ArrayList<>(Arrays.asList(spider, enderman, blaze, chickenJockey, creeper, skeleton, zombie));

        inventory =  new Inventory();

        Scanner in = new Scanner(System.in);
        Random rand = new Random();

        // Game variables
        String[] animalMobs = {"wolf", "cow", "pig", "sheep"};

        System.out.println("Choose Route:\n\n1. Pacifist\n2. Survival");
        String route = in.nextLine();

        if (route.equals("1") || route.equals("Pacifist") || route.equals("pacifist")) {
            difficulty = "hard";
        }
        System.out.println("Enter your username: ");
        String username = in.nextLine();

        System.out.println("Welcome to the world, " + username + "\n\n\n\n\n");

        Weapon weapon = new Weapon("hands", "hands");
        boolean running = true;

        Timer timer = new Timer();
        long startTime = System.currentTimeMillis();
        WOW:
        while (running) {

            System.out.println("Press 1 to " + weapon.getInfVerb() + " wood.");
            String chop = in.nextLine();
            int sum = 0;
            boolean oneY = false;
            if (chop.equals("1")) {
                oneY = true;
            } else {
                System.out.println("You pressed the wrong button!");
                break WOW;
            }
            while (oneY && !chop.equals("E")) {
                Random lund = new Random();
                int num = lund.nextInt(4) + 1;
                for (int i = 0; i < num; i++) {
                    inventory.addItem(wood);
                }

                makeWait();
                System.out.println("You " + weapon.getVerb() + " " + num + " wooden logs!");
                System.out.println("You now have " + inventory.count(wood) + " wooden logs!\n");
                System.out.println("Press E to view your inventory.");
                System.out.println("Press Enter to quit chopping wood.");
                System.out.println("Press 1 to chop wood.");
                chop = in.nextLine();
                if (chop.equals("E")) {
                    printInventory();
                } else if (chop.isEmpty()) {
                    continue;
                } else {
                    sum += 1;
                }
                if (sum % 5 == 0) {
                    System.out.println("Are you done? (y/n)");
                    String chopEnd = in.nextLine();
                    if (chopEnd.equals("y")) {
                        break;
                    } else {
                        chop = "1";
                    }
                }
            }


            String animal = animalMobs[rand.nextInt(animalMobs.length)];
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                //Don't worry about it.
            }
            System.out.println("\n\n\n\n\n\n\n\nA " + animal + " " + animalSound(animal) + " near you...\n\n\n\n");

            System.out.println("Do you want to kill it for meat? (y/n)");
            String killAnimal = in.nextLine();
            if (killAnimal.equals("y")) {
                makeWait();
                if (difficulty.equals("hard")) {
                    System.out.println("The animal died and you got nothing...");
                } else {
                    int drop = rand.nextInt(3) + 1;
                    for (int i = 0; i < drop; i++) {
                        inventory.addItem(rawMeat);
                    }
                    System.out.println("You got " + drop + " Raw Meat.");
                }
            }


            makeWait();
            long endTime = System.currentTimeMillis();
            setTimeState(endTime);
            if (timeState % 2 == 0) {
                if (inventory.getItemNames().contains("Wooden Sword") && inventory.getCurrentItem() != "Wooden Sword") {
                    String out = "You should equip your sword.";
                    System.out.println("\nDusk is approaching..." + out);
                    System.out.println("Press E to open your inventory.\n");
                    String inv = in.nextLine();

                    if (inv.equals("E") || inv.equals("e")) {
                        printInventory();
                    }
                } else if (!inventory.getItemNames().contains("Wooden Sword")) {
                    String out = "You should craft a wooden sword.";
                    System.out.println("\nDusk is approaching..." + out);
                    System.out.println("Press E to open your inventory.\n");
                    String inv = in.nextLine();

                    if (inv.equals("E") || inv.equals("e")) {
                        printInventory();
                    }
                } else {
                    System.out.println("Dusk is approaching... good luck.");
                }
            }
            if (difficulty.equals("hard")) {
                System.out.println("Quit the game!");
                System.exit(0);
            }

            makeWait();
            boolean enemyfight = true;
            GAME:
            while (enemyfight) {
                Mob enemy = hostileMobList.get(rand.nextInt(hostileMobList.size()));
                int enemyHealth = enemy.getHp();
                String enemyName = enemy.getName();
                System.out.println("\n--------------------------");
                System.out.println("\t# " + enemyName + " has appeared! #\n");

                while (enemyHealth > 0) {
                    System.out.println("\tYour HP: " + Player.getHealth());
                    System.out.println("\t" + enemyName + "'s HP: " + enemyHealth);
                    System.out.println("\n\t What would you like to do?");
                    System.out.println("\t1. Attack");
                    if (inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Raw Meat")) > 0) {
                        System.out.println("\t2. Eat raw meat ("
                                + inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Raw Meat")) + ")");
                    } else {
                        System.out.println("\t2. Eat raw meat (0)");
                    }
                    System.out.println("\t3. Run!");

                    String input = in.nextLine();
                    if (input.equals("1")) {
                        int damageDealt = rand.nextInt(getAttack()) + 10;
                        int damageTaken = enemy.getBaseAttack();

                        enemyHealth -= damageDealt;
                        Player.setHealth(Player.getHealth() - damageTaken);

                        System.out.println("\t> You strike the " + enemyName + " for " + damageDealt + " damage.");
                        System.out.println("\t> You receive " + damageTaken + " in retaliation");

                        if (Player.getHealth() < 1) {
                            System.out.println("\t> You have taken too much damage, you are too weak to go on!");
                        }
                    } else if (input.equals("2")) {
                        if (inventory.count(rawMeat) > 0) {
                            Player.setHealth(Player.getHealth() + 10);
                            inventory.removeNItems(rawMeat, 1);
                            if (inventory.getItemNames().contains("Raw Meat")) {
                                System.out.println("\t> You eat raw meat, healing yourself for 10 HP."
                                        + "\n\t> You now have " + Player.getHealth() + " HP."
                                        + "\n\t> You have "
                                        + inventory.count(rawMeat)
                                        + " raw meat.");
                            } else {
                                System.out.println("You don't have enough meat... get fucked!");
                            }
                        } else {
                            System.out.println("\t> You have no health items left! Defeat enemies for a chance to get one!");
                        }
                    } else if (input.equals("3")) {
                        System.out.println("\t> You run away from the " + enemyName + "!");
                        continue GAME;
                    } else {
                        System.out.println("\tInvalid command!");
                    }
                }

                if (Player.getHealth() < 1) {
                    System.out.println(Player.getUsername() + " limps out of the cave, weak from battle.");
                    break;
                }

                System.out.println("-----------------------");
                System.out.println(" # " + enemyName + " was defeated # ");
                System.out.println(" # " + Player.getUsername() + " has " + Player.getHealth() + " HP left");
                int drop = rand.nextInt(4) + 1;
                if (rand.nextInt(10) < 7) {
                    for (int i = 0; i < drop; i++) {
                        inventory.addItem(rawMeat);
                    }
                    System.out.println(" # The " + enemyName + " dropped " + drop + " raw meat.");
                    System.out.println(" # You now have "
                            + inventory.getNumberOfItems().get(inventory.getItemNames().indexOf("Raw Meat"))
                            + " raw meat");
                }
                System.out.println("---------------");
                System.out.println("What would you like to do now?");
                System.out.println("1. Continue fighting");
                System.out.println("2. Exit cave");

                String input = in.nextLine();

                while (!input.equals("1") && !input.equals("2")) {

                    System.out.println("Invalid command");
                    input = in.nextLine();
                }

                if (input.equals("1")) {
                    System.out.println("You continue on...");
                } else if (input.equals("2")) {
                    System.out.println("You exit the cave...");
                    break;
                }
            }

            System.out.println("########################");
            System.out.println("#  THANKS FOR PLAYING! #");
            System.out.println("########################");
            System.exit(0);


        }


    }

    public static String animalSound(String animal) {
        if (animal == "wolf") {
            return "barks";
        } else if (animal == "pig") {
            return "oinks";
        } else if (animal == "cow") {
            return "moos";
        } else {
            return "baahs";
        }
    }

    public static void makeWait() {
        System.out.print(".");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
            //Don't worry about it.
        }
        System.out.print(".");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
            //Don't worry about it.
        }
        System.out.print(".");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            //Don't worry about it.
        }
    }

    public static void setTimeState(long time) {
        if (time > 100000) {
            timeState++;
        }
    }
}
