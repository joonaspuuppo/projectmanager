package dataPht;

/**
 * @author Joonas Puuppo
 * @version Feb 22, 2021
 *
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
