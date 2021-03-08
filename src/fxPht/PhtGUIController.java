package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import dataPht.ProjectManager;
import dataPht.Task;
import dataPht.Project;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.5 Mar 8, 2021
 * Controller of the main window.
 */
public class PhtGUIController implements Initializable {

    @FXML
    private MenuItem menuOpenProject;
    
    @FXML
    private Button buttonAddTask;
    
    @FXML
    private Label projectNameLabel;
    
    
    @FXML
    private ListChooser<Task> taskListChooser;
    
    
    private Project currentProject;
    private Task currentTask;
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // pass
    }
    
    
    /**
     * Set the current Project to be modified in the main window.
     * This should only be called from PhtStartGUIController or inside this class.
     * @param p Project instance.
     */
    public void setCurrentProject(Project p) {
        this.currentProject = p;
        taskListChooser.clear();
        List<Task> tasks = p.getAllTasks();
        for (Task task : tasks) {
            taskListChooser.add(task.getName(), task);
        }
        // TODO clear the task view in the UI and set currentTask to null
        projectNameLabel.setText(p.getName());
    }
    
    
    private void setCurrentTask(Task t) {
        // TODO update the task view
        currentTask = t;
        System.out.println("Selected task is: " + t.getName());
    }
    
    
    public Project getCurrentProject() {
        return this.currentProject;
    }
    
    
    @FXML private void handleOpenTask() {
        Task task = taskListChooser.getSelectedObject();
        if (task != null) {
            setCurrentTask(task);
        }
    }
    
    
    @FXML private void handleAddTask() {
        Task t = currentProject.createTask();
        t.rename("tehtävä");
        taskListChooser.add(t.getName(), t);
    }
    
    
    @FXML private void handleMarkAsDone() {
        Dialogs.showMessageDialog("Ei osata vielä merkitä valmiiksi");
    }
    
    
    @FXML private void handleNimeaTehtavaUudelleen() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nimeä tehtävä uudelleen");
        dialog.setHeaderText("Anna tehtävän uusi nimi");
        dialog.setContentText("Uusi nimi:");
        Optional<String> answer = dialog.showAndWait();
        System.out.println(answer.isPresent() ?
           answer.get() : "Ei ollut vastausta");

    }
    
    
    @FXML private void handleDeleteTask() {
        Dialogs.showQuestionDialog("Poisto?", "Poistetaanko tehtävä?", "Kyllä", "Ei");
    }
     
    
    @FXML private void handleSave() {
        save();
    }
    
    
    /**
     * "Move" back to the starting windpw to open a Project.
     * @throws IOException if the .fxml file is not found.
     */
    @FXML private void handleOpenProject() throws IOException {
        // TODO verify saving
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PhtStartGUIView.fxml"));
        
        BorderPane root = (BorderPane)loader.load();
        Scene startWindow = new Scene(root);
        PhtStartGUIController startWindowController = (PhtStartGUIController) loader.getController();
        startWindowController.loadProjects();
        
        Stage primaryStage = (Stage) buttonAddTask.getScene().getWindow();
        primaryStage.setScene(startWindow);
    }
        
    
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
    
    
    @FXML private void handleDeleteProject() {
        Dialogs.showQuestionDialog("Poisto?", "Haluatko varmasti poistaa projektin?", "Kyllä", "Ei");
    }
    
    
    @FXML private void handleExit() {
        save();
        Platform.exit();
    }

    
    private void save() {
        ProjectManager.getInstance().saveCurrentProject();
    }
    
    
    private void displayError(String info) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }
}
