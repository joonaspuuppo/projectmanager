package fxPht;

import javafx.scene.Parent;
import javafx.scene.Scene;

@SuppressWarnings("javadoc")
public class PhtScene extends Scene {
    
    private String currentProject;

    public PhtScene(Parent arg0) {
        super(arg0);
    }
    
    public void setProject(String name) {
        this.currentProject = name;
        System.out.printf("Project set for main Scene: %s%n", name);
    }

    public String getCurrentProject() {
        return this.currentProject;
    }

}
