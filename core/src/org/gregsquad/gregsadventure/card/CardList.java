package org.gregsquad.gregsadventure.card;

import java.util.LinkedList;

public abstract class CardList {
    protected int size;
    protected LinkedList<Card> cards = new LinkedList<>();

    public CardList() {
        this.cards = new LinkedList<Card>();
        this.size = 0;
    }

    public CardList(LinkedList<Card> cards) {
        this.cards = cards;
        this.size = cards.size();
    }

    // Getters
    public int getSize() {
        return size;
    }
    public LinkedList<Card> getCards() {
        return cards;
    }
}