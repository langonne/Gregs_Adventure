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
        int damagePlayer = playerList.get(0).getStrength();
        damagePlayer = damagePlayer + playerList.get(1).getStrength();
        int damageMonster = monster.getStrength();
        //Show Interface Fight

        //Show interface Card to all players
        boolean cardHasBeenPlayed = false; // return of the interface

        if(cardHasBeenPlayed){
            fight(player, monster);
            return;
        } else {

            //Show interface Action
            String action = "Help"; // return of the interface

            switch (action) {
                case "Help":
                    System.out.println("D2 : Help");
                    //Show interface Select Player

                    //Show proposal interface to players selected

                    //Show proposal choice interface to player

                    helpPlayer = new Player(); // return of the interface proposal choice
                    
                    if(helpPlayer == NULL){
                        
                    } else {
                        allPlayerList = new ArrayList<Player>();
                        allPlayerList.add(player);
                        allPlayerList.add(helpPlayer);
                        fight(player, monster);
                        return;
                    }
                    break;
                case "Run":
                    System.out.println("D2 : Run");


                    break;
                case "Fight":
                    System.out.println("D2 : Fight");

                    break;


                default:
                    System.out.println("D2 : Error");
                    break;
            }


        }



    }

    public void charity(){
        // TODO implement here
    }




}
