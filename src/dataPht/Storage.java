package dataPht;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 1, 2021
 * Interface for all external data storing.
 */
public interface Storage {
    
    /**
     * Ensure that the Storage is ready, or fail explicitly if this cannot be done.
     * @throws StorageException If the Storage can't be initialized.
     */
    public void initialize() throws StorageException;
    
    /**
     * @param project Project to be saved.
     * @throws StorageException If anything fails.
     */
    public void save(Project project) throws StorageException;
    
    
    /**
     * @param name Name of the wanted Project.
     * @throws StorageException If anything fails.
     * @return the Project instance.
     */
    public Project getProject(String name) throws StorageException;
    
    
    /**
     * @throws StorageException If anything fails.
     * @return String array of all saved projects.
     */
    public String[] listAllProjects() throws StorageException;
    
    
    /**
     * @param project Project to be deleted.
     * @throws StorageException If anything fails.
     */
    public void deleteProject(Project project) throws StorageException;
    
    
    /**
     * @param projectName Name of project to be deleted.
     * @throws StorageException If anything fails.
     */
    public void deleteProject(String projectName) throws StorageException;
    
    
    /**
     * @param project Project to be renamed.
     * @param newName new name for project
     * @throws StorageException If anything fails.
     */
    public void renameProject(Project project, String newName) throws StorageException;
    
   
    /**
     * @param name Name of the project.
     * @return true/false
     * @throws StorageException If anything fails.
     */
    public boolean nameAlreadyExists(String name) throws StorageException;
}
