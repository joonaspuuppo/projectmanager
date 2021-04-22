package tests;

import java.io.File;
import java.sql.*;
import java.io.IOException;

import dataPht.DatabaseStorage;
import dataPht.StorageException;


/**
 * <b> == ONLY FOR TESTING == </b>
 * 
 * A wrapper class for DatabaseStorage to gain access
 * to the internal implementation of the DatabaseStorage.
 * 
 * Also includes utility methods for testing and routes the 
 * stored data away from the 'production' data storage. 
 * 
 * When creating Project instances, filepaths or anything else for testing,
 * make sure that all names are safe for the filesystem.
 * Storage implementations don't perform any checks for safety of the input.  
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 14, 2021
 */
public class TestDatabaseStorage extends DatabaseStorage {
    
    private final static String TEST_DIRECTORY = ".testdatabases";
    
    /**
     * Initialize the FileStorage.
     * This creates a folder for storing data if one doesn't exist.
     */
    public TestDatabaseStorage() {
        super();
        try {
            initialize();
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected String getDirectory() {
        return TEST_DIRECTORY;
    }
    
    
    /**
     * @param DBName Name of the database.
     * @return COnnection object.
     */
    public Connection connect(String DBName) {
        Connection conn = null;
        try {
            conn = super.createConnection(DBName);
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    /**
     * Gets test directory.
     * @return The filepath of the testing directory 
     */
    public String getTestDirectory() {
        return this.getDirectory();
    }
    
    /**
     * Removes the testing storage directory and all its contents
     */
    public void removeStorageDir() {
        File dir = new File(getDirectory());
        deleteAllFiles();
        dir.delete();
    }
    
    /**
     * Removes all files from the testing storage directory
     */
    public void deleteAllFiles() {
        File dir = new File(getDirectory());
        for (File file : dir.listFiles()) {
            file.delete();
        }
    }
    
    /**
     * @param names String array of name for projects
     * <b>THESE NEED TO BE SAFE FOR THE FILESYSYTEM</b>, as no checks
     * are performed.
     */
    public void makeTestFiles(String[] names) {
        for (String name : names) {
            String filepath = joinpath(name);
            try {
                new File(filepath).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
