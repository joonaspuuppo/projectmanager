package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.sql.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import dataPht.Priority;
import dataPht.Project;
import dataPht.StorageException;
import dataPht.Tag;
import dataPht.Task;


/**
 * Tests for DatabaseStorage.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi 
 * @version 1.1 Apr 27, 2021
 *
 */
public class DatabaseTests {
        
    private static final TestDatabaseStorage DS = new TestDatabaseStorage();
    
    //DUMMY PROJECT ATTRIBUTES
    private static final String TAG1 = "tag1";
    private static final String TAG2 = "tag2";
    private static final String TAG3 = "tag3";
    private static final String[] TAG_NAMES = {TAG1, TAG2, TAG3};
    
    /**
     * Remove the testing directory and all of its contents.
     */
    @AfterAll
    public static void cleanup() {
        DS.removeStorageDir();
        
        File testPath = new File(DS.getTestDirectory());
        assertFalse(testPath.exists());
    }
    
    
    /**
     * Remove all files from the testing directory.
     */
    @AfterEach
    public void clearTestDirectory() {
        DS.deleteAllFiles();
    }
    
    
    /**
     * Tests SQL connection to database.
     */
    @Test
    public void testConnection() {
        DS.makeTestFiles(new String[] {"testdb.db"});
        try (Connection conn = DS.connect("testdb")) {
            assertTrue(conn != null);
            if (conn == null) return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Test listing projects. 
     */
    @Test
    public void testListingProjects() {
        String[] projectNames = {"test1", "test2", "test3"};
        
        try {
            String[] results = DS.listAllProjects();
            assertTrue(results.length == 0);
        } catch (StorageException e) {
            e.printStackTrace();
        }
        
        DS.makeTestFiles(projectNames);
        
        try {
            String[] results = DS.listAllProjects();
            assertTrue(results.length == projectNames.length);
            List<String> nameList = Arrays.asList(projectNames);
            for (String name : projectNames) {
                assertTrue(nameList.contains(name));
            }
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Test checking if project name is already in use.
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
        DS.makeTestFiles(testNames);
        
        try {
            assertTrue(DS.nameAlreadyExists("project001"));
            assertTrue(DS.nameAlreadyExists("project002"));
            assertTrue(DS.nameAlreadyExists("project003"));
            assertTrue(DS.nameAlreadyExists("project004"));
            assertTrue(DS.nameAlreadyExists("project005"));
        
            assertFalse(DS.nameAlreadyExists("project006"));
            assertFalse(DS.nameAlreadyExists("project000"));
            assertFalse(DS.nameAlreadyExists("asd"));
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Test saving a project.
     */
    @Test
    public void testSaving() {
        String projectName = "savetest";
        Project p = generateDummyProject(projectName);
        try {
            DS.save(p);
        } catch (StorageException e) {
            System.out.println(e.getInfo());
            e.printStackTrace();
        }
        
        verifyTasks(p);
        verifyTags(p);
        verifyRelations(p);
        
        Task t1 = p.getTask(1);
        t1.markAsDone();
        t1.rename("some task");
        
        Task t2 = p.getTask(2);
        t2.setInfo("Some information...");
        t2.rename("some other task");
        
       try {
            DS.save(p);
        } catch (StorageException e1) {
            e1.printStackTrace();
        }
        verifyTasks(p);
        verifyTags(p);
        verifyRelations(p);
    }
    
    /**
     * Test loading a project from database.
     */
    @Test
    public void testLoading() {
        String projectName = "loadingtest";
        Project p = generateDummyProject(projectName);
        try {
            DS.save(p);
        } catch (StorageException e) {
            System.out.println(e.getInfo());
            e.printStackTrace();
        }
        try {
            p = DS.getProject(projectName);
            assertEquals(projectName, p.getName());
            
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
     * Test project deletion.
     */
    @Test
    public void testProjectDeletion() {
        Project p = generateDummyProject("deletingtest");
        
        try {
            DS.save(p);
            assertEquals("deletingtest", DS.listAllProjects()[0]);
            DS.deleteProject(p);
            assertEquals(0, DS.listAllProjects().length);
        
            p = generateDummyProject("deletingtest");
            DS.save(p);
            assertEquals("deletingtest", DS.listAllProjects()[0]);
            DS.deleteProject("deletingtest");
            assertEquals(0, DS.listAllProjects().length);
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Test renaming of a project.
     */
    @Test
    public void testProjectRenaming() {
        String projectName = "renamingtest";
        Project p = generateDummyProject(projectName);
        
        try {
            DS.save(p);
            DS.renameProject(p, "newName");
        } catch (StorageException e) {
            e.printStackTrace();
        }
        
        assertEquals("newName", p.getName());
        
        try {
            assertTrue(DS.nameAlreadyExists("newName"));
            assertFalse(DS.nameAlreadyExists("renamingtest"));
            assertTrue(DS.listAllProjects()[0].equals("newName"));
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }


    /**
     * Verifies task data in database.
     * @param p project
     */
    private void verifyTasks(Project p) {
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DS.connect(p.getName()); 
             Statement st = conn.createStatement();
             ResultSet rows = st.executeQuery(sql)) { 
            int taskCount = 0;
            while (rows.next()) {
                taskCount += 1;
                int id = rows.getInt("id");
                String name = rows.getString("name");
                String info = rows.getString("info");
                int priority = rows.getInt("priority");
                int done = rows.getInt("done");
                
                Task t = p.getTask(id);
                if (t == null) {
                    fail();
                    continue;
                }
                assertEquals(t.getName(), name);
                assertEquals(t.getInfo(), info);
                
                if (t.getPriority() == Priority.LOW) assertTrue(priority == 1);
                else if (t.getPriority() == Priority.MEDIUM) assertTrue(priority == 2);
                else if (t.getPriority() == Priority.HIGH) assertTrue(priority == 3);
                
                if (t.isDone()) assertTrue(done == 1);
                else if (!t.isDone()) assertTrue(done == 0);
                
            }
            assertEquals(p.getAllTasks().size(), taskCount);
        } catch (Exception e) {
           fail();
        }
    }
    
    
    /**
     * Verifies tag data in database.
     * @param p project
     */
    private void verifyTags(Project p) {
        String sql = "SELECT * FROM tags";
        try (Connection conn = DS.connect(p.getName());
             Statement st = conn.createStatement();
             ResultSet rows = st.executeQuery(sql)) {
            int tagCount = 0;
            boolean found;
            while (rows.next()) {
                tagCount += 1;
                found = false;
                String name = rows.getString("name");
                for (String tagName : TAG_NAMES) {
                    if (tagName.equals(name)) {
                        assertTrue(true);
                        found = true;
                        break;
                    }
                }
                if (!found) fail();
            }
            assertEquals(p.getAllTags().size(), tagCount);
        } catch (SQLException e) {
            fail();
        }
    }
    
    /**
     * Verifies relation data in database.
     * @param p project
     */
    private void verifyRelations(Project p) {
        Hashtable<String, List<Task>> relations = new Hashtable<String, List<Task>>();
        for (Tag t : p.getAllTags()) {
            List<Task> tasks = p.getAllTasksByTag(t.getName());
            relations.put(t.getName(), tasks);
        }
        
        String sql = "SELECT * FROM relations";
        try (Connection conn = DS.connect(p.getName());
             Statement st = conn.createStatement();
             ResultSet rows = st.executeQuery(sql);) {
            int relationCount = 0;
            boolean found;
            while (rows.next()) {
                relationCount += 1;
                found = false;
                int taskid = rows.getInt("taskid");
                String tagname = rows.getString("tagname");
                List<Task> tasks = relations.get(tagname);
                if (tasks == null) {
                    found = true;
                    continue;
                }
                for (Task t : tasks) {
                    int id = t.getId();
                    if (id == taskid) {
                        found = true;
                        break;
                    }
                } 
                if (!found) fail();
            }
            assertEquals(p.getRelations().count(), relationCount);
        } catch (SQLException e) {
            fail();
        }
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
    private Project generateDummyProject(String name) {
        Project p = new Project(name);
        
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
