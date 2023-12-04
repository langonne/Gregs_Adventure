package org.gregsquad.gregsadventure.card;

import java.util.Collections;

public class Stack extends CardList{
    
    public Card draw() {
        return cards.removeFirst();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}