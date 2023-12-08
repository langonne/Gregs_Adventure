package org.gregsquad.gregsadventure.card;

import java.util.Random;
import org.gregsquad.gregsadventure.game.Player;
import org.gregsquad.gregsadventure.game.Stuff;
import org.gregsquad.gregsadventure.card.*;

public class Curse extends Card{
    private String type;
    private int value;

    public Curse(int id, String name, String description){
        super(id, name, description);
    }

    public void curse(Player player){
        if(type == "level"){
            player.addLevel(value);
        }
        if(type == "damage"){
            player.addDamage(value);
        }
        if(type == "object"){
            Random rand = new Random();
            player.getStuff().removeObject(rand.nextInt(player.getStuff().getSize()));
        }
    }
}
