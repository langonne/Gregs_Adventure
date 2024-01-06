package org.gregsquad.gregsadventure.game;

import java.util.Random;
import java.io.Serializable;

import org.gregsquad.gregsadventure.card.Card;
import org.gregsquad.gregsadventure.card.CardList;
import org.gregsquad.gregsadventure.card.Deck;
import org.gregsquad.gregsadventure.card.Equipement;
import org.gregsquad.gregsadventure.card.Race;
import org.gregsquad.gregsadventure.card.Class;

public class Player implements Serializable{
    private String name;
    private int level = 0;
    private int damage = 0;

    private Class classe;
    private Race race; 
    private Deck deck;

    private Stuff stuff;

    private int diceBuff;
    private int treasuresForFight;

    public Player(String name){
        this.name = name;
        this.level = 1;
        this.damage = 1;
        this.classe = new Class(0, "Défaut", "Aucune classe");
        this.race = new Race(0, "Défaut", "Aucune Race", 0, 0);
        this.deck = new Deck();
        this.stuff = new Stuff();
        this.diceBuff = 0;
    }

    public String getName(){
        return this.name;
    }

    public int getDiceBuff(){
        this.diceBuff = this.race.getBonusDice();
        return this.diceBuff;
    }

    public void setDiceBuff(int diceBuff){
        this.diceBuff = diceBuff;
    }
    public int getTreasuresForFight(){
        return this.treasuresForFight;
    }

    public void setTreasuresForFight(int treasures){
        this.treasuresForFight = treasures;
    }

    public int getLevel(){
        return this.level;
    }

    public void addLevel(int level){
        this.level += level;
    }

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

    public void setDamage(int damage){
        this.damage = damage;
    }

    public void addDamage(int damage){
        this.damage += damage;
    }

 


    public Class getPlayerClass(){
        return this.classe;
    }

    public Race getRace(){
        return this.race;
    }
    public void setPlayerClass(Class classe){
        this.classe = classe;
    }

    public void setRace(Race race){
        this.race = race;
    }
    public Deck getDeck(){
        return this.deck;
    }

    public Stuff getStuff(){
        return this.stuff;
    }
}
