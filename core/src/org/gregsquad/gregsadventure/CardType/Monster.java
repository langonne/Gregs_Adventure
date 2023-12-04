package org.gregsquad.gregsadventure.CardType;

import org.gregsquad.gregsadventure.Card;

public class Monster extends Card{
    private int level;
    private int strength;

    public Monster(String name, int level, int strength){
        this.level = level;
        this.strength = strength;
    }

    public int getLevel(){
        return level;
    }

    public int getStrength(){
        return strength;
    }

    public void addStrength(int strength){
        this.strength += strength;
    }
}
