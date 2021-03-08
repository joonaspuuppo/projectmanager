package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import dataPht.Priority;
import dataPht.Project;
import dataPht.Task;
import dataPht.Tag;


/**
 * @author varajala
 * @version Feb 19, 2021
 */
public class ProjectTest {
    
    private final String PROJECT_NAME = "TEST";
    
    private final String TAG1 = "tag1";
    private final String TAG2 = "tag2";
    private final String TAG3 = "tag3";
    
    
    private Project createTestProject() {
        return new Project(PROJECT_NAME);
    }
    
    private List<String> tagListToStringList(List<Tag> tagList) {
        List<String> stringList = new ArrayList<String>();
        for (Tag t : tagList) {
            stringList.add(t.getName());
        }
        return stringList;
    }
    
    
    /**
     * Set up a dummy project wiht known relations
     * between data to test the queries.
     * 
     * Project data:
     * 
     * TASK1 -> TAG1
     * TASK1 -> TAG2
     * 
     * TASK2 -> TAG2
     * TASK2 -> TAG3
     * 
     * TASK3 -> TAG3
     * 
     * TASK4 -> (no tags)
     */
    private Project generateDummyProject() {
        Project p = new Project(PROJECT_NAME);
        
        Task t1 = p.createTask();
        Task t2 = p.createTask();
        Task t3 = p.createTask();
        p.createTask();
        
        p.addTagToTask(TAG1, t1);
        p.addTagToTask(TAG2, t1);
        
        p.addTagToTask(TAG2, t2);
        p.addTagToTask(TAG3, t2);
        
        p.addTagToTask(TAG3, t3);
        return p;
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
    
    
   /**
    * Test querying Tasks associated with a specified tag.
    * 
    *  See the generateDummyProject description for details on the
    *  data relations.
    */
   @Test
   public void testTaskQueryByTag() {
       Project p = generateDummyProject();
       Task t1 = p.getTask(1);
       Task t2 = p.getTask(2);
       Task t3 = p.getTask(3);
       
       List<Task> expectedList = new ArrayList<Task>();
       expectedList.add(t1);
       List<Task> taskList = p.getAllTasksByTag(TAG1);
       assertEquals(taskList, expectedList);
       
       expectedList.add(t2);
       taskList = p.getAllTasksByTag(TAG2);
       assertEquals(taskList, expectedList);
       
       expectedList = new ArrayList<Task>();
       expectedList.add(t2);
       expectedList.add(t3);
       taskList = p.getAllTasksByTag(TAG3);
       assertEquals(taskList, expectedList);
   }
   
   
   /**
    * Test querying Tags associated with a specified task.
    * 
    *  See the generateDummyProject description for details on the
    *  data relations.
    */
   @Test
   public void testTagQueryByTask() {
       Project p = generateDummyProject();
       Task t1 = p.getTask(1);
       Task t2 = p.getTask(2);
       Task t3 = p.getTask(3);
       Task t4 = p.getTask(4);
       
       List<String> expectedList = new ArrayList<String>();
       expectedList.add(TAG1);
       expectedList.add(TAG2);
       List<Tag> tagList = p.getTagsFromTask(t1.getId());
       assertEquals(tagListToStringList(tagList), expectedList);
       
       expectedList = new ArrayList<String>();
       expectedList.add(TAG2);
       expectedList.add(TAG3);
       tagList = p.getTagsFromTask(t2.getId());
       assertEquals(tagListToStringList(tagList), expectedList);
       
       expectedList = new ArrayList<String>();
       expectedList.add(TAG3);
       tagList = p.getTagsFromTask(t3.getId());
       assertEquals(tagListToStringList(tagList), expectedList);
       
       expectedList = new ArrayList<String>();
       tagList = p.getTagsFromTask(t4.getId());
       assertEquals(tagListToStringList(tagList), expectedList);
   }
   
   /**
    * Test querying tasks with specified priority
    */
   @Test
   public void testTaskQueryByPriority() {
       Project p = generateDummyProject();
       Task t1 = p.getTask(1);
       Task t2 = p.getTask(2);
       Task t3 = p.getTask(3);
       t1.setPriority(Priority.HIGH);
       t2.setPriority(Priority.HIGH);
       t3.setPriority(Priority.LOW);
       List<Task> resultsByPriority = p.getAllTasksByPriority(Priority.HIGH);
       List<Task> expectedResults = new ArrayList<Task>();
       expectedResults.add(t2);
       expectedResults.add(t1);
       assertEquals(expectedResults, resultsByPriority);
       
   }
   
   /**
    * Test removing single Tags from a specified Task.
    */
   @Test
   public void testTagRemoval() {
       Project p = generateDummyProject();
       Task t1 = p.getTask(1);
       Task t2 = p.getTask(2);
       
       List<String> expectedList = new ArrayList<String>();
       expectedList.add(TAG2);
       
       // test 1
       List<String> startCondition = new ArrayList<String>();
       startCondition.add(TAG1);
       startCondition.add(TAG2);
       
       List<Tag> tagList = p.getTagsFromTask(t1.getId());
       assertEquals(startCondition, tagListToStringList(tagList));
       
       p.removeTagFromTask(TAG1, t1);
       
       tagList = p.getTagsFromTask(t1.getId());
       assertEquals(tagListToStringList(tagList), expectedList);
       
       
       // test 2
       startCondition = new ArrayList<String>();
       startCondition.add(TAG2);
       startCondition.add(TAG3);
       
       tagList = p.getTagsFromTask(t2.getId());
       assertEquals(startCondition, tagListToStringList(tagList));
       
       p.removeTagFromTask(TAG3, t2);
       
       tagList = p.getTagsFromTask(t2.getId());
       assertEquals(tagListToStringList(tagList), expectedList);
   }
   
    /**
     * Test removing a task.
     */
    @Test
    public void testTaskRemoval() {
        Project p = generateDummyProject();
        List<Task> taskList = p.getAllTasks();
        assertEquals(4, taskList.size());
        p.removeTask(4); 
        taskList = p.getAllTasks();
        assertEquals(3, taskList.size());
        p.removeTask(5); // not a task so nothing should be removed
        taskList = p.getAllTasks();
        assertEquals(3, taskList.size());

    }
}
