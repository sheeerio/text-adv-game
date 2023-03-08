package persistence;

import model.Inventory;
import model.items.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// REFERENCE: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Inventory read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseInventory(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private Inventory parseInventory(JSONObject jsonObject) {
        Inventory inv = new Inventory();
        addItems(inv, jsonObject);
        return inv;
    }

    // MODIFIES: inv
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addItems(Inventory inv, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("MOMOS");
        JSONObject itemObject = jsonArray.getJSONObject(0);
        JSONArray jsonArray2 = itemObject.getJSONArray("items");
        for (Object json : jsonArray2) {
            JSONObject nextItem = (JSONObject) json;
            addItem(inv, nextItem);
        }
    }

    // MODIFIES: inv
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addItem(Inventory inv, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Integer num = jsonObject.getInt("number");
        Item item;
        if (name.equals("Wood") || name.equals("Wooden Planks")) {
            item = new Block(name, null);
        } else if (name.equals("Sticks")) {
            item = new Misc(name);
        } else if (name.equals("Raw Meat")) {
            item = new Food(name, 5);
        } else {
            item = new Weapon(name, null);
        }
        for (int i = 0; i < num; i++) {
            inv.addItem(item);
        }
    }
}