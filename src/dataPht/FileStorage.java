package dataPht;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
    
    
    protected final String STORAGE_DIRECTORY = ".data";
    protected final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    protected final String PATH_EXPRESSIONS = "(.+(?=\\.tasks\\.dat))"
            + "|(.+(?=\\.tags\\.dat))"
            + "|(.+(?=\\.relations\\.dat))";
    
    protected final String PROJECT_NAME_ESCAPE  = "<<PROJECT_NAME>>";
    protected final String TASKS_FILE_NAME      = PROJECT_NAME_ESCAPE + ".tasks.dat";
    protected final String TAGS_FILE_NAME       = PROJECT_NAME_ESCAPE + ".tags.dat";
    protected final String RELATIONS_FILE_NAME  = PROJECT_NAME_ESCAPE + ".relations.dat";
    
    protected final String[] FILE_PATH_FORMATS = {
            TASKS_FILE_NAME,
            TAGS_FILE_NAME,
            RELATIONS_FILE_NAME
    };
    
    
    public FileStorage() {
        setupStorageDirectory();
    }

    
    @Override
    public void save(Project project) {
        // TODO Auto-generated method stub
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
