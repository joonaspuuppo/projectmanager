package dataPht;

/**
 * Class for forming relations between Tasks and Tags.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 1, 2021
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi 
 */
public class RelationEntry {
    private int taskId;
    private String tagName;
    
    
    /**
     * Constructor for a relation entry.
     * @param taskId Id of the task.
     * @param tagName Name of the tag.
     */
    public RelationEntry(int taskId, String tagName) {
        this.taskId = taskId;
        this.tagName = tagName;
    }


    
    /**
     * Gets name of tag.
     * @return Tag name
     */
    public String getTagName() {
        return tagName;
    }


    
    /**
     * Gets taskID.
     * @return taskID
     */
    public int getTaskId() {
        return taskId;
    }
}
