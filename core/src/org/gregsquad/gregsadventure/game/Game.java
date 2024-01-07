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
    }


    /**
     * @brief Main method for debugging and testing.
     * Equivalent to the initial interface.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) { // Equivalent à l'interface de départ
        System.out.println("Start");
        Game game = new Game();

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
            if(game.help(1)){ // Appuie sur le bouton help
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


    /**
     * @brief Selects a card for debugging purposes (replaces card selection interface).
     */
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
    public boolean isGameStarted() {
        return isGameStarted;
    }
}