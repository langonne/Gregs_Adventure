package org.gregsquad.gregsadventure.card;

public class Equipement extends Card{
    protected int position; //Ex : 0 = head, 1 = body, 2 = legs, 3 = feet, 4 = left hand, 5 = right hand.
    protected int bonus;
    protected Bool equipementSize; // 0 = small, 1 = big. (small = 1 hand, big = 2 hands)
    
    public Equipement(String name, int id, String description, int position, int bonus, Bool equipementSize){
        super(id,name,description);
        this.position = position;
        this.bonus = bonus;
        this.equipementSize = equipementSize;
    }

    public int getPosition(){
        return this.position;
    }

    public int getBonus(){
        return this.bonus;
    }

    public Bool getEquipementSize(){
        return this.equipementSize;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setBonus(int bonus){
        this.bonus = bonus;
    }

    public void setEquipementSize(Bool equipementSize){
        this.equipementSize = equipementSize;
    }


}
