package dataPht;

/**
 * @author Valtteri Rajalainen
 * @version 1.0 Apr 9, 2021
 * An exception thrown from all errors occuring while handling the storage.s 
 */
public class StorageException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private String info;
    
    /**
     * @param info An error message ready to be shown to the user directly.
     */
    public StorageException(String info) {
        super();
        this.info = info;
    }

    /**
     * @return The information on this error.
     * This can be displayed to the user directly.
     */
    public String getInfo() {
        return info;
    }
}
