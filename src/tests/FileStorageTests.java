package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import dataPht.Priority;
import dataPht.Project;
import dataPht.Task;
import dataPht.TaskSerializer;


public class FileStorageTests {
    
    private static final TestFileStorage FS = new TestFileStorage();
    
    
    @AfterAll
    public static void cleanup() {
        FS.removeStorageDir();
        
        File testPath = new File(FS.getTestDirectory());
        assertFalse(testPath.exists());
    }
    
    
    @AfterEach
    public void clearTestDirectory() {
        FS.deleteAllFiles();
    }
    
    
    @Test
    public void testSetup() {
        File testPath = new File(FS.getTestDirectory());
        assertTrue(testPath.exists());
    }
    
    
    @Test
    public void testProjectListing() {
        assertTrue(FS.listAllProjects().size() == 0);
        
        String[] testNames = new String[] {"test_project1", "test_project2"};
        FS.makeTestFiles(testNames);
        
        List<String> names = FS.listAllProjects();
        assertEquals(testNames.length, names.size());
        for (String name : testNames) {
            assertTrue(names.contains(name));
        }
    }
    
    
    @Test
    public void testNameValidation() {
        String[] testNames = new String[] {
                "project001",
                "project002",
                "project003",
                "project004",
                "project005",
                };
        FS.makeTestFiles(testNames);
        
        assertTrue(FS.nameAlreadyExists("project001"));
        assertTrue(FS.nameAlreadyExists("project002"));
        assertTrue(FS.nameAlreadyExists("project003"));
        assertTrue(FS.nameAlreadyExists("project004"));
        assertTrue(FS.nameAlreadyExists("project005"));
        
        assertFalse(FS.nameAlreadyExists("project006"));
        assertFalse(FS.nameAlreadyExists("project000"));
        assertFalse(FS.nameAlreadyExists("asd"));
    }
    
    
    @Test
    public void testProjectNameExtracting() {
        String filename = "test_project.tasks.dat";
        assertEquals("test_project", FS.getProjectNameFromFilepath(filename));
        
        filename = "asd@123BB.4567c.relations.dat";
        assertEquals("asd@123BB.4567c", FS.getProjectNameFromFilepath(filename));
        
        filename = "a b c.tags.dat";
        assertEquals("a b c", FS.getProjectNameFromFilepath(filename));
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
        assertEquals(expected, Arrays.toString(FS.getFilepathsForProject(p)));
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
        assertEquals(expected, Arrays.toString(FS.getFilepathsForProject(p)));
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
