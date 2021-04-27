package dataPht;

/**
 * Tag associated with one or more tasks within a project.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.1 Apr 27, 2021
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi 
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
     * Gets name of tag.
     * @return name
     */
    public String getName() {
        return name;
    }

}
