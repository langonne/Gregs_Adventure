package org.gregsquad.gregsadventure.card;

import java.io.Serializable;
/**
 * @class Monster
 * @brief Represents a monster card in the game, derived from Card.
 */
public class Monster extends Card implements Serializable{
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


    /**
     * @brief Gets the type of incident associated with the monster.
     * @return The type of incident.
     */
    public String getTypeIncident(){
        return typeIncident;
    }

    /**
     * @brief Gets the treasure value of the monster.
     * @return The treasure value.
     */
    public int getTreasure(){
        return treasure;
    }

    /**
     * @brief Gets the level of the monster.
     * @return The level.
     */
    public int getLevel(){
        return level;
    }
    /**
     * @brief Gets the damage of the monster.
     * @return The damage value.
     */
    public int getDamage(){
        return damage;
    }
    /**
     * @brief Adds additional damage to the monster.
     * @param damage The additional damage to add.
     */
    public void addDamage(int damage){
        this.damage += damage;
    }
}
