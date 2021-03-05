package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import dataPht.ProjectManager;
import dataPht.Project;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version Jan 21, 2021
 */
public class PhtGUIController implements Initializable {

    @FXML
    private MenuItem menuOpenProject;
    
    @FXML
    private Button buttonAddTask;
    
    @FXML
    private Label projectNameLabel;
    
    
    private Project currentProject;
    
    
    public void setCurrentProject(Project p) {
        this.currentProject = p;
        // TODO clear data from previous project and 
        //load tasks from project to the listchooser
        projectNameLabel.setText(p.getName());
    }
    
    public Project getCurrentProject() {
        return this.currentProject;
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Käsitellään uuden tehtävän lisääminen
     */
    @FXML private void handleAddTask() {
        Dialogs.showMessageDialog("Ei osata vielä lisätä");
    }
    
    /**
     * Käsitellään tehtävän merkitseminen valmiiksi
     */
    @FXML private void handleMarkAsDone() {
        Dialogs.showMessageDialog("Ei osata vielä merkitä valmiiksi");
    }
    
    /**
     * @param event
     */
    @FXML private void handleNimeaTehtavaUudelleen() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nimeä tehtävä uudelleen");
        dialog.setHeaderText("Anna tehtävän uusi nimi");
        dialog.setContentText("Uusi nimi:");
        Optional<String> answer = dialog.showAndWait();
        System.out.println(answer.isPresent() ?
           answer.get() : "Ei ollut vastausta");

    }
    
    /**
     * Käsitellään tehtävän poistaminen
     */
    @FXML private void handleDeleteTask() {
        Dialogs.showQuestionDialog("Poisto?", "Poistetaanko tehtävä?", "Kyllä", "Ei");
    }
     
    
    /**
     * Handle saving.
     */
    @FXML private void handleSave() {
        save();
    }
    
    
    /**
     * Käsitellään projektin avaaminen
     * @throws IOException 
     */
    @FXML private void handleOpenProject() throws IOException {
        // TODO
    }
        
    
    /**
     * Handle creation of a new Project.
     */
    @FXML private void handleCreateNewProject() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Uusi projekti");
        dialog.setHeaderText("Anna uuden projektin nimi");
        dialog.setContentText("Projektin nimi:");
        Optional<String> answer = dialog.showAndWait();
        
        String projectName = answer.isPresent() ? answer.get() : null;
        if (projectName == null) {
            displayError("Insert a name to create new project.");
            return;
        }
        try {
            Project project = ProjectManager.getInstance().createNewProject(projectName);
            setCurrentProject(project);
        } catch (IllegalArgumentException e) {
            displayError("Invalid name for a Project.");
        }
    }
    
    /**
     * Handle renaming the Project.
     */
    @FXML private void handleRename() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nimeä uudelleen");
        dialog.setHeaderText("Anna projektin uusi nimi");
        dialog.setContentText("Uusi nimi:");
        Optional<String> answer = dialog.showAndWait();
        
        String projectName = answer.isPresent() ? answer.get() : null;
        if (projectName == null) {
            displayError("Insert a name to rename the project.");
            return;
        }
        try {
            ProjectManager.getInstance().renameCurrentProject(projectName);
        } catch (IllegalArgumentException e) {
            displayError("Invalid name for a Project.");
        }
    }
    
    /**
     * Käsitellään projektin poistaminen
     */
    @FXML private void handleDeleteProject() {
        Dialogs.showQuestionDialog("Poisto?", "Haluatko varmasti poistaa projektin?", "Kyllä", "Ei");
    }
    
    
    /**
     * Käsitellään lopetuskäsky
     */
    @FXML private void handleExit() {
        save();
        Platform.exit();
    }

    
    /**
     * Save modifications to the current Project.
     */
    private void save() {
        ProjectManager.getInstance().saveCurrentProject();
    }
    
    
    /**
     * Display error to the user.
     */
    private void displayError(String info) {
        //TODO show error
        System.out.println("ERROR: " + info);
    }



}
