package org.gregsquad.gregsadventure.card;

import java.io.Serializable;
/**
 * @class Discard
 * @brief Represents a discard pile of cards in the game, derived from CardList.
 */
public class Discard extends CardList implements Serializable {
    /**
     * @brief Adds a card to the discard pile.
     * @param card The card to be added to the discard pile.
     */
    public void addCard(Card card) {
        cards.addFirst(card);
    }
}