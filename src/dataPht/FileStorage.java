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
    
    
    private void setupStorageDirectory() {
        File storageDir = new File(STORAGE_DIRECTORY); 
        if (!storageDir.exists()) {
            boolean OK = storageDir.mkdir();
            if (!OK) {
                String info = "Failed to initialize the storage";
                throw new RuntimeException(info);
            }
        }
    } 
    
    
    protected String joinpath(String filename) {
        return STORAGE_DIRECTORY + FILE_SEPARATOR + filename;
    }
}
