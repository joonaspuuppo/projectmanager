package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dataPht.FileStorage;
import dataPht.Project;
import dataPht.StorageException;


/**
 * <b> == ONLY FOR TESTING == </b>
 * 
 * A wrapper class for FileStorage to gain access
 * to the internal implementation of the FileStorage.
 * 
 * Also includes utility methods for testing and routes the 
 * stored data away from the 'production' data storage. 
 * 
 * When creating Project instances, filepath or anything else for testing,
 * make sure that all names are safe for the filesystem.
 * Storage implementations don't perform any checks for safety of the input.  
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 14, 2021
 *
 */
public class TestFileStorage extends FileStorage {

    private final static String TEST_DIRECTORY = ".testing";
    
    /**
     * Initialize the FileStorage.
     * This creates a folder for storing data if one doesn't exist.
     */
    public TestFileStorage() {
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
     * Gets filepaths for given project.
     * @param p Project instance
     * @return String array of all different filepaths the
     * FieldStorage uses to store data for the given Project.
     */
    public String[] getFilepathsForProject(Project p) {
        return generateFilePaths(p);
    }
    
    
    /**
     * Extracts project name from filepath.
     * @param filepath A filepath in the test storage directory
     * @return the name of the project, extracted from the filename
     */
    public String getProjectNameFromFilepath(String filepath) {
        File f = new File(filepath);
        return extractProjectName(f);
    } 
    
    
    /**
     * Generates filepath for testing.
     * @param filename name of file
     * @return Filepath to the data directory as a String.
     */
    public String generateTestFilepath(String filename) {
        return joinpath(filename);
    }
    
    
    /**
     * @param names String array of name for projects
     * <b>THESE NEED TO BE SAFE FOR THE FILESYSYTEM</b>, as no checks
     * are performed.
     */
    public void makeTestFiles(String[] names) {
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Project p = new Project(name);
            String[] filepaths = getFilepathsForProject(p);
            for (String fp : filepaths) {
                try {
                    new File(fp).createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Reads lines from file.
     * @param filename Filename
     * @return List of lines in the file
     */
    public List<String> readFile(String filename) {
        List<String> lines = new ArrayList<String>();
        String filepath = joinpath(filename);
        try (FileInputStream stream = openReadStream(filepath);
             Scanner in = new Scanner(stream)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                lines.add(line);
            }
        } catch (IOException | StorageException e) {
            e.printStackTrace();
        }
        return lines;
    }
    
    
    /**
     * Writes lines to file.
     * @param lines Array of lines to be written.
     * @param filepath Filepath as a String.
     */
    public void writeLinesToFile(String[] lines, String filepath) {
        FileOutputStream stream;
        @SuppressWarnings("resource")
        PrintStream out = null;
        try {
            stream = openWriteStream(filepath);
            out = new PrintStream(stream);
            for (String line : lines) {
                out.println(line);
            }
        } catch (StorageException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
