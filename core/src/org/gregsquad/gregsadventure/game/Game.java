package org.gregsquad.gregsadventure.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import org.gregsquad.gregsadventure.card.*;
import org.gregsquad.gregsadventure.game.Player;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;


public class Game {
    static final int DICE_NEED_TO_RUN = 5;
    private ArrayList<Player> playerList = new ArrayList<Player>();
    private Stack donjonStack;
    private Stack treasureStack;
    private Discard donjonDiscard;
    private Discard treasureDiscard;
    private Player currentPlayer;
    private Monster monster;
    private Player playerHelp;
    // Voir on met la liste des joeurs qui se battent, ici c'est bon ??

    //DEBUG
    public static void main(String[] args) { // Equivalent à l'inverface de départ
        System.out.println("Start");
        Game game = new Game();
        game.donjonStack = new Stack();
        game.donjonStack.creation();
        game.treasureStack = new Stack();
        game.treasureStack.creation();
        game.donjonDiscard = new Discard();
        game.treasureDiscard = new Discard();

        for(int i = 0; i < 5; i++){ // DEBUG : interface de création de joueur
            game.playerList.add(new Player("Player" + i));
            System.out.println("Creation player " + i);
            for(int j = 0; j < 2; j++){ // A DEPLACER DANS LA CREATION DE JOUEUR
                game.playerList.get(i).getDeck().addCard(game.donjonStack.draw());
                game.playerList.get(i).getDeck().addCard(game.treasureStack.draw());
            }
        }

        game.currentPlayer = game.playerList.get(0);


        //Draw
        Card cardOnTable = game.donjonStack.draw();
        // if Card is Monster
        if(cardOnTable instanceof Monster){
            System.out.println("Monster on table "); // DEBUG
            game.monster = (Monster) cardOnTable;
            //Interface mettre carte sur la table
            //game.fight(game.monster); // Appuie sur le bouton fight
            game.run(game.monster); // Appuie sur le bouton run
            //game.Help(game.playerList, 1); // Appuie sur le bouton help
        } else if( cardOnTable instanceof Curse){
            System.out.println("Curse on table "); // DEBUG
            ((Curse) cardOnTable).curse(game.currentPlayer);
        } else { // Class Treasure/Race/Class
            System.out.println("Other on table "); // DEBUG
            game.currentPlayer.getDeck().addCard(cardOnTable);
        }

    }



    public Stack getDonjonStack() {
        return donjonStack;
    }

    public void fight(Monster monster) { //button fight  
        if(currentPlayer.getDamage() >= monster.getDamage()){
            System.out.println("Player win"); // DEBUG
        } else {
            System.out.println("Player lose"); // DEBUG
            run(monster);
        }
    }


    public boolean run(Monster monster) { //button run, true = success 
        System.out.println("Run"); // DEBUG
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1 + currentPlayer.getDiceBuff();
        if(diceResult >= DICE_NEED_TO_RUN){
            System.out.println("Run success with " + diceResult); // DEBUG
            return true;
        } else {
            System.out.println("Run failled with " + diceResult); // DEBUG
            incident(currentPlayer);
            if(playerHelp != null){
                incident(playerHelp);
            }
            return false;
        }

    }

    protected void incident(Player player){
        if(player == null){
            System.out.println("Error : No player"); // DEBUG
            return;
        }
        switch (monster.getTypeIncident()) {
            case "death":
                System.out.println("Death of player "); // DEBUG
                player.getStuff().clearStuff();
                break;
        
            case "loseObject":
                //Remove 1 object
                System.out.println("Remove random object "); // DEBUG
                Random rand = new Random();
                int randNumber = rand.nextInt(player.getStuff().getSize());
                player.getStuff().removeObject(randNumber);
                break;

            case "loseLevel":
                System.out.println("Remove 1 level "); // DEBUG
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