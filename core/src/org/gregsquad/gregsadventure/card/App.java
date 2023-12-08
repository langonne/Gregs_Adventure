package org.gregsquad.gregsadventure.card;

import org.gregsquad.gregsadventure.game.Player;

public class App {
    
    public static void main(String[] args) {
        // Create an instance of Equipement
        Equipement equipement = new Equipement("Equipement1", 1, "This is a test equipement", 0, 10, true);
        // Print out the properties of the created equipement
        System.out.println("Name: " + equipement.getName());
        System.out.println("ID: " + equipement.getId());
        System.out.println("Description: " + equipement.getDescription());
        System.out.println("Position: " + equipement.getPosition());
        System.out.println("Bonus: " + equipement.getBonus());
        System.out.println("Equipement Size: " + (equipement.getEquipementSize() ? "Large" : "Small"));
    }
    


    

}