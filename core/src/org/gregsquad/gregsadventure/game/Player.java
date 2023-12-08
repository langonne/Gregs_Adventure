package org.gregsquad.gregsadventure.game;

import java.util.LinkedList;
import java.util.Objects;

import org.gregsquad.gregsadventure.card.Card;
import org.gregsquad.gregsadventure.card.Deck;

public class Player {
    private String name;
    private int level = 0;
    private int damage = 0;

    private Card classe; // Mettre en place les classes comme héritage de Card
    private Card race; // Pareil

    private Deck deck;

    private Stuff stuff;

    private int diceModifier;
    private int treasuresForFight;

    public Player(String name){
        this.name = name;
        this.level = 1;
        this.damage = 1;
        this.deck = new Deck();
        this.stuff = new Stuff();
    }

    public int gettreasuresForFight(){
        return this.treasuresForFight;
    }

    public void settreasuresForFight(int treasuresForFight){
        this.treasuresForFight = treasuresForFight;
    }

    public int getLevel(){
        return this.level;
    }

    public void addLevel(int level){
        this.level += level;
    }

    public int getDamage(){
        return this.damage;
    }

    public void setDamage(int damage){
        this.damage = damage;
    }

    public void addDamage(int damage){
        this.damage += damage;
    }
/*
 *  public int getRunAway(){ // PAS SUR de si c'est utile (et ne fonctionne pas)
        return this.runAway;
    } 
 */


    public Card getClasse(){
        return this.classe;
    }

    public Card getRace(){
        return this.race;
    }

    public Deck getDeck(){
        return this.deck;
    }

    public Stuff getStuff(){
        return this.stuff;
    }







}
