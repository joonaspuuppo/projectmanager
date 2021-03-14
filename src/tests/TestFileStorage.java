package tests;

import java.io.File;

import dataPht.FileStorage;

public class TestFileStorage extends FileStorage {

    public final static String TEST_DIRECTORY = ".testing";
    
    
    public TestFileStorage() {
        super();
    }
    
    
    @Override
    public String getDirectory() {
        return TEST_DIRECTORY;
    }
    
    
    public void removeStorageDir() {
        File dir = new File(getDirectory());
        for (File file : dir.listFiles()) {
            file.delete();
        }
        dir.delete();
    }
}
