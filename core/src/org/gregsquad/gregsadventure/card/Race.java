package org.gregsquad.gregsadventure.card;

/**
 * @class Race
 * @brief Represents a race card in the game with attributes such as bonus damage and bonus dice.
 */
public class Race extends Card {
    private int bonusDamage;
    private int bonusDice;
    /**
     * @brief Gets the bonus damage provided by the race.
     * @return The bonus damage provided by the race.
     */
    public Race(int id, String name, String description, int bonusDamage, int bonusDice){
        super(id, name, description);
        this.bonusDamage = bonusDamage;
        this.bonusDice = bonusDice;
    }
    /**
     * @brief Gets the bonus damage provided by the race.
     * @return The bonus damage provided by the race.
     */
    public int getBonusDamage(){
        return this.bonusDamage;
    }
    /**
     * @brief Gets the bonus dice provided by the race.
     * @return The bonus dice provided by the race.
     */
    public int getBonusDice(){
        return this.bonusDice;
    }
}
