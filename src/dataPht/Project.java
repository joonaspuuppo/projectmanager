package dataPht;

import java.util.Hashtable;

/**
 * @author varajala
 * @version Feb 19, 2021
 */
public class Project {
    
    private int currentTaskId;
    private String name;
    private DynamicList<RelationEntry> relations;
    private Hashtable<Integer, Task> tasks;
    private Hashtable<String, Tag> tags;
   
    
    
    /**
     * Create new blank project.
     * 
     * This should be called only from:
     * - Storage implementations
     * - ProjectManager
     * 
     * @param name Name of the project.
     */
    public Project(String name) {
        this.tasks = new Hashtable<Integer, Task>();
        this.tags = new Hashtable<String, Tag>();
        this.relations = new DynamicList<RelationEntry>();
        this.currentTaskId = 1;
        this.name = name;
    }
    
    
    /**
     * @return Name of the Project.
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * @param name -
     */
    public void createTask(String name) {
        // TODO
    }
    
    
    /**
     * @param id -
     */
    public void removeTask(int id) {
        // TODO
    }
    
    
    /**
     * @param id -
     */
    public void getTask(int id) {
        // TODO
    }
    
    
    /**
     * @return -
     */
    public Task[] getAllTasks() {
        return null;
        // TODO
    }
    
    
    /**
     * @param tagName - 
     * @return -
     */
    public Task[] getAllTasksByTag(String tagName) {
        return null;
        // TODO
    }
    
    
    /**
     * @param prio - 
     * @return -
     */
    public Task[] getAllTasksByPriority(Priority prio) {
        return null;
        // TODO
    }
    
    
    /**
     * @param id -
     * @return -
     */
    public Tag[] getTagsFromTask(int id) {
        return null;
        // TODO
    }
    
    
    /**
     * @return -
     */
    public Tag[] getAllTags() {
        return null;
        // TODO
    }
    
    
    /**
     * @param tagName - 
     * @param task -
     */
    public void removeTagFromTask(String tagName, Task task) {
        // TODO
    }
    
    
    /**
     * @param tagName - 
     * @param t -
     * @return -
     * 
     */
    public Task[] addTagToTask(String tagName, Task t) {
        return null;
        // TODO
    }
}
