package org.gregsquad.gregsadventure.game;

import java.util.ArrayList;
import java.util.LinkedList;

import org.gregsquad.gregsadventure.card.*;


public class Game {
    private ArrayList<Player> playerList;
    private Stack donjonStack;
    private Stack treasureStack;
    private Discard donjonDiscard;
    private Discard treasureDiscard;


    public void fight(LinkedList<Player> playerList, Monster monster) {
        int damagePlayer = playerList.get(0).getDamage();
        if(playerList.size() > 1){
            damagePlayer = damagePlayer + playerList.get(1).getDamage();
        }
        int damageMonster = monster.getDamage();
        //Show Interface Fight

        //Show interface Card to all players
        boolean cardHasBeenPlayed = false; // return of the interface

        if(cardHasBeenPlayed){
            fight(playerList, monster);
            return;
        } else {

            //Show interface Action
            String action = "Help"; // return of the interface

            switch (action) {
                case "Help":
                    if(playerList.size() == 1){
                        System.out.println("D2 : Help");
                        //Show interface Select Player

                        //Show proposal interface to players selected

                        //Show proposal choice interface to player

                        Player helpPlayer = new Player(); // return of the interface proposal choice
                        
                        if(helpPlayer == null){
                            
                        } else {
                            LinkedList allPlayerList = new LinkedList<Player>();
                            allPlayerList.add(playerList.get(0));
                            allPlayerList.add(helpPlayer);
                            fight(allPlayerList, monster);
                            return;
                        }
                    } else {
                        System.out.println("You cannot ask help is you are already 2");
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