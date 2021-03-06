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
    
    /**
     * Test Project name checking.
     */
    @Test
    public void testValidProjectNames() {
        String[] validNames = {
                "Projekti",
                "Projekti13_123132",
                "PröjektiÄÄ!!!",
                "'@projekti%¤$£##'",
                ".dot",
                "....dots",
                "joku.projekti.jotain",
                "asioita, asioita, asioita",
        };
        ProjectManager pm = ProjectManager.getInstance();
        for (String name : validNames) {
            if (!pm.isValidProjectName(name)) System.out.println(name);
            assertTrue(pm.isValidProjectName(name));
        }
    }
    
    /**
     * Test Project name checking.
     */
    @Test
    public void testInvalidProjectNames() {
        String[] invalidNames = {
                " Projekti",
                "Projekti ",
                "   Projekti",
                "Projekti?",
                "Projekti.",
                "Projekti*",
                "<Projekti>",
                "Proj|ekti",
                "Projekti NULL",
                ":Projekti:",
                "Proj/ekti",
                "Proj\\ekti",
                "Proj-ekti",
                "CON-testi",
                "ProjektiLPT9",
                "COM5Projekti",
                "",
                "         ",
                "\n",
                "\nProjekti",
                "\tProjekti",
        };
        ProjectManager pm = ProjectManager.getInstance();
        for (String name : invalidNames) {
            if (pm.isValidProjectName(name)) System.out.println(name);
            assertFalse(pm.isValidProjectName(name));
        }
    }
}
