package fxPht;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dataPht.Priority;
import dataPht.Project;
import dataPht.Tag;
import dataPht.Task;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.RadioButtonChooser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.6 Apr 1, 2021
 */
public class PhtEditTaskDialogController implements ModalControllerInterface<Task>, Initializable {
   
    @FXML
    private TextField editedTaskNameField;

    @FXML
    private TextField editedTagsField;
    
    @FXML
    private Button confirmChangesButton;

    @FXML
    private RadioButtonChooser<Priority> taskPriorityChooser;
    
    private Project currentProject;
    private Task editedTask;
    private Boolean confirmChanges = false;
    
    
    /**
     * Sets current project
     * @param p current project
     */
    public void setCurrentProject(Project p) {
        this.currentProject = p;
    }
    
    /**
     * Sets editedTask that is edited
     * @param t editedTask
     */
    public void setTask(Task t) {
        this.editedTask = t;
    }
    
    /**
     * Getter for current project
     * @return current project
     */
    public Project getCurrentProject() {
        return this.currentProject;
    } 
    

    @FXML
    private void handleConfirmChanges() {
        setconfirmChanges(true);
        ModalController.closeStage(confirmChangesButton);
    }
    
    @FXML
    private void handleCancel() {
        setconfirmChanges(false);
        ModalController.closeStage(confirmChangesButton);
    }
    

    @Override
    public Task getResult() {
        Project project = this.getCurrentProject();
        Task task = this.editedTask;
        if (this.confirmChanges == false) return task;
        
        String newTaskName = editedTaskNameField.getText();
        task.rename(newTaskName);
        
        clearTagsFromTask(project, task);
        addNewTags(project, task);
        updateTaskPriority(task);
        return task;
    }

    
    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        List<Tag> tags = this.getCurrentProject().getTagsFromTask(this.editedTask.getId());
        editedTagsField.setText(this.getCurrentProject().getTagsAsString(tags));
        
    }

    
    @Override
    public void setDefault(Task task) {
        this.setTask(task);
        editedTaskNameField.setText(task.getName());
        
        if (task.getPriority() == Priority.LOW) {
            taskPriorityChooser.setSelectedIndex(2);
        } else if (task.getPriority() == Priority.MEDIUM) {
            taskPriorityChooser.setSelectedIndex(1);
        } else {
            taskPriorityChooser.setSelectedIndex(0);
        }
    }
    
    
    /**
     * Sets confirmChanges
     * @param value boolean value
     */
    public void setconfirmChanges(Boolean value) {
        this.confirmChanges = value;
    }
    
    
    private void clearTagsFromTask(Project p, Task task) {
        int taskId = task.getId();
        List<Tag> tagList = p.getTagsFromTask(taskId);
        while(!tagList.isEmpty()) {
            Tag tag = tagList.remove(0);
            p.removeTagFromTask(tag.getName(), task);
        }
    }
    
    
    private void addNewTags(Project p, Task t) {
        String tagString = editedTagsField.getText();
        List<Tag> newTags = p.readTagsFromString(tagString);
        for (Tag tag : newTags) {
            p.addTagToTask(tag.getName(), t);
        }
    }
    
    
    private void updateTaskPriority(Task t) {
        Priority[] indexToPriority = {
                Priority.HIGH,
                Priority.MEDIUM,
                Priority.LOW
            };
        int i = taskPriorityChooser.getSelectedIndex();
        Priority prio = indexToPriority[i];
        t.setPriority(prio);
    }
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Opens a modal dialog for editing a task
     * @param modalityStage -
     * @param task the task that is edited
     * @param project current project
     * @return - 
     */
    public static Task editTask(Stage modalityStage, Task task, Project project) {
        URL url = PhtEditTaskDialogController.class.getResource("PhtEditTaskDialogView.fxml");
        String title = "Muokkaa tehtävää"; 
        return ModalController.<Task, PhtEditTaskDialogController>showModal(
                url,
                title,
                modalityStage,
                task,
                ctrl -> ctrl.setCurrentProject(project)
            );
    }


}
