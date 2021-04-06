package dataPht;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.6 Apr 1, 2021
 * Interface for all external data storing.
 */
public interface Storage {
    
    /**
     * @param project Project to be saved.
     */
    public void save(Project project);
    
    
    /**
     * @param name Name of the wanted Project.
     * @return the Project instance.
     */
    public Project getProject(String name);
    
    
    /**
     * @return String array of all saved projects.
     */
    public String[] listAllProjects();
    
    
    /**
     * @param project Project to be deleted.
     */
    public void deleteProject(Project project);
    
    
    /**
     * @param projectName Name of project to be deleted.
     */
    public void deleteProject(String projectName);
    
    
    /**
     * @param project Project to be renamed.
     * @param newName new name for project
     */
    public void renameProject(Project project, String newName);
    
   
    /**
     * @param name Name of the project.
     * @return true/false
     */
    public boolean nameAlreadyExists(String name);
}
