package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dataPht.Priority;
import dataPht.Project;
import dataPht.ProjectManager;
import dataPht.Task;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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


    @FXML private MenuItem menuOpenProject;
    @FXML private TextField taskSearchTextField;
    @FXML private ListChooser<Task> taskList;
    @FXML private TextField taskNameField;
    @FXML private Button buttonAddTask;
    @FXML private Label projectNameLabel;
    @FXML private Label taskNameLabel;
    @FXML private Label taskPriorityLabel;
    @FXML private HBox tagLabelHBox;
    @FXML private TextArea taskInfoTextArea;
    
    private Project currentProject;
    
    
    
    /**
     * Setter for current project
     * @param p current project
     */
    public void setCurrentProject(Project p) {
        this.currentProject = p;
        // TODO clear data from previous project
        projectNameLabel.setText(p.getName());
        loadTasks();
        
    }
    

    @FXML private void handletaskListSelection() {
        Task t = taskList.getSelectedObject();
        taskNameLabel.setText(t.getName());
        taskInfoTextArea.setText(t.getInfo());
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
        //
    }
    
    /**
     * Filter Tasks list by name of task
     */
    @FXML private void handleFilterTasksByName() {
        // TODO
    }

    /**
     * Filter Tasks list by Task priority
     */
    @FXML private void handleFilterTasksByPriority() {
        // TODO
    }

    /**
     * Filter Tasks list by tag
     */
    @FXML private void handleFilterTasksByTag() {
        // TODO
    }
    
    
    @FXML private void handleOpenTask() {
        // pass
    }
    
    
    @FXML private void handleAddTask() {
        Task task = this.getCurrentProject().createTask();
        // if task name is given by user then rename task, otherwise keep default task name
        if (!taskNameField.getText().isBlank()) {
            task.rename(taskNameField.getText());
            taskNameField.setText("");
        }
        loadTasks();
    }
    
    
    @FXML private void handleMarkAsDone() {
        taskList.getSelectedObject().markAsDone();
        // TODO: Add visual indication of task being done
        loadTasks();
        
        //Dialogs.showMessageDialog("Ei osata vielä merkitä valmiiksi");
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
    
    
    /**
     * @param event
     */
    @FXML private void handleEditTask() {
        Task selectedTask = taskList.getSelectedObject();
        Project p = this.getCurrentProject();
        
        PhtEditTaskDialogController.editTask(null, selectedTask, p);
        
        loadTasks();
        for (int i = 0; i < p.getAllTasks().size(); i++) {
            if (taskList.getObjects().get(i).getId() == selectedTask.getId()) {
                taskList.setSelectedIndex(i);
                refresh();
                break;
            }
        }
    }
    
    /**
     * Refreshes main window right side to show selected Task's information
     */
    private void refresh() {
        if (taskList.getObjects().isEmpty()) {
            taskNameLabel.setText("");
            taskInfoTextArea.setText("");
            taskPriorityLabel.setText("");
            return;
            // TODO: hide tags
        }
        
        Task t = taskList.getSelectedObject();
        taskNameLabel.setText(t.getName());
        taskInfoTextArea.setText(t.getInfo());
        if (t.getPriority() == Priority.LOW) {
            taskPriorityLabel.setText("(kiireetön)");
        } else if (t.getPriority() == Priority.MEDIUM) {
            taskPriorityLabel.setText("");
        } else {
            taskPriorityLabel.setText("(kiireellinen)");
        }
        // TODO: tags, markAsDone
    }


    @FXML private void handleDeleteTask() {
        Boolean confirmed = Dialogs.showQuestionDialog("Poisto?", "Poistetaanko tehtävä?", "Kyllä", "Ei");
        if (confirmed) {
            this.getCurrentProject().removeTask(taskList.getSelectedObject().getId());
        }
        loadTasks();
        refresh();
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
    
    
    private void loadTasks() {
        taskList.clear();
        Project p = this.getCurrentProject();
        for (Task task : p.getAllTasks()) {
            taskList.add(task.getName(), task);
        }
        if (!p.getAllTasks().isEmpty()) {
            taskList.setSelectedIndex(0);
            Task t = taskList.getSelectedObject();
            taskNameLabel.setText(t.getName());
            taskInfoTextArea.setText(t.getInfo());
        }
    }
}
