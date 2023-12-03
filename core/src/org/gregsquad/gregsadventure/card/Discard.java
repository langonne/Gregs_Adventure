package org.gregsquad.gregsadventure.card;

public class Discard extends CardList{
        
    public void add(Card card) {
        cards.addFirst(card);
    }
}