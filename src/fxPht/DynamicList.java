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
        data[count] = item;
        count += 1;
        if (expansionNeeded()) {
            expand();
        }
    }
    
    
    /**
     * @param index Index to access the item. If index is negative,
     * count is started from the end of the list. 
     * For example index "-1" refers to the last item of the list.
     * @return The item in the index.
     * @throws IndexOutOfBoundsException -
     */
    public T get(int index) throws IndexOutOfBoundsException{
        if (index < 0) {
            return getByNegativeIndex(index);
        }
        if (index > count - 1) {
            throw new IndexOutOfBoundsException();
        }
        return data[index];
    }
    
    
    /**
     * @param index --
     * @return --
     * @throws IndexOutOfBoundsException -
     */
    public T pop(int index) throws IndexOutOfBoundsException{
        T item = data[index];
        if (shrinkNeeded()) {
            shrink(index);
        } else {
            repackItems(index);
        }
        count -= 1;
        return item;
    }
    
    
    /**
     * @param item --
     */
    public void remove(T item) {
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
    
    
    private boolean expansionNeeded() {
        return (double)count / (double)size > EXPAND_TRESHOLD;
    }
    
    
    private boolean shrinkNeeded() {
        boolean result = false;
        if ((double)count / (double)size < SHRINK_TRESHOLD && size > START_SIZE) {
            result = true;
        }
        return result;
    }
    
    
    @SuppressWarnings("unchecked")
    private void expand() {
        int newSize = size * 2;
        T[] temp = (T[])new Object[newSize];
        for (int i = 0; i < count; i++) {
            temp[i] = data[i];
        }
        size = newSize;
        data = temp;
    }
    
    
    @SuppressWarnings("unchecked")
    private void shrink(int index) {
        int newSize = size / 2;
        T[] temp = (T[])new Object[newSize];
        for (int i = 0; i < index; i++) {
            temp[i] = data[i];
        }
        for (int j = index; j < count; j++) {
            temp[j] = data[j+1];
        }
        size = newSize;
        data = temp;
    }
    
    
    private void repackItems(int index) {
        for (int i = index; i < count; i++) {
            data[i] = data[i+1];
        }
    }
    
    
    private void copyItems(T[] items) {
        for (int i = 0; i < items.length; i++) {
            data[i] = items[i];
        }
    }
    
    
    private T getByNegativeIndex(int index) throws IndexOutOfBoundsException {
        if (Math.abs(index) > count) {
            throw new IndexOutOfBoundsException();
        }
        return data[count + index];
    }
}
