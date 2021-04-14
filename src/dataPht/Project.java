package dataPht;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling all information within a Project.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 1, 2021
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi
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
     * Gets the name of the project.
     * @return Name of the Project.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets project's name.
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Creates a blank task.
     * @return A blank task.
     */
    public Task createTask() {
        Task task = new Task(currentTaskId);
        tasks.put(Integer.valueOf(currentTaskId), task);
        currentTaskId += 1;
        return task;
    }
    
    
    /**
     * This method is used ONLY to construct the Project
     * as it is being loaded from Storage implementations.
     * @param task Task to be inserted
     */
    public void insertTask(Task task) {
        Integer taskId = Integer.valueOf(task.getId());
        if (tasks.containsKey(taskId)) throw new IllegalArgumentException();
        if (task.getId() >= currentTaskId) currentTaskId = task.getId() + 1;
        tasks.put(taskId, task);
    }
    
    
    /**
     * Creates a tag with a given name.
     * @param tagName name of tag
     * @return tag
     */
    private Tag createTag(String tagName) {
        Tag tag = new Tag(tagName);
        tags.put(tagName, tag);
        return tag;
    }
    
    
    /**
     * Checks if a tag already exists.
     * @param tagName name of tag
     * @return true/false
     */
    private boolean tagExists(String tagName) {
        return tags.containsKey(tagName);
    }
    
    
    /**
     * Removes task from tasks and relations.
     * Unused tags are removed when saving.
     * @param id task id
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
     * @param id TaskID
     * @return Task
     */
    public Task getTask(int id) {
        return tasks.get(Integer.valueOf(id));
    }
    
    
    /**
     * Gets a list of all tasks in the project.
     * @return List of all tasks in the Project.
     */
    public List<Task> getAllTasks() {
        List<Task> list = new ArrayList<Task>(tasks.values());
        return list;
    }
    
    
    /**
     * Gets a list of all tags in a project.
     * @return List of all Tags in the Project.
     */
    public List<Tag> getAllTags() {
        List<Tag> list = new ArrayList<Tag>(tags.values());
        return list;
    }
    
    
    /**
     * This should only be called from Storage implementations
     * while saving the Project.
     * @return The relations between Tags and Tasks.
     */
    public DynamicList<RelationEntry> getRelations() {
        return relations;
    }

    
    /**
     * Create a new relation between a Tag and a Task.
     * All tags are created this way.
     * 
     * TODO: prevent adding the same Tag twice
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
     * Gets all tasks associated with a given tag.
     * @param tagName Name of the Tag
     * @return List of Tasks associated with the given Tag.
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
     * Gets all tasks with a given priority.
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
     * Gets tags associated with a given task.
     * @param id Task id.
     * @return List of Tags associated with the given Task.
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
     * Unused tags are removed when saving.
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
            String parsedName = tagName.trim();
            parsedName = parsedName.replace("#", "");
            if (parsedName.equals("")) continue;
            tagsFromString.add(new Tag(parsedName));
        }
        return tagsFromString;
    }
}
