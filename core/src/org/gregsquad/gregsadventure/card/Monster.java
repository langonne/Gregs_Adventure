package org.gregsquad.gregsadventure.card;

public class Monster extends Card{
    private int level;
    private int damage;
    private int treasure;
    private String typeIncident;

    public Monster(int id, String name, String description,  int level, int damage, int treasure, String typeIncident){
        super(id, name, description);
        this.level = level;
        this.damage = damage;
        this.treasure = treasure;
        this.typeIncident = typeIncident;
    }

    public String getTypeIncident(){
        return typeIncident;
    }

    public int getTreasure(){
        return treasure;
    }


    public int getLevel(){
        return level;
    }

    public int getDamage(){
        return damage;
    }

    public void addDamage(int damage){
        this.damage += damage;
    }
}
