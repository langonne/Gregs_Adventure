package org.gregsquad.gregsadventure.card;

public abstract class Card {
    
    protected int id;
    protected String name;
    //protected String type;
    protected String description;

    public Card(int id, String name, String description) {
        this.id = id;
        this.name = name;
        //this.type = type;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    /*
    public String getType() {
        return type;
    }*/
    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }
    /*public void setType(String type) {
        if (type != null && !type.isEmpty()) {
            this.type = type;
        }
    }*/
    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        }
    }
    
}
