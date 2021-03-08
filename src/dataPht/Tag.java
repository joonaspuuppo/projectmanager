package dataPht;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.5 Mar 8, 2021
 */
public class Tag {
    private String name;
    
    /**
     * @param name Tag name. This must be unique.
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
