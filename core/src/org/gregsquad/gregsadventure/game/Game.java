package org.gregsquad.gregsadventure.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import org.gregsquad.gregsadventure.card.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;

/**
 * @class Game
 * @brief Represents the main game logic and flow.
 */
public class Game {
    private static Game instance;
    static final int DICE_NEED_TO_RUN = 5;
    static final int INITIAL_DECK_SIZE = 4;
    private ArrayList<Player> playerList;
    private Stack donjonStack;
    private Stack treasureStack;
    private Discard donjonDiscard;
    private Discard treasureDiscard;
    private Player currentPlayer;
    private Monster monster;
    private Player playerHelp;
    private boolean isGameStarted;

    /**
     * @brief Private constructor to ensure singleton pattern.
     */
    private Game() {
        playerList = new ArrayList<Player>();
        donjonStack = new Stack();
        treasureStack = new Stack();
        donjonDiscard = new Discard();
        treasureDiscard = new Discard();
        isGameStarted = false;
    }

    /**
     * @brief Gets the singleton instance of the Game class.
     * @return The Game instance.
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }


    /**
     * @brief Initializes the game.
     */
    public void init(){
        int numberOfPlayer = this.playerList.size();
        isGameStarted = true;
        //Initialize of deck of all players
        for(Player playerList : playerList){
            for(int j = 0; j < INITIAL_DECK_SIZE/2; j++){
                playerList.getDeck().addCard(treasureStack.draw());
                playerList.getDeck().addCard(donjonStack.draw());
            }
        }         
    }
        
        System.out.println("[GAME] [DECK] [SIZE] : " + playerList.get(0).getDeck().getSize() + " NAME : " + playerList.get(0).getName()); // DEBUG
    }

    /**
     * @brief Initiates a fight between the player and a monster.
     * @param monster The monster to fight.
     */
    public void fight(Monster monster) { //button fight  // GARDER PARAMETRE UTILE ?
        System.out.println("Fight between player (" + currentPlayer.getDamage() + ") and " + monster.getName() + " (" + monster.getDamage() + ")"); // DEBUG
        if(currentPlayer.getDamage() > monster.getDamage()){

            System.out.println("Player win"); // DEBUG
        } else {
            System.out.println("Player lose"); // DEBUG
            run(monster);
        }
    }

    /**
     * @brief Attempts to run away from a monster.
     * @param monster The monster to run away from.
     * @return True if the run is successful, false otherwise.
     */
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

    /**
     * @brief Handles incidents that may occur during the game.
     * @param player The player involved in the incident.
     */
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

    /**
     * @brief Offers help to another player during a fight.
     * @param numberOfTreasure The number of treasures offered for help.
     * @return True if the player provided help, false otherwise.
     */
    public boolean help(int numberOfTreasure){ // true == helped

        

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


    /**
     * @brief Manages the charity phase of the game.
     */
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


    /**
     * @brief Sets the player list for the game.
     * @param playerList The list of players to set.
     */
    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    /**
     * @brief Sets the donjon stack for the game.
     * @param donjonStack The donjon stack to set.
     */
    public Stack getDonjonStack() {
        return donjonStack;
    }

    /**
     * @brief Sets the treasure stack for the game.
     * @param treasureStack The treasure stack to set.
     */
    public Stack getTreasureStack() {
        return treasureStack;
    }


    /**
     * @brief Sets the donjon discard for the game.
     * @param donjonDiscard The donjon discard to set.
     */
    public Discard getDonjonDiscard() {
        return donjonDiscard;
    }

    /**
     * @brief Sets the treasure discard for the game.
     * @param treasureDiscard The treasure discard to set.
     */
    public Discard getTreasureDiscard() {
        return treasureDiscard;
    }

    /**
     * @brief Sets the current player for the game.
     * @param currentPlayer The current player to set.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @brief Sets the current player for the game.
     * @param currentPlayer The current player to set.
     */
    public Monster getMonster() {
        return monster;
    }

    /**
     * @brief Sets the current player for the game.
     * @param currentPlayer The current player to set.
     */
    public void addPlayer(Player player) {
        playerList.add(player);
    }

    /**
     * @brief Sets the current player for the game.
     * @param currentPlayer The current player to set.
     */

    public void removePlayer(Player player) {
        playerList.remove(player);
    }

    /**
     * @brief Sets the current player for the game.
     * @param currentPlayer The current player to set.
     */
    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    /**
     * @brief Sets the current player for the game.
     * @param currentPlayer The current player to set.
     */
    public Player getPlayer(int index) {
        return playerList.get(index);
    }

    /**
     * @brief Sets the current player for the game.
     * @param currentPlayer The current player to set.
     */
    public boolean isGameStarted() {
        return isGameStarted;
    }


    public void treasureDraw(Player player){
        player.getDeck().addCard(treasureStack.draw());
    }

    public void donjonDraw(Player player){
        player.getDeck().addCard(donjonStack.draw());
    }

}