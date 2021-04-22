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

import dataPht.Project;
import dataPht.StorageException;
import dataPht.Task;


public class DatabaseTests {
        
    private static final TestDatabaseStorage DS = new TestDatabaseStorage();
    
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
