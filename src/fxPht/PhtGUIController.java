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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
    private Task currentTask; // null if no task is selected
    
//  PROJECT -----------------------------------------------------------------------------
    
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
    
    /**
     * Getter for current project
     * @return current project
     */
    public Project getCurrentProject() {
        return this.currentProject;
    }
      
    /**
     * "Move" back to the starting window to open a Project.
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
     
    @FXML private void handleRenameProject() {
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
        // TODO: delete project
    }
    
    @FXML private void handleSave() {
        save();
    }

    private void save() {
        ProjectManager.getInstance().saveCurrentProject();
    }
    
    @FXML private void handleExit() {
        save();
        Platform.exit();
    }
    
//  TASKS -----------------------------------------------------------------------------
    
    /**
     * Sets this.currentTask. t can be null. 
     * @param t task
     */
    public void setCurrentTask(Task t) {
        this.currentTask = t;
        refresh();
    }
    
    /**
     * Called when user interacts with taskList. 
     */
    @FXML private void handleTaskListSelection() {
        if (taskList.getObjects().isEmpty()) {
            setCurrentTask(null);
        } else {
            setCurrentTask(taskList.getSelectedObject());
        }
    }
    
    /**
     * Called when user types a key into taskSearchField.
     */
    @FXML private void handleSearchTasks() {
        handleFilterTasksByName();
    }
    
    /**
     * Filter Tasks list by name of task
     */
    @FXML private void handleFilterTasksByName() {
        if (taskSearchTextField.getText().equals("")) {
            loadTasks();
            return;
        }
        taskList.clear(); 
        String query = taskSearchTextField.getText().toLowerCase().trim();

        // lists all tasks with names containing the search query
        for (Task task : this.currentProject.getAllTasks()) {
            if (task.getName().toLowerCase().contains(query)) {
                if (!task.isDone()) taskList.add(task.getName(), task);
                if (task.isDone()) taskList.add(task.getName() + " (valmis)", task);
            }
        }
    }

    /**
     * Filter Tasks list by Task priority
     */
    @FXML private void handleFilterTasksByPriority() {
        taskList.clear();
        
        // tasks listed from highest priority to lowest
        for(Task task : this.currentProject.getAllTasksByPriority(Priority.HIGH)) {
            if (!task.isDone()) taskList.add(task.getName(), task);
        }
        for(Task task : this.currentProject.getAllTasksByPriority(Priority.MEDIUM)) {
            if (!task.isDone()) taskList.add(task.getName(), task);
        }
        for(Task task : this.currentProject.getAllTasksByPriority(Priority.LOW)) {
            if (!task.isDone()) taskList.add(task.getName(), task);
        } 
        
        // list tags marked as done at the end of the list (not in order of priority)
        for(Task task : this.currentProject.getAllTasks()) {
            if (task.isDone()) taskList.add(task.getName() + " (valmis)", task);
        } 
    }

    /**
     * Filter Tasks list by tag
     */
    @FXML private void handleFilterTasksByTag() {
        taskList.clear(); 
        String query = taskSearchTextField.getText().toLowerCase().trim();
        
        // lists all tasks associated with a tag that user is searching for
        for (Task task : this.currentProject.getAllTasksByTag(query)) {
            if (!task.isDone()) taskList.add(task.getName(), task);
            if (task.isDone()) taskList.add(task.getName() + " (valmis)", task);
        }
    }
    
    /**
     * Adds task to project.
     */
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
    
    /**
     * Called when user types a key into taskInfoTextArea.
     */
    @FXML private void handleUpdateTaskInfo() {
        if (currentTask == null) return;
        currentTask.setInfo(taskInfoTextArea.getText()); 
    }
    
    /**
     * Called when user clicks the "Mark as done" button.
     */
    @FXML private void handleMarkAsDone() {
        if (currentTask == null) return;
        Task t = currentTask;
        if (t.isDone()) {
            t.markAsIncomplete();
        } else {
            t.markAsDone();
        }
        loadTasks();
        taskList.setSelectedIndex(getTaskListIndex(t));
        setCurrentTask(taskList.getSelectedObject());
        refresh();
    }
    
    /**
     * Opens a dialog for editing a Task.
     */
    @FXML private void handleEditTask() {
        if (currentTask == null) return;
        Task t = currentTask;
        PhtEditTaskDialogController.editTask(null, t, currentProject);
        
        // taskList has to be updated in case taskName was changed
        loadTasks();
        
        // selecting edited Task from taskList
        taskList.setSelectedIndex(getTaskListIndex(t));
        setCurrentTask(taskList.getSelectedObject());
        refresh();
    }
    
    /**
     * Finds the taskList index of a given Task
     * @param t task
     * @return index of task or -1 if not found.
     */
    private int getTaskListIndex(Task t) {
        for (int i = 0; i < currentProject.getAllTasks().size(); i++) {
            if (taskList.getObjects().get(i).getId() == t.getId()) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Refreshes main window's right side to show selected Task's information
     */
    private void refresh() {
        // if nothing is selected no task information is displayed.
        if (currentTask == null) {
            taskNameLabel.setText("");
            taskInfoTextArea.setText("");
            taskPriorityLabel.setText("");
            tagsLabel.setText("");
            buttonMarkAsDone.setText("Merkitse valmiiksi");
            return;
        }
        taskNameLabel.setText(currentTask.getName());
        taskInfoTextArea.setText(currentTask.getInfo());
        taskPriorityLabel.setText(getPriorityAsString());
        tagsLabel.setText(getTagsAsHashTags());
        buttonMarkAsDone.setText(getMarkAsDoneButtonText());
        setTaskDisplayColor();
    }
    
    /**
     * Loads taskList. Unless taskList is empty, selects the first Task on the list.
     */
    private void loadTasks() {
        taskList.clear();
        for (Task task : currentProject.getAllTasks()) {
            if (!task.isDone()) taskList.add(task.getName(), task);
        }
        for (Task task : currentProject.getAllTasks()) {
            if (task.isDone()) taskList.add(task.getName() + " (valmis)", task);
        }
        if (!taskList.getObjects().isEmpty()) {
            taskList.setSelectedIndex(0);
            setCurrentTask(taskList.getSelectedObject());
        } else {
            setCurrentTask(null);
        }
    }

    /**
     * Opens a dialog for deleting a task.
     */
    @FXML private void handleDeleteTask() {
        if (currentTask == null) return;
        Boolean confirmed = Dialogs.showQuestionDialog("Poisto?", "Poistetaanko tehtävä?", "Kyllä", "Ei");
        if (confirmed) {
            this.getCurrentProject().removeTask(currentTask.getId());
            loadTasks();
            refresh();
        }
    }
    
    /**
     * Gets the string representation of a Task's priority setting.
     * Used to set taskPriorityLabel text.
     * @return priority as string or empty string if medium priority is selected.
     */
    private String getPriorityAsString() {
        if (currentTask.getPriority() == Priority.LOW) return "(kiireetön)";
        if (currentTask.getPriority() == Priority.HIGH) return "(kiireellinen)";
        return "";
    }
    
    /**
     * Gets a Task's tags as a String where a #-character precedes every tagName.
     * @return tags as hashtags or an empty string if task has no tags.
     */
    private String getTagsAsHashTags() {
        if (this.currentProject.getTagsFromTask(currentTask.getId()).isEmpty()) return "";
        List<Tag> tags = this.currentProject.getTagsFromTask(currentTask.getId());
        String tagsWithCommas = this.currentProject.getTagsAsString(tags);
        String tagsAsHashTags = "#" + tagsWithCommas.replace(", ", "   #");
        if (tagsAsHashTags.equals("#")) return ""; // in case tagsWithCommas is empty
        return tagsAsHashTags;
    }
    
    private String getMarkAsDoneButtonText() {
        if (currentTask.isDone()) return "Merkitse keskeneräiseksi";
        return "Merkitse valmiiksi";
    }
    
    /**
     * Sets label color depending on whether currentTask is done or not. 
     */
    public void setTaskDisplayColor() {
        if (currentTask.isDone()) {
            taskNameLabel.setTextFill(Color.GRAY);
            taskPriorityLabel.setTextFill(Color.GRAY);
            tagsLabel.setTextFill(Color.GRAY);
        } else {
            taskNameLabel.setTextFill(Color.BLACK);
            taskPriorityLabel.setTextFill(Color.BLACK);
            tagsLabel.setTextFill(Color.BLACK);
        }
    }
    
//  OTHER -----------------------------------------------------------------------------
    
    private void displayError(String info) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Virhe");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //
    }
}
