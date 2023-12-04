package org.gregsquad.gregsadventure.card;

import java.util.LinkedList;

public abstract class CardList {
    protected int size;
    protected LinkedList<Card> cards;

    public CardList() {
        this.size = 0;
        this.cards = new LinkedList<Card>();
    }

    public CardList(LinkedList<Card> cards) {
        this.size = cards.size();
        this.cards = cards;        
    }

    // Getters
    public int getSize() {
        return size;
    }
    public LinkedList<Card> getCards() {
        return cards;
    }
    public Card getCard(int index) {
        return cards.get(index);
    }
}