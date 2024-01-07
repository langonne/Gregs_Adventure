package org.gregsquad.gregsadventure.card;
   
import java.io.Serializable;
/**
 * @class Equipement
 * @brief Represents an equipment card in the game with attributes such as position, bonus, and size.
 */
public class Equipement extends Card implements Serializable{
    private int position; //Ex : 0 = head, 1 = body, 2 = legs, 3 = feet, 4 = hand
    private int bonus;
    private boolean equipementSize; // 0 = small, 1 = big. (small = 1 hand, big = 2 hands)
    private String conditionBonus;
    private int combo;
    
    public Equipement(int id, String name, String description, int position, int bonus, boolean equipementSize){
        super(id,name,description);
        this.position = position;
        this.bonus = bonus;
        this.equipementSize = equipementSize;
    }

    public Equipement(int id, String name, String description, int position, int bonus, boolean equipementSize, String conditionBonus, int combo){
        super(id,name,description);
        this.position = position;
        this.bonus = bonus;
        this.equipementSize = equipementSize;
        this.conditionBonus = conditionBonus;
        this.combo = combo;
    }
    /**
     * @brief Gets the position of the equipement on the player.
     * @return The position of the equipement.
     */
    public int getPosition(){
        return this.position;
    }
    /**
     * @brief Gets the bonus provided by the equipement.
     * @return The bonus provided by the equipement.
     */
    public int getBonus(){
        return this.bonus;
    }
    /**
     * @brief Gets the size of the equipement (small or big).
     * @return True if the equipement is big, false if it is small.
     */
    public boolean getEquipementSize(){
        return this.equipementSize;
    }
    /**
     * @brief Sets the position of the equipement on the player.
     * @param position The position to set.
     */
    public void setPosition(int position){
        this.position = position;
    }
    /**
     * @brief Sets the bonus provided by the equipement.
     * @param bonus The bonus to set.
     */
    public void setBonus(int bonus){
        this.bonus = bonus;
    }

    /**
     * @brief Sets the size of the equipement (small or big).
     * @param equipementSize The equipement size to set.
     */
    public void setEquipementSize(boolean equipementSize){
        this.equipementSize = equipementSize;
    }
    /**
     * @brief Checks if the combo condition for the bonus is valid.
     * @param currentRace The current race of the player.
     * @return True if the combo condition is valid, false otherwise.
     */
    public boolean comboValid(String currentRace){
        if(conditionBonus == currentRace){
            return true;
        } else {
            return false;
        }
    }
    /**
     * @brief Gets the combo bonus when the condition is met.
     * @return The combo bonus.
     */
    public int getCombo(){
        return this.combo;
    }

}
