package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


import dataPht.ProjectManager;

/**
 * @author varajala
 * @version Feb 19, 2021
 */
public class ProjectManagerTest {
    
    /**
     * Assert that only one instance can be created.
     */
    @Test
    public void testGettingInstance() {
        ProjectManager ins1 = ProjectManager.getInstance();
        ProjectManager ins2 = ProjectManager.getInstance();
        assertTrue(ins1 == ins2);
        
        try {
            ProjectManager pm = new ProjectManager();
            System.out.println(pm);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
