package dataPht;

public class DatabaseStorage implements Storage {

    @Override
    public void initialize() throws StorageException {
        // TODO Auto-generated method stub

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

}
