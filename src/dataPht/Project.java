package dataPht;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.5 Mar 8, 2021
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
     * @return A blank task.
     */
    public Task createTask() {
        Task task = new Task(currentTaskId);
        tasks.put(Integer.valueOf(currentTaskId), task);
        currentTaskId += 1;
        return task;
    }
    
    
    private Tag createTag(String tagName) {
        Tag tag = new Tag(tagName);
        tags.put(tagName, tag);
        return tag;
    }
    
    
    private boolean tagExists(String tagName) {
        return tags.containsKey(tagName);
    }
    
    
    /**
     * Removes task from tasks and relations.
     * @param id task id
     * TODO: should unused tags be removed from this.tags?
     */
    public void removeTask(int id) {
        if (this.tasks.containsKey(id)) {
            while (!this.getTagsFromTask(id).isEmpty()) {
                this.removeTagFromTask(this.getTagsFromTask(id).get(0).getName(), this.getTask(id));
            }
            this.tasks.remove(id);
        }
        
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
     * TODO: prevent adding the same Tag twice?
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
     * @param prio priority
     * @return a list containing all tasks with specified priority
     */
    public List<Task> getAllTasksByPriority(Priority prio) {
        List <Task> results = new ArrayList<Task>();
        for (Task task : tasks.values()) {
            if (task.getPriority() == prio) {
                results.add(task);
            }
        }
        return results;
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
     * TODO: should unused tags be removed from this.tags?
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
     * Creates a string of tagnames from given list of Tags.
     * @param tagList list of tags
     * @return comma-separated string of tagNames
     */
    public String getTagsAsString(List<Tag> tagList) {
        if (tagList.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tagList) {
            sb.append(tag.getName() + ", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        
        return sb.toString();
    }
    
    /**
     * Reads tags from a comma-separated string of tagNames and returns them as a list of Tags
     * @param tagString string of tagNames
     * @return List of tags
     */
    public List<Tag> readTagsFromString(String tagString) {
        String[] tagNames = tagString.split(",");
        ArrayList<Tag> tagsFromString = new ArrayList<>();
        for (String tagName : tagNames) {
            tagsFromString.add(new Tag(tagName.trim()));
        }
        return tagsFromString;
    }
}
