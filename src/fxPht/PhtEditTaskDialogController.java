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
 * @version Mar 11, 2021
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
    private Task task;
    private Boolean confirmChanges = false;
    
    
    /**
     * Sets current project
     * @param p current project
     */
    public void setCurrentProject(Project p) {
        this.currentProject = p;
    }
    
    /**
     * Sets task that is edited
     * @param t task
     */
    public void setTask(Task t) {
        this.task = t;
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
    
    /**
     * Sets confirmChanges
     * @param value boolean value
     */
    public void setconfirmChanges(Boolean value) {
        this.confirmChanges = value;
    }
    
    

    @Override
    public Task getResult() {
        Project p = this.getCurrentProject();
        Task t = this.task;
        if (this.confirmChanges == false) return t;
        
        // renaming task
        t.rename(editedTaskNameField.getText());
        
        // removing old tags
        while(!p.getTagsFromTask(t.getId()).isEmpty()) {
            p.removeTagFromTask(p.getTagsFromTask(t.getId()).get(0).getName(), t);
        }
        
        // adding new tags
        List<Tag> newTags = p.readTagsFromString(editedTagsField.getText());
        for (Tag tag : newTags) {
            p.addTagToTask(tag.getName(), t);
        }
        
        // updating priority
        if (taskPriorityChooser.getSelectedIndex() == 2) {
            t.setPriority(Priority.LOW);
        } else if (taskPriorityChooser.getSelectedIndex() == 1) {
            t.setPriority(Priority.MEDIUM);
        } else {
            t.setPriority(Priority.HIGH);
        }
        
        return t;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        List<Tag> tags = this.getCurrentProject().getTagsFromTask(this.task.getId());
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
     * Opens a modal dialog for editing a task
     * @param modalityStage -
     * @param task the task that is edited
     * @param project current project
     * @return - 
     */
    public static Task editTask(Stage modalityStage, Task task, Project project) {
        return ModalController.<Task, PhtEditTaskDialogController>showModal(
                PhtEditTaskDialogController.class.getResource("PhtEditTaskDialogView.fxml"),
                "Muokkaa tehtävää", modalityStage, task, ctrl -> ctrl.setCurrentProject(project));
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }


}
