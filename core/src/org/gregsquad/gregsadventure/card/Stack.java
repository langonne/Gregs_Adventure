package org.gregsquad.gregsadventure.card;

import java.util.ArrayList;
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
        init();
    }
    /**
     * @brief Draws a card from the stack.
     * If the stack is empty recreate a Stack
     * @return The drawn card.
     */
    public Card draw() {
        if(cards.size() == 0) {
            init();
        }
        Random rand = new Random();
        int randomNumber = rand.nextInt(cards.size());
        return cards.remove(randomNumber);
    }



    public void init() {
        for (int tableId : ConfigLoader.getTableIds()) {
            
            String key = Integer.toString(tableId);
            System.out.println(key);


            for (int j = 0; j < ConfigLoader.getInt(key + "_number"); j++) {
                    // Use key and value to create a card
                    switch (ConfigLoader.getString(key)) {
                        case "Curse":
                            cards.add(new Curse(tableId, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getString(key + "_type"), ConfigLoader.getInt(key + "_value")));
                            break;
                        case "Monster":
                            cards.add(new Monster(tableId, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getInt(key + "_level"), ConfigLoader.getInt(key + "_damage"), ConfigLoader.getInt(key + "_treasure"), ConfigLoader.getString(key + "_incident")));
                            break;
                        case "Equipement":
                            if(ConfigLoader.isValid(key + "_conditionBonus") && ConfigLoader.isValid(key + "_combo")){
                                cards.add(new Equipement(tableId, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getInt(key + "_position"), ConfigLoader.getInt(key + "_bonus"), ConfigLoader.getBoolean(key + "_equipementSize"), ConfigLoader.getString(key + "_conditionBonus"), ConfigLoader.getInt(key + "_combo")));
                            } else {
                                cards.add(new Equipement(tableId, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getInt(key + "_position"), ConfigLoader.getInt(key + "_bonus"), ConfigLoader.getBoolean(key + "_equipementSize")));
                            }
                            break;
                        case "Race":
                            cards.add(new Race(tableId, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getInt(key + "_bonusDamage"), ConfigLoader.getInt(key + "_bonusDice")));
                            break;
                        case "Clas":
                            cards.add(new Class(tableId, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description")));
                            break;
                        default:
                            System.out.println("Error in config file");
                            break;
                    }
            }
        }


        shuffle(); // Shuffle the draw
    }

    /**
     * @brief Shuffles the cards in the stack.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }
}