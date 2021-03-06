package dataPht;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.5 Mar 11, 2021
 */
public class Tag {
    private String name;
    
    /**
     * This should only be called from the Project instance.
     * This ensures that no duplicate tags are created.
     * @param name Tag name.  
     */
    public Tag(String name) {
        this.name = name;
    }
    
    /**
     * @return name
     */
    public String getName() {
        return name;
    }

}
