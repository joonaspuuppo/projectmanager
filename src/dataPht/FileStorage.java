package dataPht;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.regex.*;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.5 Mar 11, 2021
 * Concrete storage class. Saves data to files.
 */
public class FileStorage implements Storage {
    
    private class StorageException extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        public StorageException(String info) {
            super(info);
        }
    }
    
    
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
    
    
    public FileStorage() {
        setupStorageDirectory();
    }

    
    @Override
    public void save(Project project) {
        List<Task> tasks = project.getAllTasks();
        DynamicList<RelationEntry> relations = project.getRelations();
        String[] filepaths = generateFilePaths(project);
        
        String taskFile      = filepaths[0];
        String tagFile       = filepaths[1];
        String relationsFile = filepaths[2];
        
        saveTasks(tasks, taskFile);
        HashSet<String> usedTags = saveRelations(relations, relationsFile);
        saveTags(usedTags, tagFile);
    }


    @Override
    public Project getProject(String name) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<String> listAllProjects() {
        File storageDir = new File(getDirectory());
        HashSet<String> set = new HashSet<String>();
        ArrayList<String> projectNames = new ArrayList<String>();
        
        for (File file : storageDir.listFiles()) {
            String projectName = extractProjectName(file);
            if (!set.contains(projectName)) {
                projectNames.add(projectName);
                set.add(projectName);
            }
        }
        return projectNames;
    }


    @Override
    public void deleteProject(Project project) {
        // TODO Auto-generated method stub
    }


    @Override
    public void renameProject(Project project) {
        // TODO Auto-generated method stub
    }
    
    
    @Override
    public boolean nameAlreadyExists(String name) {
        List<String> projectNames = listAllProjects();
        return projectNames.contains(name);
    }
    
    
    protected String getDirectory() {
        return STORAGE_DIRECTORY;
    }
    
    
    protected String[] generateFilePaths(Project p) {
        int numberOfPaths = FILE_PATH_FORMATS.length;
        String[] paths = new String[numberOfPaths];
        for (int i = 0; i < numberOfPaths; i++) {
            String filename = FILE_PATH_FORMATS[i].replace(PROJECT_NAME_ESCAPE, p.getName());
            paths[i] = joinpath(filename);
        }
        return paths;
    }
    
    
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
    
    
    protected void setupStorageDirectory() {
        File storageDir = new File(getDirectory()); 
        if (!storageDir.exists()) {
            boolean OK = storageDir.mkdir();
            if (!OK) {
                String info = "Failed to initialize the storage";
                throw new StorageException(info);
            }
        }
    }
    
    
    protected String joinpath(String filename) {
        return getDirectory() + FILE_SEPARATOR + filename;
    }
    
    
    protected void saveTasks(List<Task> tasks, String filepath) {
        FileOutputStream stream = openWriteStream(filepath);
        try (PrintStream out = new PrintStream(stream)) {
            for (Task t : tasks) {
                String line = PhtSerializer.parseString(t);
                out.println(line);
            }
        }
    }
    
    protected HashSet<String> saveRelations(DynamicList<RelationEntry> entries, String filepath) {
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
    
    protected void saveTags(HashSet<String> usedTags, String filepath) {
        FileOutputStream stream = openWriteStream(filepath);
        try (PrintStream out = new PrintStream(stream)) {
            for (String tag : usedTags) {
                out.println(tag);
            }
        }
    }
    
    
    protected FileOutputStream openWriteStream(String filepath) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(filepath));
        } catch (FileNotFoundException e) {
            String info = String.format("Failed to locate file: %s", filepath);
            throw new StorageException(info);
        }
        return stream;
    }
    
    
    protected FileInputStream openReadStream(String filepath) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(filepath));
        } catch (FileNotFoundException e) {
            String info = String.format("Failed to locate file: %s", filepath);
            throw new StorageException(info);
        }
        return stream;
    }
}
