package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import dataPht.Project;
import dataPht.StorageException;
import dataPht.Task;


/**
 * Unit tests for storing data in files.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 14, 2021
 */
public class FileStorageTests {
    
    private static final TestFileStorage FS = new TestFileStorage();
    
    //DUMMY PROJECT ATTRIBUTES
    private static final String PROJECT_NAME = "test";
    private static final String TAG1 = "tag1";
    private static final String TAG2 = "tag2";
    private static final String TAG3 = "tag3";
    
    
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
     * Test saving a project.
     */
    @Test
    public void testSaving() {
        Project p = generateDummyProject();
        try {
            FS.save(p);
        } catch (StorageException e) {
            e.printStackTrace();
        }
        
        String[] tasks = {
                "1|task1|2|false|info1",
                "2|task2|2|false|info2",
                "3|task3|2|false|info3",
                "4|task4|2|false|info4",
        };
        String[] relations = {
                "1|" + TAG1,
                "1|" + TAG2,
                "2|" + TAG2,
                "2|" + TAG3,
                "3|" + TAG3,
        };
        String[] tags = {TAG1, TAG2, TAG3};
        String taskFile = PROJECT_NAME + ".tasks.dat";
        String tagFile = PROJECT_NAME + ".tags.dat";
        String relationsFile = PROJECT_NAME + ".relations.dat";
        
        List<String> fileContent = FS.readFile(taskFile);
        assertEquals(tasks.length, fileContent.size());
        for (String line : tasks) {
            assertTrue(fileContent.contains(line));
        }
        
        fileContent = FS.readFile(tagFile);
        assertEquals(tags.length, fileContent.size());
        for (String line : tags) {
            assertTrue(fileContent.contains(line));
        }
        
        fileContent = FS.readFile(relationsFile);
        assertEquals(relations.length, fileContent.size());
        for (String line : relations) {
            assertTrue(fileContent.contains(line), line);
        }
    }
    
    
    /**
     * Test loading a project from file.
     */
    @Test
    public void testLoading() {
        String taskFile = FS.generateTestFilepath(PROJECT_NAME + ".tasks.dat");
        String relationsFile = FS.generateTestFilepath(PROJECT_NAME + ".relations.dat");
        
        String[] tasks = {
                "1|task1|2|false|info1",
                "2|task2|2|false|info2",
                "3|task3|2|false|info3",
                "4|task4|2|false|info4",
        };
        String[] relations = {
                "1|" + TAG1,
                "1|" + TAG2,
                "2|" + TAG2,
                "2|" + TAG3,
                "3|" + TAG3,
        };
        FS.writeLinesToFile(tasks, taskFile);
        FS.writeLinesToFile(relations, relationsFile);
        try {
            Project p = FS.getProject(PROJECT_NAME);
            assertEquals(PROJECT_NAME, p.getName());
            
            assertEquals(4, p.getAllTasks().size());
            Task t1 = p.getTask(1);
            Task t2 = p.getTask(2);
            Task t3 = p.getTask(3);
            assertTrue(p.getTask(4) != null);
            
            List<Task> tagList1 = p.getAllTasksByTag(TAG1);
            List<Task> tagList2 = p.getAllTasksByTag(TAG2);
            List<Task> tagList3 = p.getAllTasksByTag(TAG3);
            
            assertEquals(1, tagList1.size());
            assertTrue(tagList1.contains(t1));
            
            assertEquals(2, tagList2.size());
            assertTrue(tagList2.contains(t1));
            assertTrue(tagList2.contains(t2));
            
            assertEquals(2, tagList3.size());
            assertTrue(tagList3.contains(t2));
            assertTrue(tagList3.contains(t3));
        } catch (StorageException e) {
            e.printStackTrace();
        }
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
        try {
            assertTrue(FS.listAllProjects().length == 0);
        } catch (StorageException e) {
            e.printStackTrace();
        }
        
        String[] testNames = new String[] {"test_project1", "test_project2"};
        FS.makeTestFiles(testNames);
        
        try {
            String[] names = FS.listAllProjects();
            assertEquals(testNames.length, names.length);
            for (String name : testNames) {
                boolean found = false;
                for (String pName : names) {
                    if (pName.equals(name)) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
        } catch (StorageException e) {
            e.printStackTrace();
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
        
        try {
            assertTrue(FS.nameAlreadyExists("project001"));
            assertTrue(FS.nameAlreadyExists("project002"));
            assertTrue(FS.nameAlreadyExists("project003"));
            assertTrue(FS.nameAlreadyExists("project004"));
            assertTrue(FS.nameAlreadyExists("project005"));
        
            assertFalse(FS.nameAlreadyExists("project006"));
            assertFalse(FS.nameAlreadyExists("project000"));
            assertFalse(FS.nameAlreadyExists("asd"));
        } catch (StorageException e) {
            e.printStackTrace();
        }
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
    
    /**
     * Test project deletion.
     */
    @Test
    public void testProjectDeletion() {
        clearTestDirectory();
        Project p = generateDummyProject();
        
        try {
            FS.save(p);
            assertEquals("test", FS.listAllProjects()[0]);
            FS.deleteProject(p);
            assertEquals(0, FS.listAllProjects().length);
        
            p = generateDummyProject();
            FS.save(p);
            assertEquals("test", FS.listAllProjects()[0]);
            FS.deleteProject("test");
            assertEquals(0, FS.listAllProjects().length);
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Test renaming of a project and its files.
     */
    @Test
    public void testProjectRenaming() {
        clearTestDirectory();
        Project p = generateDummyProject();
        
        try {
            FS.save(p);
            FS.renameProject(p, "newName");
        } catch (StorageException e) {
            e.printStackTrace();
        }
        
        assertEquals("newName", p.getName());
        String[] filepaths = FS.getFilepathsForProject(p);
        assertTrue(filepaths[0].contains("newName"));
        assertTrue(filepaths[1].contains("newName"));
        assertTrue(filepaths[2].contains("newName"));
        clearTestDirectory();
    }
    
    /**
     * Set up a dummy project with known relations
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
     * 
     * TASK1 = NAME:"task1",INFO:"info1"
     * TASK2 = NAME:"task2",INFO:"info2"
     * TASK3 = NAME:"task3",INFO:"info3"
     * TASK4 = NAME:"task4",INFO:"info4" 
     *  
     */
    private Project generateDummyProject() {
        Project p = new Project(PROJECT_NAME);
        
        Task t1 = p.createTask(); t1.rename("task1"); t1.setInfo("info1");
        Task t2 = p.createTask(); t2.rename("task2"); t2.setInfo("info2");
        Task t3 = p.createTask(); t3.rename("task3"); t3.setInfo("info3");
        Task t4 = p.createTask(); t4.rename("task4"); t4.setInfo("info4");
        
        p.addTagToTask(TAG1, t1);
        p.addTagToTask(TAG2, t1);
        
        p.addTagToTask(TAG2, t2);
        p.addTagToTask(TAG3, t2);
        
        p.addTagToTask(TAG3, t3);
        return p;
    }
}
