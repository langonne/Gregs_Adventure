package org.gregsquad.gregsadventure.card;

public class Equipement extends Card{
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

    public int getPosition(){
        return this.position;
    }

    public int getBonus(){
        return this.bonus;
    }

    public boolean getEquipementSize(){
        return this.equipementSize;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setBonus(int bonus){
        this.bonus = bonus;
    }


    public void setEquipementSize(boolean equipementSize){
        this.equipementSize = equipementSize;
    }

    public boolean bonusValid(String currentRace){
        if(conditionBonus == currentRace){
            return true;
        } else {
            return false;
        }
    }

    public int getCombo(){
        return this.combo;
    }

}
