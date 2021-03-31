package dataPht;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.6 Apr 1, 2021
 * @param <T> Type of objects in the list
 */
public class DynamicList<T> {
    
    private T[] data;
    private int count;
    private int size;
    
    private final int START_SIZE = 16;
    private final double EXPAND_THRESHOLD = 0.75;
    private final double SHRINK_THRESHOLD = 0.125;
    
    /**
     * Create an empty  list.
     * 
     * NOTE: Internal array is not typecheked.
     * It should never be exposed to the user.
     */
    @SuppressWarnings("unchecked")
    public DynamicList() {
        this.count = 0;
        this.size = START_SIZE;
        this.data = (T[])new Object[START_SIZE];
    }
    
    
    /**
     * Create list with items inside a give array.
     * @param items Array of refrences to be copied to the created list.
     * 
     * NOTE: Internal array is not typecheked.
     * It should never be exposed to the user.
     */
    @SuppressWarnings("unchecked")
    public DynamicList(T[] items) {
        this.count = items.length;
        this.size = Math.max(START_SIZE, items.length*2);
        this.data = (T[])new Object[this.size];
        copyItemsFrom(items);
    }


    /**
     * @return Number of items stored currently in the list. 
     */
    public int count() {
        return count;
    }
    
    
    /**
     * @param item Refrence to be appended to the list.
     */
    public void append(T item) {
        data[count] = item;
        count += 1;
        if (expansionNeeded()) {
            expand();
        }
    }
    
    
    /**
     * Return an item at the given position in the list.
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
     * Remove and return an item in the list in the given index.
     * @param index Index of the item.
     * @return The item in the given index.
     * @throws IndexOutOfBoundsException -
     */
    public T pop(int index) throws IndexOutOfBoundsException{
        if (index < 0) return popWithNegativeIndex(index);
        T item = this.get(index);
        if (shrinkNeeded()) {
            shrink(index);
        } else {
            repackItems(index);
        }
        count -= 1;
        return item;
    }
    
    
    /**
     * Remove first instance of the item from the list.
     * Comparison is done by using the '==' operator. 
     * @param item An item to be removed from the list.
     */
    public void remove(T item) {
        for (int i = 0; i < this.count(); i++) {
            if (this.get(i) == item) {
                if (shrinkNeeded()) {
                    shrink(i);
                } else {
                    repackItems(i);
                }
                count -= 1;
                return;
            }
        }
    }
    
    
    @Override public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < count; i++) {
            sb.append(this.get(i).toString());
            if (i+1 < count) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    
    private boolean expansionNeeded() {
        return (double)count / (double)size > EXPAND_THRESHOLD;
    }
    
    
    private boolean shrinkNeeded() {
        boolean result = false;
        if ((double)count / (double)size < SHRINK_THRESHOLD && size > START_SIZE) {
            result = true;
        }
        return result;
    }
    
    
    /**
     * Copy items from the current array to 
     * a new array with twice the size.
     */
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
    
    
    /**
     * Shrink the internal array to a new array of
     * half the size and copy the existing items. 
     * This is called exclusively from deleting
     * operations (pop, remove), so the internal array is copied
     * in such a way that there are no gaps left.
     * @param index Index where the removal occured.
     */
    @SuppressWarnings("unchecked")
    private void shrink(int index) {
        int newSize = Math.max(START_SIZE, size / 2);
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
    
    
    /**
     * Remove the gap from the internal array after deleting operation.
     * @param index Index where the removal occured.
     */
    private void repackItems(int index) {
        for (int i = index; i < count; i++) {
            data[i] = data[i+1];
        }
    }
    
    
    private void copyItemsFrom(T[] items) {
        for (int i = 0; i < items.length; i++) {
            data[i] = items[i];
        }
    }
    
    
    private T popWithNegativeIndex(int index) {
        if (Math.abs(index) > count) {
            throw new IndexOutOfBoundsException();
        }
        return this.pop(count + index);
    }
    
    
    private T getByNegativeIndex(int index) throws IndexOutOfBoundsException {
        if (Math.abs(index) > count) {
            throw new IndexOutOfBoundsException();
        }
        return this.get(count + index);
    }
}
