package org.gregsquad.gregsadventure.card;

import java.util.ArrayList;

public abstract class CardList {
    protected int size;
    protected ArrayList<Card> cards = new ArrayList<>();

    public CardList() {
        this.cards = new ArrayList<Card>();
        this.size = 0;
    }

    public CardList(ArrayList<Card> cards) {
        this.cards = cards;
        this.size = cards.size();
    }

    // Getters
    public int getSize() {
        return size;
    }
    public ArrayList<Card> getCards() {
        return cards;
    }
    public Card drawCard() {
        if (size > 0) {
            Card card = cards.get(0);
            cards.remove(0);
            size--;
            return card;
        }
        return null;
    }

    // Setters
    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
            size++;
        }
    }

