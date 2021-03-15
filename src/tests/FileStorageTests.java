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


/**
 * @author Valtteri Rajalainen, Joonas Puuppo
 * @version Mar 15, 2021
 * Unittests for Storing data in files.
 */
public class FileStorageTests {
    
    private static final TestFileStorage FS = new TestFileStorage();
    
    
    /**
     * Remove the testing directory and all of its contents.
     */
    @AfterAll
    public static void cleanup() {
        FS.removeStorageDir();
        
        File testPath = new File(FS.getTestDirectory());
        assertFalse(testPath.exists());
    }
    
    
    /**
     * Remove all files from the testing directory.
     */
    @AfterEach
    public void clearTestDirectory() {
        FS.deleteAllFiles();
    }
    
    
    /**
     * Test the directory creation.
     */
    @Test
    public void testSetup() {
        File testPath = new File(FS.getTestDirectory());
        assertTrue(testPath.exists());
    }
    
    
    /**
     * Test listing the project names.
     */
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
    
    
    /**
     * Test the checking of existance of a given name.
     */
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
    
    
    /**
     * Test the utility function which extracts Project's name from
     * different filenames.
     */
    @Test
    public void testProjectNameExtracting() {
        String filename = "test_project.tasks.dat";
        assertEquals("test_project", FS.getProjectNameFromFilepath(filename));
        
        filename = "asd@123BB.4567c.relations.dat";
        assertEquals("asd@123BB.4567c", FS.getProjectNameFromFilepath(filename));
        
        filename = "a b c.tags.dat";
        assertEquals("a b c", FS.getProjectNameFromFilepath(filename));
    }
    
    
    /**
     * Test filepath creation for POSIX
     */
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
    
    
    /**
     * Test filepath creation for WINDOWS
     */
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
}
