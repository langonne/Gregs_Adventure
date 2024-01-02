package org.gregsquad.gregsadventure.card;

import java.util.Collections;
import java.util.Random;

public class Stack extends CardList{
    
    public Stack() { //TODO
        /*for (int i = 0; i < 10; i++) { // CREATION TEMPORAIRE DE LA PILE
            cards.add(new Monster(i, "DebugCard " + i, "Monster " + i, 1, 1, 1, "death"));
            cards.add(new Curse(i, "DebugCard " + i, "Curse " + i));
        }*/
        boolean finish = true;
        int i = 1;
        int id = 1;
        while(finish == true){ 
            
            cards.add(new Monster(i, "DebugCard " + i, "Monster " + i, 1, 1, 1, "death"));
            String key = Integer.toString(i);
            for (int j = 0; j < ConfigLoader.getInt(key + "_number"); j++) {
                    // Use key and value to create a card
                    switch (ConfigLoader.getString(key)) {
                        case "Curse":
                            cards.add(new Curse(id, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getString(key + "_type"), ConfigLoader.getInt(key + "_value")));
                            
                            break;
                        case "Monster":
                            cards.add(new Monster(id, ConfigLoader.getString(key + "_name"), ConfigLoader.getString(key + "_description"), ConfigLoader.getInt(key + "_level"), ConfigLoader.getInt(key + "_treasure"), ConfigLoader.getInt(key + "_damage"), ConfigLoader.getString(key + "_type")));
                            
                            break;
                        case "Object":
                            
                            break;
                    
                        default:
                            break;
                    }
                    id++;
            }
            i++;
        }


        shuffle(); // Shuffle the draw
        System.out.println("Stack created"); // DEBUG



    }



    public Card draw() {
        if(cards.size() == 0) { //TODO
            for (int i = 0; i < 30; i++) { // CREATION TEMPORAIRE DE LA PILE (on peut pas récup la discard avec la conception actuelle, a voir si on change)
                cards.add(new Monster(i, "DebugCard " + i, "DebugCard " + i, 1, 1, 1, "death"));
            }
        }
        Random rand = new Random();
        int randomNumber = rand.nextInt(cards.size());
        return cards.remove(randomNumber);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}