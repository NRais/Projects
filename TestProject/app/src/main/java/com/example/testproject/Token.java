package com.example.testproject;

import java.io.Serializable;

/**
 * Class to store tokens characteristic (their value and their city)
 */
class Token implements Serializable {

    public static final String[] CITIES = {"Belston", "Campaign Hall", "Coms Array", "Covern MilitaryBase", "Elis North", "Elscon", "Glinsmire", "Glinnin Forest", "Helston Stopfare", "Keep", "Levington", "New Valden", "Norithxe", "Northern Settlement", "Quor", "Ruins of Talmon" };
    public static final String[][] TYPES = {{Clue.TYPES[0]}, // belston
            {}, // campaign hall
            {Clue.TYPES[0], Clue.TYPES[1], Clue.TYPES[3]}, // coms array
            {Clue.TYPES[1], Clue.TYPES[2]}, // covern military
            {Clue.TYPES[0], Clue.TYPES[2], Clue.TYPES[3]}, // elis north
            {Clue.TYPES[1], Clue.TYPES[3]}, // elscon
            {Clue.TYPES[3]}, // glinsmire
            {Clue.TYPES[0], Clue.TYPES[2]}, // glinnin forest
            {Clue.TYPES[0], Clue.TYPES[1]}, // helston stopfare
            {Clue.TYPES[0], Clue.TYPES[1], Clue.TYPES[2]}, // keep
            {Clue.TYPES[2], Clue.TYPES[3]}, // levington
            {Clue.TYPES[1], Clue.TYPES[2], Clue.TYPES[3]}, // new valden
            {Clue.TYPES[0], Clue.TYPES[3]}, // northixe
            {Clue.TYPES[1]}, // northern settlement
            {Clue.TYPES[0], Clue.TYPES[1], Clue.TYPES[2], Clue.TYPES[3]}, // quor
            {Clue.TYPES[2]}, // ruins of talmon
            
    };

    private boolean revealed; // can other players see the token
    private boolean value; // every token is either true or false
    private String city; // every token has a city name

    Token(String city, boolean value) {
        this.city = city;
        this.value = value;
        this.revealed = false;
    }

    public boolean isRevealed() {
        return revealed;
    }
    public void setRevealed(boolean value) {this.revealed = value;}

    public boolean getValue() {
        return value;
    }
    public String getCity() {
        return city;
    }

    public int compareTo( Token other ) {
        String compare1 = this.toString();
        String compare2 = other.toString();

        return compare1.compareTo( compare2 );
    }

    public String toString()
    {
        return this.city + "-" + this.value;
    }

    // STATIC METHODS

    /**
     * Return a set of values for the city that is passed
     *
     * @param city
     * @return
     */
    public static String[] getType(String city) {
        int found = -1;

        for (int i = 0; i < CITIES.length; i++) {
            if (city.equals(CITIES[i])) {
                found = i;
                break;
            }
        }

        return TYPES[found];
    }

    /**
     * Return a set of images for a set of values
     *
     * @param type
     * @return
     */
    public static Integer[] getImages(String[] type) {
        Integer[] ints = new Integer[4];

        // for each terrain type
        for (int i = 0; i < 4; i++) {

            // default value is NOT contains it
            String item = null;

            for (String s : type) {
                if (s.equals(Clue.TYPES[i])) {
                    // found item, so it contains it
                    item = Clue.TYPES[i] + " true";
                }
            }

            ints[i] = Clue.IMAGES.get(item);
        }

        return ints;
    }

    /**
     * Return a set of booleans for the terrain
     *
     * @param type
     * @return
     */
    public static Boolean[] getTerrain(String[] type) {
        Boolean[] terrains = new Boolean[4];

        // for each terrain type
        for (int i = 0; i < 4; i++) {

            // default is false
            boolean result = false;

            // but we will go search it in the terrain list
            for (String s : type) {

                // if this item is in the list
                if (s.equals(Clue.TYPES[i])) {
                    // we change our result to true
                    result = true;
                }
            }

            terrains[i] = result;
        }

        return terrains;
    }
}
