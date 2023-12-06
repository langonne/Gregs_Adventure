
import org.gregsquad.gregsadventure.object;

public class Objects {
    protected int size;
    protected LinkedList<Object> ObjectList;

    public Objects() {
        this.size = 0;
        this.ObjectList = new LinkedList<Object>();
    }

    public Objects(LinkedList<Object> ObjectList) {
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
