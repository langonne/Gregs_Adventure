package org.gregsquad.gregsadventure;

public class Card {
    private int id;
    private String name;
    private String type;

    public Card() {
        this.id = 0;
        this.name = "Undefined";
        this.type = "Undefined";
    }

    public Card(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
}
