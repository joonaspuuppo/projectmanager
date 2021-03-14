package dataPht;

import java.io.File;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.5 Mar 11, 2021
 * Concrete storage class. Saves data to files.
 */
public class FileStorage implements Storage {
    
    private class CorruptDataException extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        public CorruptDataException(String info) {
            super(info);
        }
    }
    
    
    protected final String STORAGE_DIRECTORY = ".data";
    protected final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    protected final String PROJECT_NAME_ESCAPE = "<<PROJECT_NAME>>";
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
        // TODO Auto-generated method stub
    }


    @Override
    public Project getProject(String name) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] listAllProjects() {
        // TODO Auto-generated method stub
        return null;
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
        return false;
    }
    
    
    public String getDirectory() {
        return STORAGE_DIRECTORY;
    }
    
    
    protected void setupStorageDirectory() {
        File storageDir = new File(getDirectory()); 
        if (!storageDir.exists()) {
            boolean OK = storageDir.mkdir();
            if (!OK) {
                String info = "Failed to initialize the storage";
                throw new RuntimeException(info);
            }
        }
    }
    
    
    protected String joinpath(String filename) {
        return getDirectory() + FILE_SEPARATOR + filename;
    }
    
    
    public String[] generateFilePaths(Project p) {
        int numberOfPaths = FILE_PATH_FORMATS.length;
        String[] paths = new String[numberOfPaths];
        for (int i = 0; i < numberOfPaths; i++) {
            String filename = FILE_PATH_FORMATS[i].replace(PROJECT_NAME_ESCAPE, p.getName());
            paths[i] = joinpath(filename);
        }
        return paths;
    }
}
