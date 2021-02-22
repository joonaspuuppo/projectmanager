package dataPht;

/**
 * @author Joonas Puuppo
 * @version Feb 22, 2021
 *
 */
public class Task {
    private int id;
    private String name, info;
    private Boolean done;
    private enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
    private Priority priority;
    
    // TODO: Konstruktori
    
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
    
    /**
     * @param newName name
     */
    public void rename(String newName) {
        // TODO: pitääkö nimen olla uniikki?
        this.name = newName;
    }
    
    /**
     * @return done
     */
    public Boolean isDone() {
        return done;
    }
    
    /**
     * Marks task as done.
     */
    public void markAsDone() {
        done = true;
    }
    
    /**
     * Marks task as incomplete.
     */
    public void markAsIncomplete() {
        done = false; 
    }
    
    /**
     * @return info
     */
    public String getInfo() {
        return info;
    }
    
    /**
     * @param info info
     */
    public void setInfo(String info) {
        this.info = info;
    }
    
    /**
     * @return priority
     */
    public Priority getPriority() {
        return priority;
    }
    
    /**
     * @param p priority
     */
    public void setPriority (Priority p) {
        priority = p;
    }

}