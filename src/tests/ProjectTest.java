package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import dataPht.Project;
import dataPht.Task;


/**
 * @author varajala
 * @version Feb 19, 2021
 */
public class ProjectTest {
    
    private final String PROJECT_NAME = "TEST";
    private final String TAG1 = "tag1";
    private final String TAG2 = "tag2";
    
    
    private Project createTestProject() {
        return new Project(PROJECT_NAME);
    }
    
    
    /**
     * Test creation of Tasks and getting of Tasks.
     */
    @Test
    public void testTaskCreation() {
        Project p = createTestProject();
        
        assertTrue(p.getAllTasks().size() == 0);
        
        Task t1 = p.createTask();
        Task t2 = p.createTask();
        Task t3 = p.createTask();
        
        List<Task> taskList = p.getAllTasks();
        assertTrue(taskList.size() == 3);
        
        assertEquals(p.getTask(1), t1);
        assertEquals(p.getTask(2), t2);
        assertEquals(p.getTask(3), t3);
    }
    
    
    /**
     * Test creation of Tags and getting of Tags.
     * 
     * Tags are created by the Project instance internally
     * when a new tag is associated with a task.
     */
    @Test
    public void testTagCreation() {
        Project p = createTestProject();
        Task task = p.createTask();

        p.addTagToTask(TAG1, task);
        p.addTagToTask(TAG2, task);       
        assertTrue(p.getAllTags().size() == 2);
        
        p.addTagToTask(TAG1, task);
        p.addTagToTask(TAG2, task);    
        assertTrue(p.getAllTags().size() == 2);
   }
}
