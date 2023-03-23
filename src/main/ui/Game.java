package ui;

import model.Inventory;
import model.Mob;
import model.Player;
import model.items.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.*;


// Game represents information about the UI of game, including printing and scanning.
public class Game {

    JFrame window;
    Container con;
    JPanel titleNamePanel, startButtonPanel, mainTextPanel, choiceButtonPanel, playerPanel;
    JPanel startFieldPanel, inventoryTextPanel;
    JLabel titleNameLabel, hpLabel, hpLabelNumber, weaponLabel, weaponLabelName, startFieldLabel;
    JLabel usernameTextLabel;
    Font titleFont = new Font("Times New Roman", Font.BOLD, 94);
    Font normalFont = new Font("Times New Roman", Font.PLAIN, 18);
    JButton startButton, choice1, choice2, choice3, choice4;
    JTextArea mainTextArea, inventoryTextArea;
    JTextField usernameTextField;
    DefaultCaret caret;
    JScrollPane scroll;

    TitleScreenHandler tsHandler = new TitleScreenHandler();
    ActionListener chopListener;

    // Initialize JSON classes
    private static final String JSON_STORE = "./data/game.json";
    private static JsonReader jsonReader = new JsonReader(JSON_STORE);
    private static JsonWriter jsonWriter = new JsonWriter(JSON_STORE);

    // Initialize day
    private static int timeState = 1; // 1 : day; 2 : night
    int woodChopped = 0;
    boolean whichOne = false;

    // Initialize neutral mobs
    static Mob spider = new Mob("Spider", 16, 2, .4);
    static Mob enderman = new Mob("Enderman", 40, 7, 0.0);

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

    // Initialize player
    static Player player;

    // Intialize scanner
    static Scanner in = new Scanner(System.in);

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings", "checkstyle:LineLength"})
    // EFFECT: prints the current inventory
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
            System.out.println("Press S to save the inventory");
            System.out.println("Press L to read the inventory");
            Scanner in = new Scanner(System.in);
            String numToChange = in.nextLine();
            if (numToChange.equals("E")) {
                break;
            } else if (numToChange.equals("L")) {
                try {
                    inventory = jsonReader.read();
                    System.out.println("Loaded Game from " + JSON_STORE);
                } catch (IOException e) {
                    System.out.println("Unable to read from file: " + JSON_STORE);
                }
            } else if (numToChange.equals("S")) {
                try {
                    jsonWriter.open();
                    jsonWriter.writeInventory(inventory, player);
                    jsonWriter.close();
                    System.out.println("Saved Game to " + JSON_STORE);
                } catch (FileNotFoundException e) {
                    System.out.println("Unable to read from file: " + JSON_STORE);
                }
            } else if (numToChange.equals("1") || numToChange.equals("2") || numToChange.equals("3")
                    || numToChange.equals("4")) {
                inventory.setCurrentItemTo(Integer.valueOf(numToChange) - 1);
                System.out.println("Press R to throw away " + inventory.getCurrentItem());
                System.out.println("Press E to close your inventory.");
                getRecipeOfCurrentItem();
                String closeInv = in.nextLine();

                if (numToChange.equals("E")) {
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

    // EFFECT: prints the current item
    public static void printCurrentItem() {
        System.out.println("Your current item is " + inventory.getCurrentItem());
        getRecipeOfCurrentItem();
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings", "checkstyle:LineLength"})
    // EFFECT: prints the recipe of curent item
    public static void getRecipeOfCurrentItem() {
        if (inventory.getCurrentItem() == "Wood") {
            System.out.println("Press B to create Wooden Planks (4)");
            String next = in.nextLine();
            if (next.equals("B")) {
                for (int i = 0; i < 4; i++) {
                    inventory.addItem(woodenPlank);
                }
                inventory.removeNItems(wood, 1);
                inventory.setCurrentItemTo(inventory.getItems().indexOf(woodenPlank));
                printInventory();
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
    // EFFECT: main
    public static void main(String[] args) {
        new Game();
        String difficulty = "normal";

        ArrayList<Mob> hostileMobList = new ArrayList<>(Arrays.asList(spider, enderman, blaze, chickenJockey, creeper, skeleton, zombie));


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
        player = new Player(username, 20);

        System.out.println("Press 1 to load previous game data");
        String baba = in.nextLine();

        if (baba.equals("1")) {
            try {
                inventory = jsonReader.read();
                System.out.println("Loaded inventory from " + JSON_STORE);
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        }

        Weapon weapon = new Weapon("hands", "hands");
        boolean running = true;

        Timer timer = new Timer();
        long startTime = System.currentTimeMillis();
        WOW:
        while (running) {

            System.out.println("Press 1 to " + weapon.getInfVerb() + " tree.");
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
                System.out.println("Press 1 to " + weapon.getInfVerb() + " tree");

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
                        int damageDealt = rand.nextInt(20) + 10;
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
                                break;
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
                System.out.println("3. Save Game");
                System.out.println("4. Load Game (Current game data will be lost)");
                System.out.println("5. Go outside to chop wood");

                String input = in.nextLine();

                if (input.equals("3")) {
                    try {
                        jsonWriter.open();
                        jsonWriter.writeInventory(inventory, player);
                        jsonWriter.close();
                        System.out.println("Saved Game to " + JSON_STORE);
                    } catch (FileNotFoundException e) {
                        System.out.println("Unable to read from file: " + JSON_STORE);
                    }
                }
                if (input.equals("5")) {
                    continue WOW;
                }

                if (input.equals("4")) {
                    try {
                        inventory = jsonReader.read();
                        System.out.println("Loaded Game from " + JSON_STORE);
                    } catch (IOException e) {
                        System.out.println("Unable to read from file: " + JSON_STORE);
                    }
                }

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

    // EFFECT: returns the first-person subject agreement of verb for animal
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

    // EFFECT: delays execution of next line after makeWait() by 2 seconds
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

    // MODIFIES: this
    // EFFECT: changes state of time
    public static void setTimeState(long time) {
        if (time > 100000) {
            timeState++;
        }
    }

    public Game() {
        window = new JFrame();
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.black);
        window.setLayout(null);
        con = window.getContentPane();

        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(100, 100, 600, 150);
        titleNamePanel.setBackground(Color.black);
        titleNameLabel = new JLabel("12 SECONDS");
        titleNameLabel.setText("12 SECONDS");
        titleNameLabel.setForeground(Color.white);
        titleNameLabel.setFont(titleFont);

        startFieldPanel = new JPanel();
        startFieldPanel.setBounds(200, 240, 400, 40);
        startFieldPanel.setBackground(Color.black);
        startFieldPanel.setForeground(Color.white);

        usernameTextLabel = new JLabel();
        usernameTextField = new JTextField("Enter Username, e.g. PussyDestroyer69", 40);
        startFieldPanel.setLayout(new GridLayout(1, 2));
        startFieldPanel.add(usernameTextField);

        startButtonPanel = new JPanel();
        startButtonPanel.setBounds(300, 350, 200, 100);
        startButtonPanel.setBackground(Color.black);

        startButton = new JButton("START");
        startButtonPanel.setBackground(Color.black);
        startButtonPanel.setForeground(Color.white);

        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);
        startButton.setFont(normalFont);
        startButton.addActionListener(tsHandler);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player = new Player(usernameTextField.getText(), 20);
            }
        });

        con.add(titleNamePanel);
        con.add(startButtonPanel);
        con.add(startFieldPanel);
        window.setVisible(true);
    }

    public void createGameScreen() {
        titleNamePanel.setVisible(false);
        startButtonPanel.setVisible(false);
        startFieldPanel.setVisible(false);

        mainTextPanel = new JPanel();
        mainTextPanel.setBounds(100, 100, 600, 250);
        mainTextPanel.setBackground(Color.black);
        con.add(mainTextPanel);
        inventory = new Inventory();

        mainTextArea = new JTextArea("Welcome to the world, " + player.getUsername());
        mainTextArea.setBounds(100, 100, 600, 250);
        mainTextArea.setBackground(Color.black);
        mainTextArea.setForeground(Color.white);
        mainTextArea.setFont(normalFont);
        mainTextArea.setLineWrap(true);
        scroll = new JScrollPane(mainTextArea);
        mainTextPanel.add(mainTextArea);
        mainTextPanel.add(scroll);

        choiceButtonPanel = new JPanel();
        choiceButtonPanel.setBounds(250, 350, 300, 150);
        choiceButtonPanel.setBackground(Color.black);
        choiceButtonPanel.setLayout(new GridLayout(4, 1));
        con.add(choiceButtonPanel);

        choice1 = new JButton("chop wood");
        choice1.setBackground(Color.black);
        choice1.setFont(normalFont);

        choice1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //do your task
                Random lund = new Random();
                int which = lund.nextInt(2);
                int num = lund.nextInt(4) + 1;
                if (which == 0) {
                    whichOne = true;
                    for (int i = 0; i < num; i++) {
                        inventory.addItem(wood);
                    }
                } else {
                    whichOne = false;
                    for (int i = 0; i < num; i++) {
                        inventory.addItem(woodenPlank);
                    }
                }
                woodChopped = num;
                callWoodPrint();
            }});
        choiceButtonPanel.add(choice1);

        choice2 = new JButton("Choice 2");
        choice2.setBackground(Color.black);
        choice2.setFont(normalFont);
        choiceButtonPanel.add(choice2);

        choice3 = new JButton("Choice 3");
        choice3.setBackground(Color.black);
        choice3.setFont(normalFont);
        choiceButtonPanel.add(choice3);

        choice4 = new JButton("Check Inventory");
        choice4.setBackground(Color.black);
        choice4.setFont(normalFont);
        choiceButtonPanel.add(choice4);
        choice4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInventory();
            }
        });

        playerPanel = new JPanel();
        playerPanel.setBounds(100, 15, 700, 50);
        playerPanel.setBackground(Color.black);
        playerPanel.setLayout(new GridLayout(1, 3));
        con.add(playerPanel);
        hpLabel = new JLabel("HP: " + player.getHealth().toString());
        hpLabel.setFont(normalFont);
        hpLabel.setForeground(Color.white);
        playerPanel.add(hpLabel);
        weaponLabel = new JLabel("Current Item: " + inventory.getCurrentItem());
        weaponLabel.setFont(normalFont);
        weaponLabel.setForeground(Color.white);
        playerPanel.add(weaponLabel);
        weaponLabelName = new JLabel();
        weaponLabelName.setFont(normalFont);
        weaponLabelName.setForeground(Color.white);
        playerPanel.add(weaponLabelName);

        inventoryTextPanel = new JPanel();
        inventoryTextPanel.setBounds(100, 100, 600, 250);
        inventoryTextPanel.setBackground(Color.black);
        con.add(inventoryTextPanel);
        inventory = new Inventory();

        inventoryTextArea = new JTextArea("Inventory: \n");
        inventoryTextArea.setBounds(100, 100, 600, 250);
        inventoryTextArea.setBackground(Color.black);
        inventoryTextArea.setForeground(Color.white);
        inventoryTextArea.setFont(normalFont);
        inventoryTextArea.setLineWrap(true);
        inventoryTextPanel.add(inventoryTextArea);
    }

    public class TitleScreenHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            createGameScreen();
        }
    }

    public void callWoodPrint() {
        String ogText = mainTextArea.getText();
        if (whichOne == true) {
            mainTextArea.setText(ogText + "\nYou received " + woodChopped + " wood!");
        } else {
            mainTextArea.setText(ogText + "\nYou received " + woodChopped + " wooden planks!");
        }
        caret = (DefaultCaret)mainTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
    }

    public void openInventory() {
        new Test(inventory, 300, 300);
        mainTextPanel.setVisible(false);
        String total = "";
        for (Item i : inventory.getItems()) {
            int index = inventory.getItemNames().indexOf(i.getName()) + 1;
            total += total + index + ".  " +
                    i.getName() + " ("
                    + inventory.getNumberOfItems().get(inventory.getItems().indexOf(i)) + ")\n";
        }
        inventoryTextArea.setText(total);
        con.add(mainTextPanel);
        weaponLabel.setText("Current Item: " + inventory.getCurrentItem());
    }
}
