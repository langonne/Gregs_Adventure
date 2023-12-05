package org.gregsquad.gregsadventure;

import java.util.ArrayList;
import org.gregsquad.gregsadventure.CardType.*;

public class CoreLauncher {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    private ArrayList<Player> playerList;
    private Deck donjonDeck;
    private Deck TreasureDeck;
    private Card cardOnTable;
    
    

    public CoreLauncher() {
        this.playerList = new ArrayList<Player>();
        this.donjonDeck = new Deck();
        this.TreasureDeck = new Deck();
    }

    public void gameController() {
        // TODO implement here
    }


    public void fight(ArrayList<Player> playerList, Monster monster) {
        // TODO implement here
    }

    public void charity(){
        // TODO implement here
    }




}
