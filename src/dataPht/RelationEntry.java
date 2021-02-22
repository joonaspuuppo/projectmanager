package dataPht;

/**
 * @author varajala
 * @version Feb 22, 2021
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


    @SuppressWarnings("javadoc")
    public String getTagName() {
        return tagName;
    }


    @SuppressWarnings("javadoc")
    public int getTaskId() {
        return taskId;
    }
}
