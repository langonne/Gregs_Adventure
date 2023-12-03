package org.gregsquad.gregsadventure.card;

import java.util.ArrayList;

public class Deck extends CardList{
    
    Deck(){
        this.size = 0;
        this.cards = new ArrayList<Card>();
    }

    Deck(ArrayList<Card> cards){
        this.size = cards.size();
        this.cards = cards;
    }

    public void addCard(Card card){
        this.cards.add(card);
        this.size++;
    }

    public void removeCard(Card card){
        this.cards.remove(card);
        this.size--;
    }
}
