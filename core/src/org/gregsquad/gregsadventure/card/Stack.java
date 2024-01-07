package org.gregsquad.gregsadventure.card;

import java.util.Collections;
import java.util.Random;
import java.io.Serializable;

import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
/**
 * @class Stack
 * @brief Represents a stack of cards in the game, derived from CardList.
 */
public class Stack extends CardList implements Serializable {
    /**
     * @brief Default constructor for Stack class.
     * Initializes the stack by loading cards from the configuration file and shuffling them.
     */
    public Stack() {
        System.out.println("Creating stack"); // DEBUG
        //boolean finish = true;
        int i = 1;
        int id = 1;
        while(i != 15){ 
            
            cards.add(new Monster(i, "DebugCard " + i, "Monster " + i, 1, 1, 1, "death"));
            String key = Integer.toString(i);
            for (int j = 0; j < ConfigLoader.getInt(key + "_number"); j++) {
                    // Use key and value to create a card
                    switch (ConfigLoader.getString(key)) {
                        case "Curse":
                            cards.add(new Curse(id, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getString(key + "_type"), ConfigLoader.getInt(key + "_value")));
                            
                            break;
                        case "Monster":
                            cards.add(new Monster(id, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getInt(key + "_level"), ConfigLoader.getInt(key + "_damage"), ConfigLoader.getInt(key + "_treasure"), ConfigLoader.getString(key + "_incident")));
                            
                            break;
                        case "Equipement":
                            if(ConfigLoader.isValid(key + "_conditionBonus") && ConfigLoader.isValid(key + "_combo")){
                                System.out.println("combo");
                                cards.add(new Equipement(id, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getInt(key + "_position"), ConfigLoader.getInt(key + "_bonus"), ConfigLoader.getBoolean(key + "_equipementSize"), ConfigLoader.getString(key + "_conditionBonus"), ConfigLoader.getInt(key + "_combo")));
                            } else {
                                System.out.println("pas combo");
                                cards.add(new Equipement(id, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getInt(key + "_position"), ConfigLoader.getInt(key + "_bonus"), ConfigLoader.getBoolean(key + "_equipementSize")));
                            }
                            
                            break;
                        case "Race":
                            cards.add(new Race(id, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getInt(key + "_bonusDamage"), ConfigLoader.getInt(key + "_bonusDice")));
                            break;
                        case "Clas":
                            cards.add(new Class(id, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description")));
                            break;
                        default:
                            System.out.println("Error in config file");
                            break;
                    }
                    id++;
            }
            i++;
        }


        shuffle(); // Shuffle the draw
        System.out.println("Stack created"); // DEBUG
    }
    /**
     * @brief Main method for testing the Stack class. Draws and prints 10 cards.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) { //DEBUG
        Stack stack = new Stack();
        System.out.println("D1");
        for (int i = 0; i < 10; i++) {
            System.out.println(stack.draw().getName());
        }
        System.out.println("D5");
    }


    /**
     * @brief Draws a card from the stack.
     * If the stack is empty, temporarily fills it with debug Monster cards.
     * @return The drawn card.
     */
    public Card draw() {
        if(cards.size() == 0) { //TODO
            for (int i = 0; i < 30; i++) { // CREATION TEMPORAIRE DE LA PILE (on peut pas récup la discard avec la conception actuelle, a voir si on change)
                cards.add(new Monster(i, "DebugCard " + i, "DebugCard " + i, 1, 1, 1, "death"));
            }
        }
        Random rand = new Random();
        int randomNumber = rand.nextInt(cards.size());
        return cards.remove(randomNumber);
    }
    /**
     * @brief Shuffles the cards in the stack.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }
}