package com.example.tlds.testwebservice;

/**
 * Created by PC on 4/27/2016.
 */
public class Match {
    private String field_name;
    private String maximum_players, price;

    public Match(String field_name, String maximum_players, String price){
        this.field_name = field_name;
        this.maximum_players = maximum_players;
        this.price = price;
    }

    public String getMaximum_players() {
        return maximum_players;
    }

    public void setMaximum_players(String maximum_players) {
        this.maximum_players = maximum_players;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
