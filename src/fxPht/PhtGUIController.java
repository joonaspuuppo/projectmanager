package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dataPht.Priority;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import dataPht.ProjectManager;
import dataPht.Task;
import dataPht.Project;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.5 Mar 11, 2021
 * Controller of the main window.
 */
public class PhtGUIController implements Initializable {


    @FXML private MenuItem menuOpenProject;
    @FXML private TextField taskSearchTextField;
    @FXML private ListChooser<Task> taskList;
    @FXML private TextField taskNameField;
    @FXML private Button buttonAddTask;
    @FXML private Button buttonMarkAsDone;
    @FXML private Label projectNameLabel;
    @FXML private Label taskNameLabel;
    @FXML private Label taskPriorityLabel;
    @FXML private Label tagsLabel;
    @FXML private TextArea taskInfoTextArea;
    
    private Project currentProject;
    
    
    /**
     * Setter for current project
     * @param p current project
     */
    public void setCurrentProject(Project p) {
        this.currentProject = p;
        projectNameLabel.setText(p.getName());
        loadTasks();
        refresh();
        
    }
    

    @FXML private void handletaskListSelection() {
        if (taskList.getObjects().isEmpty()) return;
        
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
        if (this.currentProject.getTagsFromTask(t.getId()).isEmpty()) {
            tagsLabel.setText("");
        } else {
            tagsLabel.setText("#" + this.currentProject.getTagsAsString(this.currentProject.getTagsFromTask(t.getId())).replace(", ", "   #"));
        }
        if (t.isDone()) {
            taskNameLabel.setTextFill(Color.GRAY);
            buttonMarkAsDone.setText("Merkitse keskeneräiseksi");
        } else {
            taskNameLabel.setTextFill(Color.BLACK);
            buttonMarkAsDone.setText("Merkitse valmiiksi");
        }
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
        taskList.clear(); 
        String search = taskSearchTextField.getText().toLowerCase().trim();
        
        
        // lists all tasks with names starting with the string user has typed into the search field
        for (Task task : this.currentProject.getAllTasks()) {
            if (task.getName().toLowerCase().startsWith(search)) {
                taskList.add(task.getName(), task);
            }
            
        }
    }
    
    /**
     * When user has typed a key into taskSearchTextField, 
     * list any tasks starting with the search string. 
     */
    @FXML private void handleSearchTasks() {
        handleFilterTasksByName();
    }

    /**
     * Filter Tasks list by Task priority
     */
    @FXML private void handleFilterTasksByPriority() {
        taskList.clear();
        
        // tasks listed from highest priority to lowest
        for(Task task : this.currentProject.getAllTasksByPriority(Priority.HIGH)) {
            taskList.add(task.getName(), task);
        }
        for(Task task : this.currentProject.getAllTasksByPriority(Priority.MEDIUM)) {
            taskList.add(task.getName(), task);
        }
        for(Task task : this.currentProject.getAllTasksByPriority(Priority.LOW)) {
            taskList.add(task.getName(), task);
        }
        
    }

    /**
     * Filter Tasks list by tag
     */
    @FXML private void handleFilterTasksByTag() {
        taskList.clear(); 
        
        // lists all tasks associated with a tag that user is searching for
        for (Task task : this.currentProject.getAllTasksByTag(taskSearchTextField.getText().toLowerCase().trim())) {
            taskList.add(task.getName(), task);
        }
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
        refresh();
    }
    
    @FXML private void handleUpdateTaskInfo() {
        Task t = taskList.getSelectedObject();
        t.setInfo(taskInfoTextArea.getText()); 
    }
  
    
    
    @FXML private void handleMarkAsDone() {
        Task t = taskList.getSelectedObject();
        if (t.isDone()) {
            t.markAsIncomplete();
        } else {
            t.markAsDone();
        }
        refresh();
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
            tagsLabel.setText("");
            return;
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
        if (this.currentProject.getTagsFromTask(t.getId()).isEmpty()) {
            tagsLabel.setText("");
        } else {
            tagsLabel.setText("#" + this.currentProject.getTagsAsString(this.currentProject.getTagsFromTask(t.getId())).replace(", ", "   #"));
        }
        if (t.isDone()) {
            taskNameLabel.setTextFill(Color.GRAY);
            buttonMarkAsDone.setText("Merkitse keskeneräiseksi");
        } else {
            taskNameLabel.setTextFill(Color.BLACK);
            buttonMarkAsDone.setText("Merkitse valmiiksi");
        }
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
            displayError("Ole hyvä ja syötä nimi, jotta voit luoda uuden projektin.");
            return;
        }
        try {
            Project project = ProjectManager.getInstance().createNewProject(projectName);
            setCurrentProject(project);
        } catch (IllegalArgumentException e) {
            displayError("Virheellinen nimi projektille");
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
            return;
        }
        try {
            ProjectManager.getInstance().renameCurrentProject(projectName);
            this.projectNameLabel.setText(projectName);
        } catch (IllegalArgumentException e) {
            displayError("Virheellinen nimi projektille");
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
        alert.setTitle("Virhe");
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
        if (!taskList.getObjects().isEmpty()) taskList.setSelectedIndex(0);
    }
}
