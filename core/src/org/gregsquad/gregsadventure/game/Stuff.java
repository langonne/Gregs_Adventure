package org.gregsquad.gregsadventure.game;

import java.util.LinkedList;
import org.gregsquad.gregsadventure.card.Equipement;
import java.io.Serializable;
/**
 * @class Stuff
 * @brief Represents the equipment collection of a player in the game.
 */
public class Stuff implements Serializable {
    protected int size;
    protected LinkedList<Equipement> equipements;
    /**
     * @brief Default constructor for Stuff class.
     * Initializes the size to 0 and creates an empty list of equipements.
     */
    public Stuff() {
        this.size = 0;
        this.equipements = new LinkedList<Equipement>();
    }

    /**
     * @brief Constructor for Stuff class with an initial list of equipements.
     * @param Equipements The initial list of equipements.
     */
    public Stuff(LinkedList<Equipement> Equipements) {
        this.size = Equipements.size();
        this.equipements = Equipements;
    }
    /**
     * @brief Gets the list of equipements.
     * @return The list of equipements.
     */
    public LinkedList<Equipement> getEquipements() {
        return equipements;
    }

    /**
     * @brief Adds an equipement to the collection.
     * @param equipement The equipement to add.
     */
    public void addEquipement(Equipement equipement) {
        equipements.add(equipement);
    }

    /**
     * @brief Removes a specified equipement from the collection.
     * @param equipement The equipement to remove.
     */
    public void removeEquipement(Equipement equipement) {
        equipements.remove(equipement);
    }

    /**
     * @brief Removes an equipement at the specified index.
     * Used for the loss of a random equipement.
     * @param index The index of the equipement to remove.
     */
    public void removeEquipement(int index) { // Pour la perte d'un equipement random
        equipements.remove(index);
    }
    /**
     * @brief Clears the entire collection of equipements.
     */
    public void clearStuff() {
        equipements.clear();
    }
    /**
     * @brief Gets the current size of the collection.
     * @return The current size of the collection.
     */
    public int getSize() {
        return size;
    }
}
