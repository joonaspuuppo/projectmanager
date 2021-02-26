package dataPht;

/**
 * @author Joonas Puuppo
 * @version Feb 22, 2021
 *
 */
public class Tag {
    private int id;
    private String name;
    
    /**
     * @param id Tag id given by Project instance.
     * @param name Tag name. This must be unique.
     */
    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * @return id
     */
    public int getId() {
        return id;
    }
    
    /**
     * @return name
     */
    public String getName() {
        return name;
    }

}
