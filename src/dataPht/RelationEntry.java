package dataPht;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.6 Apr 1, 2021
 */
public class RelationEntry {
    private int taskId;
    private String tagName;
    
    
    /**
     * @param taskId Id of the task.
     * @param tagName Name of the tag.
     */
    public RelationEntry(int taskId, String tagName) {
        this.taskId = taskId;
        this.tagName = tagName;
    }


    
    /**
     * @return Tag name
     */
    public String getTagName() {
        return tagName;
    }


    
    /**
     * @return taskID
     */
    public int getTaskId() {
        return taskId;
    }
}
