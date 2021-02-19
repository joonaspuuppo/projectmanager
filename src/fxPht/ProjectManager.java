package fxPht;

/**
 * @author varajala
 * @version Feb 19, 2021
 */
public class ProjectManager {
    
    private static final ProjectManager INSTANCE = new ProjectManager();
    
    
    /**
     * Ensure that only one instance is created.
     */
    public ProjectManager() {
        if (INSTANCE != null) {
            throw new RuntimeException();
        }
    }
    
    
    /**
     * The global access point to the class instance.
     * @return The instance of this class.
     */
    public static ProjectManager getInstance() {
        return ProjectManager.INSTANCE;
    }
    
    
    
}
