package dataPht;

/**
 * @author varajala
 * @version Feb 19, 2021
 */
public class ProjectManager {
    
    private static final ProjectManager INSTANCE = new ProjectManager();
    
    private Storage storage = new FileStorage();
    private Project currentProject;
    
    
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
    
    
    /**
     * @param name -
     * @return -
     */
    public Project openProject(String name) {
        return null;
        // TODO
    }
    
    
    
    /**
     * @param name -
     */
    public void renameCurrentProject(String name) {
        // TODO
    }
    
    
    /**
     * -
     */
    public void saveCurrentProject() {
        // TODO
    }
    
    
    /**
     * -
     */
    public void removeCurrentProject() {
        // TODO
    }
    
    
    /**
     * @param name -
     * @return -
     */
    public Project createNewProject(String name) {
        return null;
        // TODO
    }
    
}
