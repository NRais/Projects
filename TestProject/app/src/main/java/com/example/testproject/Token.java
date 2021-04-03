package com.example.testproject;

class Token {

    private boolean value;
    private String city;

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

}
