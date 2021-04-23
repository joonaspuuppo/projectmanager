package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.sql.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import dataPht.Priority;
import dataPht.Project;
import dataPht.StorageException;
import dataPht.Task;


public class DatabaseTests {
        
    private static final TestDatabaseStorage DS = new TestDatabaseStorage();
    
    //DUMMY PROJECT ATTRIBUTES
    private static final String TAG1 = "tag1";
    private static final String TAG2 = "tag2";
    private static final String TAG3 = "tag3";
    
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
    
    
    @Test
    public void testConnection() {
        DS.makeTestFiles(new String[] {"testdb.db"});
        Connection conn = DS.connect("testdb");
        assertTrue(conn != null);
        
        if (conn == null) return;
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
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
            e.printStackTrace();
        }
        
        Connection conn = DS.connect(p.getName());
        
        verifyTasks(conn, p);
        
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
        verifyTasks(conn, p);
            
        /*
            st = conn.createStatement();
            sql = "SELECT * FROM tags";
            st.executeQuery(sql);
            st.close();
        
            st = conn.createStatement();
            sql = "SELECT * FROM relations";
            st.executeQuery(sql);
            st.close();
       */
        
       try {
           conn.close();
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
    }


    private void verifyTasks(Connection conn, Project p) {
        try {
            ResultSet rows;
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM tasks";
            rows = st.executeQuery(sql);
            st.close();
            
            while (rows.next()) {
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
                
                if (t.getPriority() == Priority.LOW) assertTrue(priority == 0);
                else if (t.getPriority() == Priority.MEDIUM) assertTrue(priority == 1);
                else if (t.getPriority() == Priority.HIGH) assertTrue(priority == 2);
                
                if (t.isDone()) assertTrue(done == 1);
                else if (!t.isDone()) assertTrue(done == 0);
            }
        } catch (Exception e) {
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
