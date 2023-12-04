package org.gregsquad.gregsadventure;

import java.util.ArrayList;

import org.gregsquad.gregsadventure.CardType.Job;
import org.gregsquad.gregsadventure.CardType.Race;

public class Player {
    private int id;
    private String name;
    private int level;
    private Race race;
    private Job job;
    private int strength;
    private Objects heavyObjects;
    private Objects lightObjects;
    private ArrayList<Card> hand;
    private int diceModifier;







    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public Race getRace(){
        return race;
    }

    public Job getJob(){
        return job;
    }

    public int getStrength(){
        return strength;
    }

    public Objects getHeavyObjects(){
        return heavyObjects;
    }

    public Objects getLightObjects(){
        return lightObjects;
    }

    public ArrayList<Card> getHand(){
        return hand;
    }

    public int getDiceModifier(){
        return diceModifier;
    }

    public void setDiceModifier(int diceModifier){
        this.diceModifier = diceModifier;
    }



    

}
