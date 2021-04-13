package dataPht;

/**
 * A task within a project.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.6 Apr 1, 2021
 */
public class Task {
    private int id;
    private String name, info;
    private Boolean done;
    private Priority priority;
    
    /**
     * Constructor for a task. Default values used unless set by user.
     * @param id Task id given by Project instance.
     */
    public Task(int id) {
        this.id = id;
        name = "Uusi tehtävä " + id;
        info = "";
        done = false;
        priority = Priority.MEDIUM;
    }
    
    /**
     * Gets the task's ID.
     * @return id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets name of task.
     * @return name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Renames a task.
     * @param newName name
     */
    public void rename(String newName) {
        this.name = newName;
    }
    
    /**
     * Checks if a task is done.
     * @return true/false
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
     * Gets task's info.
     * @return info
     */
    public String getInfo() {
        return info;
    }
    
    /**
     * Sets task's info.
     * @param info task's info.
     */
    public void setInfo(String info) {
        this.info = info;
    }
    
    /**
     * Gets task's priority.
     * @return priority
     */
    public Priority getPriority() {
        return priority;
    }
    
    /**
     * Sets task's priority.
     * @param p priority
     */
    public void setPriority (Priority p) {
        priority = p;
    }

    
    /**
     * Toggle between done and undone.
     */
    public void toggleDone() {
        if (isDone()) {
            this.done = false;
            return;
        }
        this.done = true;
    }

}
