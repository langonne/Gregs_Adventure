package org.gregsquad.gregsadventure.card;

import java.util.ArrayList;

public class Deck{

    protected int size;
    protected ArrayList<Card> cards;
    
    Deck(){
        this.size = 0;
        this.cards = new ArrayList<Card>();
    }

    Deck(ArrayList<Card> cards){
        this.size = cards.size();
        this.cards = cards;
    }

    public int getSize(){
        return this.size;
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }

    public Card getCard(int index){
        return this.cards.get(index);
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
