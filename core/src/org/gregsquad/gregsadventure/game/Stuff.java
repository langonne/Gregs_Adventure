package org.gregsquad.gregsadventure.game;

import java.util.LinkedList;
import org.gregsquad.gregsadventure.card.Equipement;

public class Stuff {
    protected int size;
    protected LinkedList<Equipement> equipements;

    public Stuff() {
        this.size = 0;
        this.equipements = new LinkedList<Equipement>();
    }

    public Stuff(LinkedList<Equipement> Equipements) {
        this.size = Equipements.size();
        this.equipements = Equipements;
    }

    public LinkedList<Equipement> getEquipements() {
        return equipements;
    }

    public void addEquipement(Equipement equipement) {
        equipements.add(equipement);
    }

    public void removeEquipement(Equipement equipement) {
        equipements.remove(equipement);
    }

    public void clearStuff() {
        equipements.clear();
    }

    public int getSize() {
        return size;
    }
}
