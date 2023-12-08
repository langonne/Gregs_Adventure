package org.gregsquad.gregsadventure.game;

import java.util.LinkedList;
import org.gregsquad.gregsadventure.card.Equipement;

public class Stuff {
    protected int size;
    protected LinkedList<Equipement> ObjectList;

    public Stuff() {
        this.size = 0;
        this.ObjectList = new LinkedList<Equipement>();
    }

    public Stuff(LinkedList<Equipement> ObjectList) {
        this.size = ObjectList.size();
        this.ObjectList = ObjectList;        
    }

    public LinkedList<Equipement> getObjects() {
        return ObjectList;
    }

    public void addObject(Equipement object) {
        ObjectList.add(object);
    }
    public void removeObject(int number) { // Can be adapt to a type of object
        ObjectList.remove(number);
    }

    public int getSize() {
        return size;
    }
}
