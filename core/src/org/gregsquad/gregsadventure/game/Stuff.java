package org.gregsquad.gregsadventure.game;
import java.util.LinkedList;

import org.gregsquad.gregsadventure.card.Object;

public class Stuff {
    protected int size;
    protected LinkedList<Object> ObjectList;

    public Stuff() {
        this.size = 0;
        this.ObjectList = new LinkedList<Object>();
    }

    public Stuff(LinkedList<Object> ObjectList) {
        this.size = ObjectList.size();
        this.ObjectList = ObjectList;        
    }

    public LinkedList<Card> getObjects() {
        return ObjectList;
    }

    public void addObject(Object object) {
        ObjectList.add(object);
    }
    public void removeObject(Object object) {
        ObjectList.remove(object);
    }
}
