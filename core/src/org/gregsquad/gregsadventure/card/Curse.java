package org.gregsquad.gregsadventure.card;

import java.util.Random;
import org.gregsquad.gregsadventure.game.Player;
import java.io.Serializable;

/**
 * @class Curse
 * @brief Represents a curse card in the game with attributes such as type and value.
 */
public class Curse extends Card implements Serializable{
    private String type;
    private int value;
    /**
     * @brief Applies the curse to the specified player.
     * @param player The player to curse.
     */
    public Curse(int id, String name, String description, String type, int value){
        super(id, name, description);
        this.type = type;
        this.value = value;
    }
    /**
     * @brief Applies the curse to the specified player.
     * @param player The player to curse.
     */
    public void curse(Player player){
        if(type == "level"){
            player.addLevel(value);
        }
        if(type == "damage"){
            player.addDamage(value);
        }
        if(type == "equipement"){
            Random rand = new Random();
            player.getStuff().removeEquipement(rand.nextInt(player.getStuff().getSize()));
        }
    }
    /**
     * @brief Plays the curse card.
     * Displays a message and prompts the player to choose a curse.
     */
    public final void play() {
        System.out.println("Playing curse " + this.name);
        //Interface choix joueur
        Player playerSelected = null;
        //curse(playerSelected);
        }

}
