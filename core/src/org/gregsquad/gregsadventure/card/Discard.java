package org.gregsquad.gregsadventure.card;

import java.io.Serializable;

public class Discard extends CardList implements Serializable {
        
    public void addCard(Card card) {
        cards.addFirst(card);
    }
}