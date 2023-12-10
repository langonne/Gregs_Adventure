package org.gregsquad.gregsadventure.game;

import java.util.LinkedList;
import org.gregsquad.gregsadventure.card.Equipement;

public class Stuff {
    protected int size;
    protected LinkedList<Equipement> Equipements;

    public Stuff() {
        this.size = 0;
        this.Equipements = new LinkedList<Equipement>();
    }

    public Stuff(LinkedList<Equipement> Equipements) {
        this.size = Equipements.size();
        this.Equipements = Equipements;
    }

    public LinkedList<Equipement> getEquipements() {
        return Equipements;
    }

    public void addEquipement(Equipement equipement) {
        Equipements.add(equipement);
    }
    public void removeEquipement(int number) { // Can be adapt to a type of equipement
        Equipements.remove(number);
    }

    public void removeEquipement(Equipement equipement) {
        Equipements.remove(equipement);
    }

    public void clearStuff() {
        Equipements.clear();
    }

    public int getSize() {
        return size;
    }
}
