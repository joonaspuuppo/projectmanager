package dataPht;

import java.util.Hashtable;
import java.util.ArrayList;

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
     * Lookup a specific task with the id of the task.
     * @param id -
     * @return Task
     */
    public Task getTask(int id) {
        return tasks.get(Integer.valueOf(id));
    }
    
    
    /**
     * @return Array of all tasks in the Project.
     */
    public Task[] getAllTasks() {
        return (Task[]) tasks.values().toArray();
    }
    
    
    /**
     * @param tagName Name of the Tag
     * @return Array of Tasks associated with the given Tag.
     */
    public Task[] getAllTasksByTag(String tagName) {
        ArrayList<Task> results = new ArrayList<Task>();
        for (int i = 0; i < relations.count(); i++) {
            RelationEntry re = relations.get(i);
            boolean tagNameMatches = re.getTagName().equals(tagName); 
            if (tagNameMatches) {
                Integer taskId = Integer.valueOf(re.getTaskId());
                Task task = this.tasks.get(taskId);
                results.add(task);
            }
        }
        return (Task[]) results.toArray();
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
     * @param id Task id.
     * @return Array of Tags associated with the given Task.
     */
    public Tag[] getTagsFromTask(int id) {
        ArrayList<Tag> results = new ArrayList<Tag>();
        for (int i = 0; i < relations.count(); i++) {
            RelationEntry re = relations.get(i);
            boolean taskIdMatches = re.getTaskId() == id; 
            if (taskIdMatches) {
                Tag tag = this.tags.get(re.getTagName());
                results.add(tag);
            }
        }
        return (Tag[]) results.toArray();
    }
    
    
    /**
     * @return All Tags in the Project.
     */
    public Tag[] getAllTags() {
        return (Tag[]) tags.values().toArray();
    }
    
    
    /**
     * Remove relation between the given Tag and the Task.
     * @param tagName Name of the Tag.
     * @param task Task instance.
     */
    public void removeTagFromTask(String tagName, Task task) {
        int id = task.getId();
        for (int i = 0; i < relations.count(); i++) {
            RelationEntry re = relations.get(i);
            boolean tagNameMatches = re.getTagName().equals(tagName);
            boolean taskIdMatches = re.getTaskId() == id; 
            if (tagNameMatches && taskIdMatches) {
                relations.pop(i);
                break;
            }
        }
    }
    
    
    /**
     * Create a new relation between a Tag and a Task.
     * @param tagName Name of the Tag.
     * @param t Task instance.
     */
    public void addTagToTask(String tagName, Task t) {
        RelationEntry entry = new RelationEntry(t.getId(), tagName);
        this.relations.append(entry);
    }
}
