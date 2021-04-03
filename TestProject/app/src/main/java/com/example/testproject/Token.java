package com.example.testproject;

import java.io.Serializable;

/**
 * Class to store tokens characteristic (their value and their city)
 */
class Token implements Serializable {

    public static final String[] CITIES = new String[] {"Belston", "Campaign Hall", "Coms Array", "Covern MilitaryBase", "Elis North", "Elscon", "Glinnin Forest", "Helston Stopfare", "Keep", "Levington", "New Valden", "Norithxe", "Northern Settlement", "Ruins of Talmon" };

    private boolean value; // every token is either true or false
    private String city; // every token has a city name

    Token(String city, boolean value) {
        this.city = city;
        this.value = value;
    }

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

}
