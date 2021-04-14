package dataPht;

/**
 * An exception thrown from all errors occuring while handling the storage.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 14, 2021
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi
 */
public class StorageException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private String info;
    
    /**
     * Constructor for StorageException.
     * @param info An error message ready to be shown to the user directly.
     */
    public StorageException(String info) {
        super();
        this.info = info;
    }

    /**
     * Gets info of a StorageException.
     * @return The information on this error.
     * This can be displayed to the user directly.
     */
    public String getInfo() {
        return info;
    }
}
