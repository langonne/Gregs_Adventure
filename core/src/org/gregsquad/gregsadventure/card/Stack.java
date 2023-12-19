package org.gregsquad.gregsadventure.card;

import java.util.Collections;
import java.util.Random;

public class Stack extends CardList{
    
    public Stack() { //TODO
        for (int i = 0; i < 10; i++) { // CREATION TEMPORAIRE DE LA PILE
            cards.add(new Monster(i, "DebugCard " + i, "Monster " + i, 1, 1, 1, "death"));
            cards.add(new Curse(i, "DebugCard " + i, "Curse " + i));
        }
        System.out.println("Stack created"); // DEBUG

    }


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

    public void shuffle() {
        Collections.shuffle(cards);
    }
}