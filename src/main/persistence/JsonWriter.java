package persistence;

import model.Inventory;
import model.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// REFERENCE: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a writer that writes JSON representation of workroom to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of inventory to file
    public void writeInventory(Inventory inv, Player p) {
        JSONObject jsonP = p.toJson();
        JSONObject json = inv.toJson();
        JSONArray jsonFull = new JSONArray();
        jsonFull.put(json);
        jsonFull.put(jsonP);
        JSONObject jsonPapa = new JSONObject();
        jsonPapa.put("MOMOS", jsonFull);
        saveToFile(jsonPapa.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of player to file
    public void writePlayer(Player player) {
        JSONObject json = player.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
