package org.gregsquad.gregsadventure.card;

public class Race extends Card {
    private int bonusDamage;
    private int bonusDice;

    public Race(int id, String name, String description, int bonusDamage, int bonusDice){
        super(id, name, description);
        this.bonusDamage = bonusDamage;
        this.bonusDice = bonusDice;
    }

    public int getBonusDamage(){
        return this.bonusDamage;
    }

    public int getBonusDice(){
        return this.bonusDice;
    }
}
