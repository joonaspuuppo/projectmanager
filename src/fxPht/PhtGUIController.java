package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import dataPht.Priority;
import dataPht.Project;
import dataPht.ProjectManager;
import dataPht.StorageException;
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
 * @version 1.0 Apr 1, 2021
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi
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
    private Task currentTask;
    
    /**
     * Setter for current project
     * @param p current project
     */
    public void setCurrentProject(Project p) {
        this.currentProject = p;
        projectNameLabel.setText(p.getName());
        refreshTasks();
        Task t = getSelectedTask();
        setCurrentTask(t);
    }
    
    /**
     * Getter for current Project
     * @return current Project
     */
    public Project getCurrentProject() {
        return this.currentProject;
    }

    /**
     * Getter for current task
     * @return current Task
     */
    public Task getCurrentTask() {
        return this.currentTask;
    }

    /**
     * @return current Task's ID or -1 if Task is null.
     */
    public int getCurrentTaskId() {
        if (getCurrentTask() == null) return -1;
        return this.currentTask.getId();
    }

    /**
     * Set the current Task.
     * @param task Task
     */
    public void setCurrentTask(Task task) {
        resetTaskView();
        this.currentTask = task;
        updateTaskView(task);
    }

 
    /**
     * "Move" back to the starting window to open a Project.
     * @throws IOException if the .fxml file is not found.
     */
    @FXML private void handleOpenProject() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PhtStartGUIView.fxml"));
        
        BorderPane root = (BorderPane)loader.load();
        Scene startWindow = new Scene(root);
        PhtStartGUIController startWindowController = (PhtStartGUIController) loader.getController();
        startWindowController.loadProjects();
        
        Stage primaryStage = (Stage) buttonAddTask.getScene().getWindow();
        primaryStage.setScene(startWindow);
    }

    
    /**
     * Handle event for creating a new project.
     */
    @FXML private void handleCreateNewProject() {
        String title = "Luo uusi projekti";
        String header = "";
        String context = "Projektin nimi:";
        String projectName = askString(title, header, context);
        if (projectName == null) {
            displayError("Ole hyvä ja syötä nimi, jotta voit luoda uuden projektin.");
            return;
        }
        try {
            Project project = ProjectManager.getInstance().createNewProject(projectName);
            setCurrentProject(project);
        
        } catch (IllegalArgumentException e) {
            displayError("Virheellinen nimi projektille");
        
        } catch (StorageException e) {
            displayError(e.getInfo());
        }
    }


    /**
     * Handle event for renaming a project.
     */
    @FXML private void handleRenameProject() {
        String title = "Nmeä uudelleen";
        String header = "";
        String context = "Uusi nimi:";
        String projectName = askString(title, header, context);
        
        if (projectName == null) return;
        
        try {
            save();
            ProjectManager.getInstance().renameCurrentProject(projectName);
            this.projectNameLabel.setText(projectName);
        
        } catch (IllegalArgumentException e) {
            displayError("Virheellinen nimi projektille");
        
        } catch (StorageException e) {
            //TODO revert back to opening stage, since this project is corrupt?
            displayError(e.getInfo());
        }
    }


    /**
     * Handle event for deleting a project.
     */
    @FXML private void handleDeleteProject() throws IOException {
        boolean delete = Dialogs.showQuestionDialog(
                "Poisto?",
                "Haluatko varmasti poistaa projektin?",
                "Kyllä",
                "Ei"
        );
        if (delete) {
            try {
                save();
                ProjectManager.getInstance().removeCurrentProject();
            } catch (StorageException e) {
                //TODO some files are missing or renamed by the user.
                //Still delete them to avoid this exception...
                displayError(e.getInfo());
            }
            handleOpenProject();
        }
        
    }

    
    /**
     * Hanlde event for saving.
     */
    @FXML private void handleSave() {
        try {
            save();
        } catch (StorageException e) {
            //TODO revert back to opening stage, since this project is corrupt?
            displayError(e.getInfo());
        }
    }


    /**
     * Handle event for creating a new project.
     */
    @FXML private void handleExit() {
        try {
            save();
        } catch (StorageException e) {
            displayError(e.getInfo());
        }
        Platform.exit();
    }

    /**
     * Opens a dialog for deleting a task.
     */
    @FXML private void handleDeleteTask() {
        if (getCurrentTask() == null) return;
        Boolean confirmed = Dialogs.showQuestionDialog(
                "Poisto?",
                "Poistetaanko tehtävä?",
                "Kyllä",
                "Ei"
        );
        if (confirmed) {
            getCurrentProject().removeTask(getCurrentTaskId());
            resetTaskView();
            refreshTasks();
        }
    }


    /**
     * Handle event for selecting tasks.
     * Called when user interacts with taskList. 
     */
    @FXML private void handleTaskListSelection() {
        Task task = getSelectedTask();
        setCurrentTask(task);
    }
    
    
    /**
     * Handle event for searching tasks.
     * Called when user types a key into taskSearchField.
     */
    @FXML private void handleSearchTasks() {
        handleFilterTasksByName();
    }
    
    /**
     * Handle event for filtering tasks by name.
     * Filter Tasks list by name of task
     */
    @FXML private void handleFilterTasksByName() {
        String query = taskSearchTextField.getText();
        query = query.toLowerCase().trim();
        if (query.equals("")) {
            refreshTasks();
            return;
        }
        sortTaskListByName(query);
    }

    
    /**
     * Handle event for filtering tasks by priority.
     */
    @FXML private void handleFilterTasksByPriority() {
        sortTaskListByPriority(); 
    }

    /**
     * Handle event for filtering tasks by priority.
     * Filter Tasks list by tag
     */
    @FXML private void handleFilterTasksByTag() { 
        String query = taskSearchTextField.getText().trim();
        sortTaskListByTag(query);
    }
    
    /**
     * Handle event for adding tasks.
     * Adds task to project.
     */
    @FXML private void handleAddTask() {
        Task newTask = getCurrentProject().createTask();
        String taskName = readTaskNameField();
        if (!taskName.equals("")) newTask.rename(taskName);
        setCurrentTask(newTask);
        refreshTasks();
    }
    
    /**
     * Handle updating task view.
     * Called when user types a key into taskInfoTextArea.
     */
    @FXML private void handleUpdateTaskInfo() {
        Task task = getCurrentTask();
        if (task != null) {
            String newInfo = taskInfoTextArea.getText();
            task.setInfo(newInfo); 
        }
    }
    
    /**
     * Handle event for toggling done.
     * Called when user clicks the "Mark as done" button.
     */
    @FXML private void handleMarkAsDone() {
        Task task = getCurrentTask();
        if (task != null) {
            task.toggleDone();
            updateTaskView(task);
            refreshTasks();
        }
    }
    
    /**
     * Handle event for editing tasks.
     * Opens a dialog for editing a Task.
     */
    @FXML private void handleEditTask() {
        Task task = getCurrentTask();
        if (task != null) {
            PhtEditTaskDialogController.editTask(null, task, currentProject);
            updateTaskView(task);
            refreshTasks();
        }
    }


    /**
     * Show an error dialog to the user
     */
    private void displayError(String info) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Virhe");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }


    /**
     * Show a dialog to ask userinput.
     * @return The string user entered, or null if none was inserted. 
     */
    private String askString(String title, String header, String context) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(context);
        Optional<String> answer = dialog.showAndWait();
        return answer.isPresent() ? answer.get() : null;
    }


    /**
     * Read and reset the contents of the taskNameField.
     * @return Contents of the textField.
     */
    private String readTaskNameField() {
        String text = taskNameField.getText();
        taskNameField.setText("");
        return text;
    }
    
    
    /**
     * Finds the taskList index of a given Task
     * @param t Task
     * @return index of Task or 0 if not found.
     */
    private int getTaskListIndex(Task t) {
        if (t == null) return 0;
        for (int i = 0; i < currentProject.getAllTasks().size(); i++) {
            if (taskList.getObjects().get(i).getId() == t.getId()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Refresh the taskList.
     * Remembers the selected Task.
     */
    private void refreshTasks() {
        List<Task> tasks = getCurrentProject().getAllTasks();
        Task task = getSelectedTask();
        updateTaskList(tasks);
        int index = getTaskListIndex(task);
        taskList.setSelectedIndex(index);
    }
    
    /**
     * Clear the taskList and insert all Tasks given into it.
     * @param tasks List of Tasks
     */
    private void updateTaskList(List<Task> tasks) {
        taskList.clear();
        for (Task task : tasks) {
            if (!task.isDone()) taskList.add(task.getName(), task);
        }
        for (Task task : tasks) {
            if (task.isDone()) taskList.add(task.getName() + " ✓", task);
        }
    }
    
    
    /**
     * Get the selected task.
     * @return null if no Task selected, else the Task instance
     */
    private Task getSelectedTask() {
        return taskList.getSelectedObject();
    }
    

    /**
     * Clear the Task view right side of the screen and set default values.
     */
    private void resetTaskView() {
        taskNameLabel.setText("");
        taskInfoTextArea.setText("");
        taskPriorityLabel.setText("");
        tagsLabel.setText("");
        buttonMarkAsDone.setText("Merkitse valmiiksi");
    }

    
    /**
     * Insert values to the Task view right side
     * of the screen based on the given Task's values.  
     */
    private void updateTaskView(Task task) {
        if (currentTask == null) return;
        taskNameLabel.setText(task.getName());
        taskInfoTextArea.setText(task.getInfo());
        taskPriorityLabel.setText(Utils.taskPriorityToString(task));
        
        List<Tag> tags= getCurrentProject().getTagsFromTask(task.getId());
        tagsLabel.setText(Utils.formatTags(tags));
        
        buttonMarkAsDone.setText(Utils.getDoneIndicatorString(task));
        setTaskDisplayColor();
    }
    
    
    /**
     * Sets label color depending on whether currentTask is done or not. 
     */
    public void setTaskDisplayColor() {
        Color color = Utils.getTextFillColor(getCurrentTask());
        taskNameLabel.setTextFill(color);
        taskPriorityLabel.setTextFill(color);
        tagsLabel.setTextFill(color);
    }
    
    
    /**
     * Sort the task list by priority.
     */
    private void sortTaskListByPriority() {
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
     * Sort the task list by tag.
     */
    private void sortTaskListByTag(String query) {
        taskList.clear();
        // lists all tasks associated with a tag that user is searching for
        for (Task task : this.currentProject.getAllTasksByTag(query)) {
            if (!task.isDone()) taskList.add(task.getName(), task);
            if (task.isDone()) taskList.add(task.getName() + " (valmis)", task);
        }
    }
    
    
    /**
     * Sort the task list by name.
     */
    private void sortTaskListByName(String query) {
        taskList.clear(); 
        // lists all tasks with names containing the search query
        for (Task task : this.currentProject.getAllTasks()) {
            if (task.getName().toLowerCase().contains(query)) {
                if (!task.isDone()) taskList.add(task.getName(), task);
                if (task.isDone()) taskList.add(task.getName() + " (valmis)", task);
            }
        }
    }

    
    /**
     * Save.
     */
    private void save() throws StorageException {
        ProjectManager.getInstance().saveCurrentProject();
    }

    
    /**
     * Initialize the GUI.
     * Not used.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //
    }
}
