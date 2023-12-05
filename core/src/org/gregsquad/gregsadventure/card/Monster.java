package org.gregsquad.gregsadventure.card;

public class Monster extends Card{
    private int level;
    private int damage;

    public Monster(int id, String name, String description,  int level, int damage){
        super(id, name, description);
        this.level = level;
        this.damage = damage;
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
