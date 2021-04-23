package dataPht;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class DatabaseStorage implements Storage {
    
    
    /**
     * The directory used to store all data.
     */
    protected final String STORAGE_DIRECTORY = ".databases";
    
    /**
     * A separator String used to separate 'columns' inside the files.
     */
    protected final String FILE_SEPARATOR = System.getProperty("file.separator");

    
    @Override
    public void initialize() throws StorageException {
        setupStorageDirectory();
    }


    @Override
    public void save(Project project) throws StorageException {
        String errorInfo = "Tallennus epäonnistui.";
        Connection conn = null;
        
        try {
            conn = createConnection(project.getName());
            createTables(conn);
        
            List<Task> taskList = project.getAllTasks();
            List<Tag> tagList = project.getAllTags();
            DynamicList<RelationEntry> relationsList = project.getRelations();
            
            saveTasks(conn, taskList);
            saveTags(conn, tagList);
            saveRelations(conn, relationsList);
        
            conn.close();
            
        } catch (Exception e) {
           throw new StorageException(errorInfo);
       
        } finally {
           if (conn != null) {
               try {
                   conn.close();
               } catch (SQLException e) {
                  String info = "Tietokannan yhteyttä ei voida sulkea.";
                  throw new StorageException(info);
               }
           }
       }
    }


    @Override
    public Project getProject(String name) throws StorageException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] listAllProjects() throws StorageException {
        File storageDir = new File(getDirectory());
        
        if (!storageDir.exists()) {
            String info = "Tallennettu data on poistettu tai korruptoitunut.";
            throw new StorageException(info);
        }
        
        ArrayList<String> projectNames = new ArrayList<String>();
        
        for (File file : storageDir.listFiles()) {
            String projectName = file.getName();
            if (projectName != null) {
                projectNames.add(projectName);
            }
        }
        String[] nameArray = new String[projectNames.size()];
        return projectNames.toArray(nameArray);
    }


    @Override
    public void deleteProject(Project project) throws StorageException {
        // TODO Auto-generated method stub

    }


    @Override
    public void deleteProject(String projectName) throws StorageException {
        // TODO Auto-generated method stub

    }


    @Override
    public void renameProject(Project project, String newName)
            throws StorageException {
        // TODO Auto-generated method stub

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
     * @param databaseName name of the database.
     * @return The sql.Connection.
     * @throws StorageException Thrown if connection can't be established.
     */
    protected Connection createConnection(String databaseName) throws StorageException {
        Connection conn = null;
        try {
            String URI = String.format("jdbc:sqlite:%s.db", joinpath(databaseName));
            conn = DriverManager.getConnection(URI);
        } catch (SQLException e) {
            String info = "Tietojen haku tietokannasta epäonnistui.";
            throw new StorageException(info);
        }
        return conn;
    }
    
    
    /**
     * Check if the needed tables exist, and create them if not.
     * @param conn Connection object to the database.
     */
    protected void createTables(Connection conn) {
        //
    }
    
    
    /**
     * @param conn Connection object ot the database.
     * @param tasks List of all saved Tasks.
     * @throws StorageException If the writing fails.
     */
    protected void saveTasks(Connection conn, List<Task> tasks) throws StorageException {
        //
    }
    
    
    /**
     * @param conn Connection object ot the database.
     * @param tags List of all saved Tags.
     * @throws StorageException If the writing fails.
     */
    protected void saveTags(Connection conn, List<Tag> tags) throws StorageException {
        //
    }
    
    /**
     * @param conn Connection object ot the database.
     * @param relations List of all saved RelationEntries.
     * @throws StorageException If the writing fails.
     */
    protected void saveRelations(Connection conn, DynamicList<RelationEntry> relations) throws StorageException {
        //
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
     * Gets the directory used for storing project data.
     * @return Filepath to the data directory as a String.
     */
    protected String getDirectory() {
        return STORAGE_DIRECTORY;
    }
    
    
    /**
     * Gets a full filepath to the data directory.
     * @param filename Name of the file.
     * @return Filepath to the data directory as a String.
     */
    protected String joinpath(String filename) {
        return getDirectory() + FILE_SEPARATOR + filename;
    }

}
