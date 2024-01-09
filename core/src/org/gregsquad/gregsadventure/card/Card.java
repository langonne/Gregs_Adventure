package org.gregsquad.gregsadventure.card;

import java.io.Serializable;

import org.gregsquad.gregsadventure.game.Player;

public abstract class Card implements Serializable {
    
    protected int id;
    protected String name;
    protected String description;

    public Card(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
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
    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        }
    }

    public void play() {
        System.out.println("Error card without type");
    }
    
}
