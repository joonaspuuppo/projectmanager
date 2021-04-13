package dataPht;


import java.util.regex.*;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.6 Apr 1, 2021
 */
public class ProjectManager {
    
    private static ProjectManager instance = null;
    
    private Storage storage = new FileStorage();
    private Project currentProject;
    
    
    /**
     * Ensure that only one instance is created.
     */
    public ProjectManager() {
        if (instance != null) {
            throw new RuntimeException();
        }
        try {
            this.storage.initialize();
        } catch (StorageException e) {
            String info = e.getInfo();
            System.out.println(info);
            System.exit(1);
        }
    }
    
    
    /**
     * The global access point to the class instance.
     * @return The instance of this class.
     */
    public static ProjectManager getInstance() {
        if (instance == null) {
            instance = new ProjectManager();
        }
        return ProjectManager.instance;
    }
    
    
    /**
     * Lists all projects.
     * @return An array of all Project names created.
     * @throws StorageException If any errors occur.
     */
    public String[] listAllProjects() throws StorageException {
        return this.storage.listAllProjects();
    }
    
    
    /**
     * Opens a given project.
     * @param name Name of the Project.
     * @return Project loaded form storage.
     * @throws StorageException If any errors occur.
     */
    public Project openProject(String name) throws StorageException {
        Project project = this.storage.getProject(name);
        this.currentProject = project;
        return project;
    }
    
    
    
    /**
     * Renames current project.
     * @param name new name for project
     * @throws IllegalArgumentException When name is invalid
     * @throws StorageException If any errors occur.
     */
    public void renameCurrentProject(String name) throws IllegalArgumentException, StorageException {
        if (!isValidProjectName(name)) {
            String errorInfo = "Not a valid name for a project.";
            throw new IllegalArgumentException(errorInfo);
        }
        this.storage.renameProject(currentProject, name);
    }
    
    
    /**
     * Writes the current Prject to Storage device.
     * @throws StorageException If any errors occur.
     */
    public void saveCurrentProject() throws StorageException {
        this.storage.save(currentProject);
    }
    
    
    /**
     * Removes current project.
     * @throws StorageException If any errors occur.
     */
    public void removeCurrentProject() throws StorageException {
        this.storage.deleteProject(currentProject);
    }
    
    
    /**
     * Create a blank Project if the name is valid.
     * 
     * @param name Name for the Project.
     * @return A Project instance.
     * @throws IllegalArgumentException When name is invalid.
     * @throws StorageException If any errors occur.
     */
    public Project createNewProject(String name) throws IllegalArgumentException, StorageException {
        if (!isValidProjectName(name)) {
            String errorInfo = "Not a valid name for a project.";
            throw new IllegalArgumentException(errorInfo);
        }
        Project project = new Project(name);
        currentProject = project;
        return project;
    }
    
    
    /**
     * Check if the given String is valid for Project's name.
     * This is basically checking if the String can be used as
     * a filename in Windows, Mac and Linux.
     * 
     * Checks:
     * - if String is blank
     * - if String starts or ends with whitespace
     * - if String ends with a dot
     * - if String contains restricted characters
     * - if String contains reserved names
     * 
     * @param name Name of the Project
     * @return boolean
     * @throws StorageException If the Storage lookup fails.
     */
    public boolean isValidProjectName(String name) throws StorageException {
        if (name.isBlank())                     return false;
        if (startsWithWhitespace(name))         return false;
        if (endsWithWhitespace(name))           return false;
        if (endsWithDot(name))                  return false;
        if (containsReservedCharacters(name))   return false;
        if (containsReservedNames(name))        return false;
        if (nameAlreadyExists(name))            return false;
        return true;
    }
    
    
    private boolean nameAlreadyExists(String name) throws StorageException{
        return storage.nameAlreadyExists(name);
    }


    private boolean endsWithDot(String name) {
        return name.endsWith(".");
    }


    private boolean containsReservedNames(String name) {
        final String[] RESERVED_NAMES = {
                "CON", "PRN", "AUX", "NUL",
                "COM1", "COM2", "COM3", "COM4",
                "COM5", "COM6", "COM7", "COM8",
                "COM9", "LPT1", "LPT2", "LPT3",
                "LPT4", "LPT5", "LPT6", "LPT7",
                "LPT8", "LPT9"
        };
        for (String reservedName : RESERVED_NAMES) {
            Pattern pattern = Pattern.compile(reservedName);
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) return true;
        }
        return false;
    }


    private boolean containsReservedCharacters(String name) {
        final String RESTRICTED_CHARS = "<|>|:|\"|/|\\||\\?|\\*|-|\\\\";
        Pattern pattern = Pattern.compile(RESTRICTED_CHARS);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }


    private boolean endsWithWhitespace(String name) {
        char last = name.charAt(name.length()-1);
        return Character.isWhitespace(last);
    }


    private boolean startsWithWhitespace(String name) {
        char first = name.charAt(0);
        return Character.isWhitespace(first);
    }
    
}
