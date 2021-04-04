package com.example.testproject;

import java.io.Serializable;
import java.util.HashMap;

public class Clue implements Serializable {

    public static final String[] TYPES = new String[] {"Forest", "Lake", "Mountain", "City"};

    public static final HashMap<String, Integer> IMAGES = new HashMap<String, Integer>() {{
        put("Forest true",      R.drawable.forest);
        put("Mountain true",    R.drawable.mountain);
        put("Lake true",        R.drawable.lake);
        put("City true",        R.drawable.city);
        put("Forest false",     R.drawable.forest_false);
        put("Mountain false",   R.drawable.mountain_false);
        put("Lake false",       R.drawable.lake_false);
        put("City false",       R.drawable.city_false);
        put("Unknown",          R.drawable.book); }};

    private String type;
    private boolean visible;
    private boolean clue;
    private Integer icon;

    public Clue(String type, boolean clue, boolean visible) {
        this.type = type;
        this.clue = clue;
        this.icon = IMAGES.get(type + " " + clue);
        this.visible = visible;
    }

    public boolean isTrue() {return clue; }

    public String getType() {
        if (isVisible()) {
            if (isTrue()) {
                return type;
            } else {
                return "Avoided " + type;
            }
        } else {
            return "Unknown";
        }
    }

    public Integer getIcon() {
        if (isVisible()) {
            return icon;
        } else {
            return IMAGES.get("Unknown");
        }
    }

    public boolean isVisible() {return visible; }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return getType();
    }
}
