package org.gregsquad.gregsadventure.card;

import java.util.Collections;
import java.util.Random;

public class Stack extends CardList{
    
    public void creation() {
        for (int i = 0; i < 3; i++) {
            cards.add(new Monster(i, "DebugCard " + i, "DebugCard " + i, 1, 1, 1, "death"));
        }
    }


    public Card draw() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(cards.size());
        return cards.removeFirst();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}