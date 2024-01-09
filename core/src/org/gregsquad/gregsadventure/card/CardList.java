package org.gregsquad.gregsadventure.card;

import java.util.LinkedList;
import java.io.Serializable;

public abstract class CardList implements Serializable {
    protected LinkedList<Card> cards;

    public CardList() {
        this.cards = new LinkedList<Card>();
    }

    public CardList(LinkedList<Card> cards) {
        this.cards = cards;        
    }

    // Getters
    public int getSize() {
        return cards.size();
    }
    public LinkedList<Card> getCards() {
        return cards;
    }
    public Card getCard(int index) {
        return cards.get(index);
    }

    public void removeCard(int index) {
        cards.remove(index);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}