package tests;

import java.io.File;
import java.io.IOException;

import dataPht.FileStorage;
import dataPht.Project;

public class TestFileStorage extends FileStorage {

    public final static String TEST_DIRECTORY = ".testing";
    
    
    public TestFileStorage() {
        super();
    }
    
    
    @Override
    protected String getDirectory() {
        return TEST_DIRECTORY;
    }
    
    
    public String getTestDirectory() {
        return this.getDirectory();
    }
    
    
    public void removeStorageDir() {
        File dir = new File(getDirectory());
        deleteAllFiles();
        dir.delete();
    }
    
    
    public void deleteAllFiles() {
        File dir = new File(getDirectory());
        for (File file : dir.listFiles()) {
            file.delete();
        }
    }
    
    
    public String[] getFilepathsForProject(Project p) {
        return super.generateFilePaths(p);
    }
    
    
    public String getProjectNameFromFilepath(String filepath) {
        File f = new File(filepath);
        return super.extractProjectName(f);
    } 
    
    
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
}
