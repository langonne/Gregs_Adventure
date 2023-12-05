package org.gregsquad.gregsadventure;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cardsInDeck;
    private ArrayList<Card> cardsInDiscard;

    public Deck() {
        this.cardsInDeck = new ArrayList<Card>();
        this.cardsInDiscard = new ArrayList<Card>();
    }

    public void shuffle() {
        for(int i = 0; i < cardsInDeck.size(); i++){
            int randomIndex = (int) (Math.random() * cardsInDeck.size());
            Card temp = cardsInDeck.get(i);
            cardsInDeck.set(i, cardsInDeck.get(randomIndex));
            cardsInDeck.set(randomIndex, temp);
        }
    }

    public ArrayList<Card> drawCards(int numberOfCards) {
        ArrayList<Card> cardsDraw = new ArrayList<Card>();
        for(int i = 0; i < numberOfCards; i++){
            cardsDraw.add(cardsInDeck.get(0));
            cardsInDeck.remove(0);
        }
        return cardsInDeck;
    }

    public void addCardInDiscard(Card card){
        cardsInDiscard.add(card);
    }
    public void creationDeck() {
        // TODO implement here
    }

    public void discardRecovery() {
        for(int i = 0; i < cardsInDiscard.size(); i++){
            cardsInDeck.add(cardsInDiscard.get(i));
        }
        shuffle();
    }

}
