package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dataPht.FileStorage;
import dataPht.Priority;
import dataPht.Task;
import dataPht.TaskSerializer;


public class FileStorageTests {
    
    private final FileStorage fs = new FileStorage();
    
    
    @Test
    public void testTaskStringParsing() {
        Task t = new Task(1);
        t.rename("task1");
        t.setPriority(Priority.HIGH);
        t.setInfo("some task to be done");
        t.markAsDone();
        
        String expectedResult = "1|task1|3|true|some task to be done";
        assertEquals(expectedResult, TaskSerializer.parseString(t));
        
        t.rename("do something");
        t.setPriority(Priority.LOW);
        t.markAsIncomplete();
        expectedResult = "1|do something|1|false|some task to be done";
        assertEquals(expectedResult, TaskSerializer.parseString(t));
        
        t.setInfo("This is a String\\nwith multiple lines");
        expectedResult = "1|do something|1|false|This is a String<<newline>>with multiple lines";
        assertEquals(expectedResult, TaskSerializer.parseString(t));
    }
    
    
    @Test
    public void testTaskParsing() {
        String str = "1|do something|1|false|some task to be done";
        Task t = TaskSerializer.parseTask(str);
        assertEquals(t.getId(), 1);
        assertEquals(t.getName(), "do something");
        assertEquals(t.getPriority(), Priority.LOW);
        assertEquals(t.isDone(), false);
        assertEquals(t.getInfo(), "some task to be done");
        
        str = "1|task1|2|true|This is a String<<newline>>with multiple lines";
        t = TaskSerializer.parseTask(str);
        assertEquals(t.getId(), 1);
        assertEquals(t.getName(), "task1");
        assertEquals(t.getPriority(), Priority.MEDIUM);
        assertEquals(t.isDone(), true);
        assertEquals(t.getInfo(), "This is a String\\nwith multiple lines");
    }
}
