package micelaneos;

public class NodoList <T>{
    private NodoList pNext;
    private T value;

    public NodoList(T value) {
        this.pNext = null;
        this.value = value;
    }

    public NodoList getpNext() {
        return pNext;
    }

    public void setpNext(NodoList pNext) {
        this.pNext = pNext;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T  value) {
        this.value = value;
    }
}
