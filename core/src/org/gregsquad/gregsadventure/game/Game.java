package org.gregsquad.gregsadventure.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import org.gregsquad.gregsadventure.card.*;
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

    //DEBUG
    public static void main(String[] args) { // Equivalent à l'interface de départ
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
            game.monster = (Monster) cardOnTable;
            System.out.println("Monster on table : " + game.monster.getName() + " " + game.monster.getDamage()); // DEBUG
            //Interface mettre carte sur la table
            game.cardSelect();
            //Apparition des boutons
            //game.fight(game.monster); // Appuie sur le bouton fight
            //game.run(game.monster); // Appuie sur le bouton run
            if(game.Help(1)){ // Appuie sur le bouton help
                System.out.println("Help"); // DEBUG
                game.fight(game.monster);
            } else {
                System.out.println("No help"); // DEBUG
                game.fight(game.monster);
            }
        } else if( cardOnTable instanceof Curse){
            System.out.println("Curse on table "); // DEBUG
            ((Curse) cardOnTable).curse(game.currentPlayer);
        } else { // Class Treasure/Race/Class
            System.out.println("Other on table "); // DEBUG
            game.currentPlayer.getDeck().addCard(cardOnTable);
        }

        //Charity
        game.charity();

    }

    public void cardSelect(){ // function of DEBUG (remplace interface of select card)
        System.out.println("Card in your hand : "); // DEBUG
        for(Card card : currentPlayer.getDeck().getCards()){
            System.out.println(card.getName() + " " + card.getDescription()); // DEBUG
        }
        System.out.println("Select card : "); // DEBUG
        Scanner sc = new Scanner(System.in);
        int cardNumber = sc.nextInt();
        Card cardPlayed = currentPlayer.getDeck().getCard(cardNumber);

        if(cardPlayed instanceof Curse){
            System.out.println("Select player : "); // DEBUG
            int playerNumber = sc.nextInt();
            Player playerSelected = playerList.get(playerNumber);
            ((Curse) cardPlayed).play();
        } else if (cardPlayed instanceof Monster) {
            if(true && monster == null){ // Check si le joueur est celui dont c'est le tour
                monster = (Monster) cardPlayed;
                //Affichage des 3 bouttons de jeu
            }
        } else if (cardPlayed instanceof Equipement){
            Equipement equipementPlayed = (Equipement) cardPlayed;
            //Check if position is free
            for(Equipement equipement : currentPlayer.getStuff().getEquipements()){
                if(equipement.getPosition() == ((Equipement) cardPlayed).getPosition()){
                    //Ask remove or not
                    System.out.println("Remove equipement " + equipement.getName() + " ?"); // DEBUG
                    Scanner sc2 = new Scanner(System.in);
                    String answer = sc2.nextLine();
                    if(answer == "yes"){
                        currentPlayer.getStuff().removeEquipement(equipement);
                        treasureDiscard.addCard(equipement);
                        currentPlayer.getStuff().addEquipement(equipementPlayed);
                    } else {
                        return;
                    }
                }
            }
            
        }else {
            cardPlayed.play();
        }
        
    }

    public Stack getDonjonStack() {
        return donjonStack;
    }

    public void fight(Monster monster) { //button fight  // GARDER PARAMETRE UTILE ?
        System.out.println("Fight between player (" + currentPlayer.getDamage() + ") and " + monster.getName() + " (" + monster.getDamage() + ")"); // DEBUG
        if(currentPlayer.getDamage() > monster.getDamage()){

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
                System.out.println("Remove random object "); // DEBUG
                Random rand = new Random();
                int randNumber = rand.nextInt(player.getStuff().getSize());
                player.getStuff().removeEquipement(randNumber);
                break;

            case "loseLevel":
                if(player.getLevel() != 1){
                    System.out.println("Remove 1 level "); // DEBUG
                    player.addLevel(-1);
                    break;
                }
            default:
                System.out.println("Error : incident not found ");
                break;
        }
    }

    //Interface proposition d'aide (return treasure number + playerList)
    public boolean Help(int numberOfTreasure){ // true == helped

        

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
        int minLevelOtherPlayer = 11;
        for(Player player : playerList){
            if(player != currentPlayer){
                if(player.getLevel() < minLevelOtherPlayer){
                    minLevelOtherPlayer = player.getLevel();
                    return;
                }
            }
        }
        while(currentPlayer.getDeck().getSize() > 5){
            //Interface choix carte (return card)
            Card card = currentPlayer.getDeck().getCard(0);
            currentPlayer.getDeck().removeCard(card);
            if(currentPlayer.getLevel() > minLevelOtherPlayer){
                //Interface choix joueur (return player)
                Player player = playerList.get(0);
                player.getDeck().addCard(card);
                return;
            } else {
                if(card instanceof Equipement){
                    treasureDiscard.addCard(card);
                } else {
                    donjonDiscard.addCard(card);
                }
            }

        }
    }


}