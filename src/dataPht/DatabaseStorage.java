package dataPht;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for storing data in an SQL database.
 * Each project has its own database.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi 
 * @version 1.1 Apr 25, 2021
 */
public class DatabaseStorage implements Storage {
    
    /**
     * The directory used to store all data.
     */
    protected final String STORAGE_DIRECTORY = ".databases";
    
    /**
     * A separator String used to separate 'columns' inside the files.
     */
    protected final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    /**
     * The SQL tables and columns used to stored Project's data.
     * New database is created for each Project.
     */
    protected final String[][] TABLES = {
            
            {"tasks",
                "id INT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "info TEXT NOT NULL, "
                + "done INT NOT NULL, "
                + "priority INT NOT NULL, "
                + "UNIQUE(id)"
              }, 
              
              {"tags",
               "name TEXT NOT NULL, "
               + "UNIQUE(name)"
              }, 
              
              {"relations",
                "taskid INT NOT NULL, "
                + "tagname TEXT NOT NULL"
              }
          
    }; 


    @Override
    public void initialize() throws StorageException {
        setupStorageDirectory();
    }


    @Override
    public void save(Project project) throws StorageException {
        String errorInfo = "Tallennus epäonnistui.";
        
        try (Connection conn = createConnection(project.getName())) {
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
       
        }
    }


    @Override
    public Project getProject(String name) throws StorageException {
        if (!nameAlreadyExists(name)) {
            String info = "Project doesn't exist";
            throw new StorageException(info);    
        }
        Project project = new Project(name);
        
        //Throws StorageException, which is "passed" to the caller
        loadTasks(project);
        generateRelations(project);
        return project;
    }

    private void generateRelations(Project project) throws StorageException {
        try (Connection con = createConnection(project.getName());
                PreparedStatement sql = con.prepareStatement("SELECT * FROM relations")) {
            try (ResultSet results = sql.executeQuery() ) {  
                while (results.next()) {
                    RelationEntry entry = new RelationEntry(results.getInt(1), results.getString(2));
                    Task t = project.getTask(entry.getTaskId());
                    project.addTagToTask(entry.getTagName(), t);
                }
            }
        } catch (SQLException e) {
            String info = "Tietokannan lukeminen epäonnistui.";
            throw new StorageException(info);
        }
        
    }


    private void loadTasks(Project project) throws StorageException {
        try (Connection con = createConnection(project.getName());
            PreparedStatement sql = con.prepareStatement("SELECT * FROM tasks")) {
                try (ResultSet results = sql.executeQuery() ) {  
                    while (results.next()) {
                        String[] taskAsArray = new String[5];
                        for (int i = 0; i < 5; i++) {
                            taskAsArray[i] = results.getString(i+1);
                        }
                        Task t = PhtSerializer.parseTask(taskAsArray);
                        project.insertTask(t);
                    }
                }
        } catch (SQLException e) {
              String info = "Tietokantaan yhdistäminen epäonnistui.";
              throw new StorageException(info);
        }
        
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
            String projectNameWithoutFileExtension = projectName.replace(".db", "");
            if (projectNameWithoutFileExtension != null) {
                projectNames.add(projectNameWithoutFileExtension);
            }
        }
        String[] nameArray = new String[projectNames.size()];
        return projectNames.toArray(nameArray);
    }



    @Override
    public void deleteProject(Project project) throws StorageException {
        deleteProject(project.getName());
    }


    @Override
    public void deleteProject(String projectName) throws StorageException {
        if (nameAlreadyExists(projectName)) {
            File databaseFile = new File(joinpath(projectName) + ".db");
            if (databaseFile.delete() == false) {
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
     * @throws StorageException If table creation fails.
     */
    protected void createTables(Connection conn) throws StorageException {
        String sql;
        
        //Statement can be created with usafe String.format.
        //No userinput is used.
        try (Statement st = conn.createStatement()) {
            for (String[] table : TABLES) {
                String name = table[0];
                String tableSQL = table[1];
                sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s);", name, tableSQL);
                st.executeUpdate(sql);
                
                sql = String.format("DELETE FROM %s WHERE 1=1;", name);
                st.executeUpdate(sql);
            }
            st.close();
        } catch (SQLException e) {
            String info = "Odottamaton virhe. Ei voitu alustaa tietokantaa projektille.";
            throw new StorageException(info);
        }
        
    }
    
    
    /**
     * @param conn Connection object of the database.
     * @param tasks List of all saved Tasks.
     * @throws StorageException If the writing fails.
     */
    protected void saveTasks(Connection conn, List<Task> tasks) throws StorageException {
        String sql = "INSERT INTO tasks (id, name, info, done, priority) "
                     + "VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            for (Task t : tasks) {
                int id = t.getId();
                String name = t.getName();
                String info = t.getInfo();
                int done = t.isDone() ? 1 : 0;
                int priority = 1;
                if (t.getPriority() == Priority.MEDIUM) priority = 2;
                else if (t.getPriority() == Priority.HIGH) priority = 3;
                
                st.setInt(1, id);
                st.setString(2, name);
                st.setString(3, info);
                st.setInt(4, done);
                st.setInt(5, priority);
                st.executeUpdate();
            }
            
            st.close();
        } catch (SQLException e) {
            String info = "Tallentaminen epäonnistui.";
            throw new StorageException(info);
        }
    }
    
    
    /**
     * @param conn Connection object ot the database.
     * @param tags List of all saved Tags.
     * @throws StorageException If the writing fails.
     */
    protected void saveTags(Connection conn, List<Tag> tags) throws StorageException {
        String sql = "INSERT INTO tags (name) VALUES (?);";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
        
            for (Tag t : tags) {
                String name = t.getName();
                st.setString(1, name);
                st.executeUpdate();
            }
            
            st.close();
        } catch (SQLException e) {
            String info = "Tallentaminen epäonnistui.";
            throw new StorageException(info);
        }
    }
    
    /**
     * @param conn Connection object of the database.
     * @param relations List of all saved RelationEntries.
     * @throws StorageException If the writing fails.
     */
    protected void saveRelations(Connection conn, DynamicList<RelationEntry> relations) throws StorageException {
        String sql = "INSERT INTO relations (taskid, tagname) VALUES (?, ?);";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            for (int i = 0; i < relations.count(); i++) {
                RelationEntry re = relations.get(i);
                String tagname = re.getTagName();          
                int taskid = re.getTaskId();
                st.setInt(1, taskid);
                st.setString(2, tagname);
                st.executeUpdate();
            }
            
            st.close();
        } catch (SQLException e) {
            String info = "Tallentaminen epäonnistui.";
            throw new StorageException(info);
        }
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
