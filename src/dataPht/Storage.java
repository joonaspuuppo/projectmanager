package dataPht;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 1, 2021
 * Interface for all external data storing.
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi 
 */
public interface Storage {
    
    /**
     * Ensure that the Storage is ready, or fail explicitly if this cannot be done.
     * @throws StorageException If the Storage can't be initialized.
     */
    public void initialize() throws StorageException;
    
    /**
     * Saves the project.
     * @param project Project to be saved.
     * @throws StorageException If anything fails.
     */
    public void save(Project project) throws StorageException;
    
    
    /**
     * Gets the project with the given name. 
     * @param name Name of the wanted Project.
     * @throws StorageException If anything fails.
     * @return the Project instance.
     */
    public Project getProject(String name) throws StorageException;
    
    
    /**
     * Lists all saved projects.
     * @throws StorageException If anything fails.
     * @return String array of all saved projects.
     */
    public String[] listAllProjects() throws StorageException;
    
    
    /**
     * Deletes given project.
     * @param project Project to be deleted.
     * @throws StorageException If anything fails.
     */
    public void deleteProject(Project project) throws StorageException;
    
    
    /**
     * Deletes project with the given projectName attribute.
     * @param projectName Name of project to be deleted.
     * @throws StorageException If anything fails.
     */
    public void deleteProject(String projectName) throws StorageException;
    
    
    /**
     * Renames the given project.
     * @param project Project to be renamed.
     * @param newName new name for project
     * @throws StorageException If anything fails.
     */
    public void renameProject(Project project, String newName) throws StorageException;
    
   
    /**
     * Checks if a projectName is already in use. 
     * @param name Name of the project.
     * @return true/false
     * @throws StorageException If anything fails.
     */
    public boolean nameAlreadyExists(String name) throws StorageException;
}
