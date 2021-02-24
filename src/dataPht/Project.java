package dataPht;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author varajala
 * @version Feb 19, 2021
 */
public class Project {
    
    private int currentTaskId;
    private int currentTagId;
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
     * @return A blank task.
     */
    public Task createTask() {
        Task task = new Task(currentTaskId);
        tasks.put(Integer.valueOf(currentTaskId), task);
        currentTaskId += 1;
        return task;
    }
    
    
    private Tag createTag(String tagName) {
        Tag tag = new Tag(currentTagId, tagName);
        tags.put(tagName, tag);
        currentTagId += 1;
        return tag;
    }
    
    
    private boolean tagExists(String tagName) {
        return tags.containsKey(tagName);
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
    public List<Task> getAllTasks() {
        List<Task> list = new ArrayList<Task>(tasks.values());
        return list;
    }
    
    
    /**
     * @return All Tags in the Project.
     */
    public List<Tag> getAllTags() {
        List<Tag> list = new ArrayList<Tag>(tags.values());
        return list;
    }

    
    /**
     * Create a new relation between a Tag and a Task.
     * All tags are created this way.
     * 
     * @param tagName Name of the Tag.
     * @param t Task instance.
     */
    public void addTagToTask(String tagName, Task t) {
        if (!tagExists(tagName)) {
            createTag(tagName);
        }
        RelationEntry entry = new RelationEntry(t.getId(), tagName);
        this.relations.append(entry);
    }
    
    
    /**
     * @param tagName Name of the Tag
     * @return Array of Tasks associated with the given Tag.
     */
    public List<Task> getAllTasksByTag(String tagName) {
        List<Task> results = new ArrayList<Task>();
        for (int i = 0; i < relations.count(); i++) {
            RelationEntry re = relations.get(i);
            boolean tagNameMatches = re.getTagName().equals(tagName); 
            if (tagNameMatches) {
                Integer taskId = Integer.valueOf(re.getTaskId());
                Task task = this.tasks.get(taskId);
                results.add(task);
            }
        }
        return results;
    }
    
    
    /**
     * @param prio - 
     * @return -
     */
    public List<Task> getAllTasksByPriority(Priority prio) {
        return null;
        // TODO
    }
    
    
    /**
     * @param id Task id.
     * @return Array of Tags associated with the given Task.
     */
    public List<Tag> getTagsFromTask(int id) {
        List<Tag> results = new ArrayList<Tag>();
        for (int i = 0; i < relations.count(); i++) {
            RelationEntry re = relations.get(i);
            boolean taskIdMatches = re.getTaskId() == id; 
            if (taskIdMatches) {
                Tag tag = this.tags.get(re.getTagName());
                results.add(tag);
            }
        }
        return results;
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
}
