package dataPht;

import java.io.File;
import java.io.IOException;
import java.sql.*;


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
        // TODO Auto-generated method stub
    }


    @Override
    public Project getProject(String name) throws StorageException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] listAllProjects() throws StorageException {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return false;
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
