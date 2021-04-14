package dataPht;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.*;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 1, 2021
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi
 * Concrete storage class. Saves data to files.
 */
public class FileStorage implements Storage {
    
    /**
     * The directory used to store all data.
     */
    protected final String STORAGE_DIRECTORY = ".data";
    /**
     * A separator String used to separate 'columns' inside the files.
     */
    protected final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    /**
     * Regular expressions to extract Project names out of the  different filenames.
     */
    protected final String PATH_EXPRESSIONS = "(.+(?=\\.tasks\\.dat))"
            + "|(.+(?=\\.tags\\.dat))"
            + "|(.+(?=\\.relations\\.dat))";
    
    /**
     * Placeholder String replaced with the actual Project's name to form
     * the different filenames.
     */
    protected final String PROJECT_NAME_ESCAPE  = "<<PROJECT_NAME>>";
    
    /**
     * Filenames.
     * 
     * [0] = task file path
     * [1] = tag file path
     * [2] = relations file path
     * 
     * KEEP THIS ORDER
     */
    protected final String[] FILE_PATH_FORMATS = {
            PROJECT_NAME_ESCAPE + ".tasks.dat",
            PROJECT_NAME_ESCAPE + ".tags.dat",
            PROJECT_NAME_ESCAPE + ".relations.dat"
    };
    
    
    @Override
    public void initialize() throws StorageException {
        //Throws StorageException which is "passed" to the user.
        setupStorageDirectory();
    }

    
    @Override
    public void save(Project project) throws StorageException {
        List<Task> tasks = project.getAllTasks();
        DynamicList<RelationEntry> relations = project.getRelations();
        String[] filepaths = generateFilePaths(project);
        
        String taskFile      = filepaths[0];
        String tagFile       = filepaths[1];
        String relationsFile = filepaths[2];
        
        //Throws StorageException, which is "passed" to the caller
        saveTasks(tasks, taskFile);
        HashSet<String> usedTags = saveRelations(relations, relationsFile);
        saveTags(usedTags, tagFile);
    }

    @Override
    public Project getProject(String name) throws StorageException {
        if (!nameAlreadyExists(name)) {
            String info = "Project doesn't exist";
            throw new StorageException(info);    
        }
        Project project = new Project(name);
        String[] filepaths = generateFilePaths(project);
        
        String taskFile      = filepaths[0];
        String relationsFile = filepaths[2];
        
        //Throws StorageException, which is "passed" to the caller
        loadTasks(taskFile, project);
        generateRelations(relationsFile, project);
        return project;
    }


    @Override
    public String[] listAllProjects() throws StorageException {
        File storageDir = new File(getDirectory());
        
        if (!storageDir.exists()) {
            String info = "Tallennettu data on poistettu tai korruptoitunut.";
            throw new StorageException(info);
        }
        
        HashSet<String> set = new HashSet<String>();
        ArrayList<String> projectNames = new ArrayList<String>();
        
        for (File file : storageDir.listFiles()) {
            String projectName = extractProjectName(file);
            if (projectName == null) continue;
            if (!set.contains(projectName)) {
                projectNames.add(projectName);
                set.add(projectName);
            }
        }
        String[] nameArray = new String[projectNames.size()];
        return projectNames.toArray(nameArray);
    }


    @Override
    public void deleteProject(Project project) throws StorageException {
        String projectName = project.getName();
        //Throws StorageException, which is "passed" to the caller
        deleteProject(projectName);
    }
    
    @Override
    public void deleteProject(String projectName) throws StorageException {
        String[] filePaths = generateFilePaths(projectName);
        
        for (String filePath : filePaths) {
            File projectFile = new File(filePath);
            boolean deletionFailed = !projectFile.delete();
            if (deletionFailed) {
                String info = "Projektin poisto epäonnistui";
                throw new StorageException(info);
            }
        }
    }

    @Override
    public void renameProject(Project project, String newName) throws StorageException {
        String oldName = project.getName();
        project.setName(newName);
        //Throws StorageException, which is "passed" to the caller
        save(project);
        deleteProject(oldName);
        
    }
    
    
    @Override
    public boolean nameAlreadyExists(String name) throws StorageException {
        //Throws StorageException, which is "passed" to the caller
        String[] projectNames = listAllProjects();
        
        boolean result = false;
        for (String projectName : projectNames) {
            if (projectName.equals(name)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    
    /**
     * Gets the directory used for storing project data.
     * @return Filepath to the data directory as a String.
     */
    protected String getDirectory() {
        return STORAGE_DIRECTORY;
    }
    
    
    /**
     * Generates filepaths based on a given project's name.
     * @param p project
     * @return filepaths
     */
    protected String[] generateFilePaths(Project p) {
        String projectName = p.getName();
        return generateFilePaths(projectName);
    }
    
    /**
     * Generates filepaths based on a given project name.
     * @param projectName name of project
     * @return filepaths
     */
    protected String[] generateFilePaths(String projectName) {
        int numberOfPaths = FILE_PATH_FORMATS.length;
        String[] paths = new String[numberOfPaths];
        for (int i = 0; i < numberOfPaths; i++) {
            String filename = FILE_PATH_FORMATS[i].replace(PROJECT_NAME_ESCAPE, projectName);
            paths[i] = joinpath(filename);
        }
        return paths;
    }
    
    
    /**
     * Extract the Project's name from a filepath.
     * @param file Filepath as a String.
     * @return Project's name as a String.
     */
    protected String extractProjectName(File file) {
        String projectName = null;
        String filename = file.getName();
        Pattern pattern = Pattern.compile(PATH_EXPRESSIONS);
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            projectName = matcher.group();
        }
        return projectName;
    }
    
    
    /**
     * Ensure that the data directory exists.
     * New directory is created if it doesn't exist.
     * @throws StorageException When the data directory can't be created.
     */
    protected void setupStorageDirectory() throws StorageException {
        File storageDir = new File(getDirectory()); 
        if (!storageDir.exists()) {
            boolean OK = storageDir.mkdir();
            if (!OK) {
                String info = "Ohjelman tallentamisen alustus epäonnistui.\n"
                        + "Ole hyvä ja tarkista, että sinulla on tarvittavat oikeudet uusien tiedostojen luomiseen.";
                throw new StorageException(info);
            }
        }
    }
    
    
    /**
     * Gets a full filepath to the data directory.
     * @param filename Name of the file.
     * @return Filepath to the data directory as a String.
     */
    protected String joinpath(String filename) {
        return getDirectory() + FILE_SEPARATOR + filename;
    }
    
    
    /**
     * Saves all tasks to file.
     * @param tasks List of tasks used in the Project.
     * @param filepath Filepath as a String.
     * @throws StorageException When saving fails.
     */
    protected void saveTasks(List<Task> tasks, String filepath) throws StorageException {
        FileOutputStream stream = openWriteStream(filepath);
        try (PrintStream out = new PrintStream(stream)) {
            for (Task t : tasks) {
                String line = PhtSerializer.parseString(t);
                out.println(line);
            }
        }
    }
    
    
    /**
     * Saves relations to file. 
     * @param entries A DynamicList containing all the Realtion Entry instances used in the Project.
     * @param filepath Filepath as a String.
     * @return A HashSet instance that holds all used tag names as a String.
     * @throws StorageException If saving fails.
     */
    protected HashSet<String> saveRelations(DynamicList<RelationEntry> entries, String filepath) throws StorageException {
        HashSet<String> usedTags = new HashSet<String>();
        FileOutputStream stream = openWriteStream(filepath);
        try (PrintStream out = new PrintStream(stream)) {
            for (int i = 0; i < entries.count(); i++) {
                RelationEntry e = entries.get(i);
                usedTags.add(e.getTagName());
                String line = PhtSerializer.parseString(e);
                out.println(line);
            }
        }
        return usedTags;
    }
    
    
    /**
     * Saves tags to file.
     * @param usedTags HashSet of the used tag names.
     * @param filepath Filepath as a String.
     * @throws StorageException If writing fails.
     */
    protected void saveTags(HashSet<String> usedTags, String filepath) throws StorageException {
        FileOutputStream stream = openWriteStream(filepath);
        try (PrintStream out = new PrintStream(stream)) {
            for (String tag : usedTags) {
                out.println(tag);
            }
        }
    }
    
    
    /**
     * Loads tasks to a project instance.
     * @param filepath Filepath as a String.
     * @param p Project instance where the tasks are loaded.
     * @throws StorageException If reading fails.
     */
    protected void loadTasks(String filepath, Project p) throws StorageException {
        try (FileInputStream stream = openReadStream(filepath);
               Scanner in = new Scanner(stream)) {
               while (in.hasNextLine()) {
                   String line = in.nextLine();
                   Task t = PhtSerializer.parseTask(line);
                   p.insertTask(t);
               }
        } catch (IOException e) {
            String info = "Tiedoston luku epäonnistui";
            throw new StorageException(info);
        }
    }

    
    /**
     * Generates relations to a project instance.
     * @param filepath Filepath as a String.
     * @param p Project instance where the realtions are loaded.
     * @throws StorageException If anything fails.
     */
    protected void generateRelations(String filepath, Project p) throws StorageException {
        try (FileInputStream stream = openReadStream(filepath);
                Scanner in = new Scanner(stream)) {
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    RelationEntry entry = PhtSerializer.parseRelationEntry(line);
                    Task t = p.getTask(entry.getTaskId());
                    p.addTagToTask(entry.getTagName(), t);
                }
         
        } catch (IOException e) {
             String info = "Tiedoston luku epäonnistui";
             throw new StorageException(info);
         
         } catch (PhtSerializer.SerializationException e) {
             String info = "Tiedoston luku epäonnistui";
             throw new StorageException(info);
         }
    }
    
    
    /**
     * Gets a FileOutputStream used for writing to the file.
     * @param filepath Filepath as a String.
     * @return FileOutputStream instance that can be used to write to the file.
     * @throws StorageException When file is not found
     */
    protected FileOutputStream openWriteStream(String filepath) throws StorageException {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(filepath));
        
        } catch (FileNotFoundException e) {
            String info = "Tiedostoon tallennus epäonnistui";
            throw new StorageException(info);
        }
        return stream;
    }
    
    
    /**
     * Gets a FileInputStream used for reading from the file.
     * @param filepath Filepath as a String.
     * @return FileInputStream instance that can be used to read the file.
     * @throws StorageException When the file isn't found.
     */
    protected FileInputStream openReadStream(String filepath) throws StorageException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(filepath));
        } catch (FileNotFoundException e) {
            String info = "Tiedoston luku epäonnistui";
            throw new StorageException(info);
        }
        return stream;
    }
}
