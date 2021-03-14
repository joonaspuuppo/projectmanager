package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import dataPht.FileStorage;
import dataPht.Priority;
import dataPht.Project;
import dataPht.Task;
import dataPht.TaskSerializer;


public class FileStorageTests {
    
    private static final FileStorage FS = new TestFileStorage();
    
    
    @AfterAll
    public static void cleanup() {
        TestFileStorage testfs = (TestFileStorage)FS;
        testfs.removeStorageDir();
        
        File testPath = new File(FS.getDirectory());
        assertFalse(testPath.exists());
    }
    
    
    @Test
    public void testSetup() {
        assertEquals(FS.getDirectory(), TestFileStorage.TEST_DIRECTORY);
        File testPath = new File(FS.getDirectory());
        assertTrue(testPath.exists());
    }
    
    
    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    public void testFilePathsPOSIX() {
        Project p = new Project("test_project");
        String expected = "["
                + ".testing/test_project.tasks.dat, "
                + ".testing/test_project.tags.dat, "
                + ".testing/test_project.relations.dat"
                + "]";
        assertEquals(expected, Arrays.toString(FS.generateFilePaths(p)));
    }
    
    
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testFilePathsWIN() {
        Project p = new Project("test_project");
        String expected = "["
                + ".testing\\test_project.tasks.dat, "
                + ".testing\\test_project.tags.dat, "
                + ".testing\\test_project.relations.dat"
                + "]";
        assertEquals(expected, Arrays.toString(FS.generateFilePaths(p)));
    }
    
    
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
