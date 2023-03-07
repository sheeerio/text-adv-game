package model.items;

import org.json.JSONObject;

public interface Item {

    public String getName();

    public JSONObject toJson();
}
