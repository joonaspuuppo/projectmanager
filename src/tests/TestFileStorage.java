package tests;

import java.io.File;

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
        for (File file : dir.listFiles()) {
            file.delete();
        }
        dir.delete();
    }
    
    
    public String[] getFilepathsForProject(Project p) {
        return super.generateFilePaths(p);
    }
    
    
    public String getProjectNameFromFilepath(String filepath) {
        File f = new File(filepath);
        return super.extractProjectName(f);
    } 
}
