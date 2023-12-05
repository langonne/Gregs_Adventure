package org.gregsquad.gregsadventure.game;

import java.util.LinkedList;
import java.util.Objects;

import org.gregsquad.gregsadventure.card.Card;
import org.gregsquad.gregsadventure.card.Deck;

public class Player {
    private String name;
    private int level = 0;
    private int damage = 0;
    private int runAway = 0;

    private Card classe; // Mettre en place les classes comme héritage de Card
    private Card race; // Pareil

    private Deck hand;

    private Objects heavyObjects;
    private Objects lightObjects;

    private int diceModifier;
    private int TreasuresForFight;

    public int getTreasuresForFight(){
        return this.TreasuresForFight;
    }

    public void setTreasuresForFight(int TreasuresForFight){
        this.TreasuresForFight = TreasuresForFight;
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

    public int getRunAway(){ // PAS SUR de si c'est utile
        return this.runAway;
    } 

    public Card getClasse(){
        return this.classe;
    }

    public Card getRace(){
        return this.race;
    }







}
