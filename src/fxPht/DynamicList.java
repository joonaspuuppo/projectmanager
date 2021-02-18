package fxPht;

/**
 * @author varajala
 * @version Feb 18, 2021
 * @param <T> Type of objects in the list
 */
public class DynamicList<T> {
    
    private T[] data;
    private int count;
    private int size;
    
    private final int START_SIZE = 16;
    private final double EXPAND_TRESHOLD = 0.75;
    private final double SHRINK_TRESHOLD = 0.125;
    
    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public DynamicList() {
        this.count = 0;
        this.size = START_SIZE;
        this.data = (T[])new Object[START_SIZE];
    }
    
    
    /**
     * @param items --
     */
    @SuppressWarnings("unchecked")
    public DynamicList(T[] items) {
        this.count = items.length;
        this.size = Math.max(START_SIZE, items.length*2);
        this.data = (T[])new Object[this.size];
        copyItems(items);
    }


    /**
     * @return Number of items stored currently in the list. 
     */
    public int count() {
        return count;
    }
    
    
    /**
     * @param item Item to be appended to the list.
     */
    public void append(T item) {
        //
    }
    
    
    /**
     * @param index --
     * @return --
     */
    public T get(int index) {
        return null;
    }
    
    
    /**
     * @param index --
     * @return --
     */
    public T pop(int index) {
        return null;
    }
    
    
    /**
     * @param item --
     * @param index --
     */
    public void insert(T item, int index) {
        //
    }
    
    
    @Override public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < count; i++) {
            sb.append(data[i].toString());
            if (i+1 < count) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    
    @SuppressWarnings("unused")
    private boolean expansionNeeded() {
        return false;
    }
    
    
    @SuppressWarnings("unused")
    private boolean shrinkNeeded() {
        return false;
    }
    
    
    @SuppressWarnings("unused")
    private void expand() {
        //
    }
    
    
    @SuppressWarnings("unused")
    private void shrink() {
        //
    }
    
    
    private void copyItems(T[] items) {
        for (int i = 0; i < items.length; i++) {
            data[i] = items[i];
        }
    }
}
