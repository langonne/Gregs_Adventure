package org.gregsquad.gregsadventure.game;

import java.util.Random;
import java.io.Serializable;

import org.gregsquad.gregsadventure.card.Card;
import org.gregsquad.gregsadventure.card.CardList;
import org.gregsquad.gregsadventure.card.Deck;
import org.gregsquad.gregsadventure.card.Equipement;
import org.gregsquad.gregsadventure.card.Race;
import org.gregsquad.gregsadventure.card.Class;

/**
 * @class Player
 * @brief Represents a player in the game with attributes such as name, level, and damage.
 */
public class Player implements Serializable {

    private int id = 0;

    private String name;
    private int level = 0;
    private int damage = 0;

    private Class classe;
    private Race race; 
    private Deck deck;

    private Stuff stuff;

    private int diceBuff;
    private int treasuresForFight;

    /**
     * @brief Constructor for Player class.
     * @param name The name of the player.
     */
    public Player(int id, String name){
        this.id = id;
        this.name = name;
        this.level = 1;
        this.damage = 1;
        this.classe = new Class(0, "Défaut", "Aucune classe");
        this.race = new Race(0, "Défaut", "Aucune Race", 0, 0);
        this.deck = new Deck();
        this.stuff = new Stuff();
        this.diceBuff = 0;
    }



    /**
     * @brief Gets the name of the player.
     * @return The name of the player.
     */
    public String getName(){
        return this.name;
    }

    /**
     * @brief Gets the dice bonus based on the player's race.
     * @return The dice bonus.
     */
    public int getDiceBuff(){
        this.diceBuff = this.race.getBonusDice();
        return this.diceBuff;
    }
    /**
     * @brief Sets the dice bonus for the player.
     * @param diceBuff The dice bonus to set.
     */
    public void setDiceBuff(int diceBuff){
        this.diceBuff = diceBuff;
    }
    /**
     * @brief Gets the number of treasures the player has for a fight.
     * @return The number of treasures for the fight.
     */
    public int getTreasuresForFight(){
        return this.treasuresForFight;
    }
    /**
     * @brief Sets the number of treasures the player has for a fight.
     * @param treasures The number of treasures for the fight.
     */
    public void setTreasuresForFight(int treasures){
        this.treasuresForFight = treasures;
    }
    /**
     * @brief Gets the player's current level.
     * @return The player's level.
     */
    public int getLevel(){
        return this.level;
    }
    /**
     * @brief Adds a specified level to the player.
     * @param level The level to add.
     */
    public void addLevel(int level){
        this.level += level;
    }
    /**
     * @brief Calculates and gets the total damage of the player.
     * @return The total damage of the player.
     */
    public int getDamage(){//We do the calcul of damage bonus here
        damage = level;
        for(Equipement equipement : stuff.getEquipements()){
            damage += equipement.getBonus();
            if(equipement.comboValid(this.race.getName())){
                damage += equipement.getCombo();
            }
        }
        damage += this.race.getBonusDamage();
        return this.damage;
    }
    /**
     * @brief Sets the damage for the player.
     * @param damage The damage to set.
     */
    public void setDamage(int damage){
        this.damage = damage;
    }

    /**
     * @brief Adds a specified amount of damage to the player.
     * @param damage The amount of damage to add.
     */
    public void addDamage(int damage){
        this.damage += damage;
    }

    
    /**
     * @brief Gets the player's class.
     * @return The player's class.
     */
    public Class getPlayerClass(){
        return this.classe;
    }

    /**
     * @brief Gets the player's race.
     * @return The player's race.
     */
    public Race getRace(){
        return this.race;
    }
    /**
     * @brief Sets the player's class.
     * @param classe The class to set.
     */
    public void setPlayerClass(Class classe){
        this.classe = classe;
    }
    /**
     * @brief Sets the player's race.
     * @param race The race to set.
     */
    public void setRace(Race race){
        this.race = race;
    }
    /**
     * @brief Gets the player's deck.
     * @return The player's deck.
     */
    public Deck getDeck(){
        return this.deck;
    }
    /**
     * @brief Gets the player's stuff.
     * @return The player's stuff.
     */
    public Stuff getStuff(){
        return this.stuff;
    }

    public int getId() {
        return id;
    }
}
