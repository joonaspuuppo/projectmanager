package fxPht;


import dataPht.Task;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.stage.Stage;

/**
 * @author Joonas Puuppo
 * @version Mar 3, 2021
 *
 */
public class PhtEditTaskDialogController implements ModalControllerInterface<String> {
   

    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }
    
    
    
    /**
     * Opens a modal dialog for editing a task
     * @param modalityStage -
     * @param task the task that is edited
     * @return - 
     */
    public static Task editTask(Stage modalityStage, Task task) {
        return ModalController.showModal(
                PhtEditTaskDialogController.class.getResource("PhtEditTaskDialogView.fxml"),
                "Muokkaa tehtävää", modalityStage, task);
    }
}
