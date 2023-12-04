package org.gregsquad.gregsadventure;

import java.util.ArrayList;

public class Objects {

    private ArrayList<Object> ObjectList;
    

    public Objects() {
        this.ObjectList = new ArrayList<Object>();
    }

    public ArrayList<Object> getObjectList() {
        return ObjectList;
    }

    public void addObject(Object object) {
        ObjectList.add(object);
    }

    public void removeObject(Object object) {
        ObjectList.remove(object);
    }
    
}
