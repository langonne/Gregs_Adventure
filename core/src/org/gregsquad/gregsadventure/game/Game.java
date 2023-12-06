package org.gregsquad.gregsadventure.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import org.gregsquad.gregsadventure.card.*;


public class Game {
    static final int DICE_NEED_TO_RUN = 5;
    private ArrayList<Player> playerList;
    private Stack donjonStack;
    private Stack treasureStack;
    private Discard donjonDiscard;
    private Discard treasureDiscard;
    private Player currentPlayer;
    private Monster monster;
    Player playerHelp;
    // Voir on met la liste des joeurs qui se battent, ici c'est bon ??

    public void fight(Monster monster) { //button fight  

        if(currentPlayer.getDamage() >= monster.getDamage()){
            
        } else {
            run(monster);
        }



    }


    public boolean run(Monster monster) { //button run, true = success 
        Random rand = new Random();
        if(rand.nextInt(6) + 1 + currentPlayer.getRunAway() >= DICE_NEED_TO_RUN){
            return true;
        } else {
            incident(currentPlayer);
            incident(playerHelp);
            return false;
        }

    }

    protected void incident(Player player){
        switch (monster.getTypeIncident()) {
            case "death":
                //Remove stuff 
                break;
        
            case "loseObject":
                //Remove 1 object
                break;

            case "loseLevel":
                player.addLevel(-1);
                break;
            default:
                break;
        }
    }

    //Interface proposition d'aide (return treasure number + playerList)
    public boolean Help(LinkedList<Player> playersList, int numberOfTreasure){ // true == helped

        

        //Interface to other players (return playerList)

        LinkedList<Player> playerListWantTohelp = new LinkedList<Player>();

        //Interface choix joueur (return player sur playerHelp)

        if(playerHelp == null){

            return false;
        } else {
            currentPlayer.setDamage(currentPlayer.getDamage() + playerHelp.getDamage());

            playerHelp.setTreasuresForFight(numberOfTreasure);
            currentPlayer.setTreasuresForFight(monster.getTreasure() - numberOfTreasure);


            return true;
        }
    }



    public void charity(){
        // TODO implement here
    }


}