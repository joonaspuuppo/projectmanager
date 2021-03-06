package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dataPht.Project;
import dataPht.ProjectManager;
import dataPht.Tag;
import dataPht.Task;
import fi.jyu.mit.fxgui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version Jan 21, 2021
 *
 */
public class PhtGUIController implements Initializable {

    @FXML
    private ListChooser<Task> taskList;
    
    @FXML
    private TextField taskNameField;
    
    @FXML
    private MenuItem menuOpenProject;
    
    @FXML
    private Label projectNameLabel;

    @FXML
    private Button buttonAddTask;
    
    private Project currentProject;
    
    
    public void setCurrentProject(Project p) {
        this.currentProject = p;
        // TODO clear data from previous project
        projectNameLabel.setText(p.getName());
        loadTasks();
    }
    
    /**
     * Getter for current project
     * @return current project
     */
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
        Task task = this.getCurrentProject().createTask();
        // if task name is given by user then rename task, otherwise keep default task name
        if (!taskNameField.getText().isBlank()) task.rename(taskNameField.getText());
        loadTasks();
        
        
        //Dialogs.showMessageDialog("Ei osata vielä lisätä");
    }
    
    /**
     * Käsitellään tehtävän merkitseminen valmiiksi
     */
    @FXML private void handleMarkAsDone() {
        taskList.getSelectedObject().markAsDone();
        loadTasks();
        
        //Dialogs.showMessageDialog("Ei osata vielä merkitä valmiiksi");
    }
    
    /**
     * @param event
     */
    @FXML private void handleEditTask() {
        Task selectedTask = taskList.getSelectedObject();
        Project p = this.getCurrentProject();
        
        PhtEditTaskDialogController.editTask(null, selectedTask, p);
        
        loadTasks();
    }
    
    /**
     * Käsitellään tehtävän poistaminen
     */
    @FXML private void handleDeleteTask() {
        Boolean confirmed = Dialogs.showQuestionDialog("Poisto?", "Poistetaanko tehtävä?", "Kyllä", "Ei");
        if (confirmed) {
            this.getCurrentProject().removeTask(taskList.getSelectedObject().getId());
        }
        loadTasks();
    }
     
    
    /**
     * Käsitellään tallennuskäsky
     */
    @FXML private void handleSave() {
        PhtScene s = (PhtScene) buttonAddTask.getScene();
        String projectName = s.getCurrentProject();
        System.out.printf("Project is: %s%n", projectName);
        save();
    }
    
    
    /**
     * Käsitellään projektin avaaminen
     * @throws IOException 
     */
    @FXML private void handleOpenProject() throws IOException {
        // TODO: Tallennus ennen siirtymistä aloitukseen!
        Stage primaryStage = (Stage) buttonAddTask.getScene().getWindow();
        BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("PhtStartGUIView.fxml"));
        Scene startWindow = new Scene(root);
        primaryStage.setScene(startWindow);
        }
        
    
    /**
     * Käsitellään projektin luominen
     */
    @FXML private void handleCreateNewProject() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Uusi projekti");
        dialog.setHeaderText("Anna uuden projektin nimi");
        dialog.setContentText("Projektin nimi:");
        Optional<String> answer = dialog.showAndWait();
        System.out.println(answer.isPresent() ?
           answer.get() : "Ei ollut vastausta");
    }
    
    /**
     * Käsitellään projektin nimeäminen uudelleen
     */
    @FXML private void handleRename() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nimeä uudelleen");
        dialog.setHeaderText("Anna projektin uusi nimi");
        dialog.setContentText("Uusi nimi:");
        Optional<String> answer = dialog.showAndWait();
        System.out.println(answer.isPresent() ?
           answer.get() : "Ei ollut vastausta");
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
     * @param info error info
     */
    private void displayError(String info) {
        //TODO show error
        System.out.println("ERROR: " + info);
    }


    
    /**
     * Loads tasks to taskList
     */
    private void loadTasks() {
        taskList.clear();
        Project p = this.getCurrentProject();
        for (Task task : p.getAllTasks()) {
            taskList.add(task.getName(), task);
        }
    }



}
