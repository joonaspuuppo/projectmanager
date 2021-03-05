package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dataPht.Project;
import dataPht.ProjectManager;
import fi.jyu.mit.fxgui.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version Jan 29, 2021
 */
public class PhtStartGUIController implements Initializable {

    @FXML
    private Button buttonOpenProject;
    
    @FXML
    private ListChooser<String> listChooser;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
    }
    
    /**
     * Käsitellään uuden projektin lisääminen
     */
    @FXML private void handleCreateNewProject() {
        //Dialogs.showMessageDialog("Ei osata vielä lisätä uutta projektia");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Uusi projekti");
        dialog.setHeaderText("Anna uuden projektin nimi");
        dialog.setContentText("Projektin nimi:");
        Optional<String> answer = dialog.showAndWait();
        
        String projectName = answer.isPresent() ? answer.get() : null;
        if (projectName == null) {
         // TODO handle errors in the name, display error
        }
        ProjectManager pm = ProjectManager.getInstance();
        try {
            Project project = pm.createNewProject(projectName);
            openProjectToMainWindow(project);
        } catch (IllegalArgumentException e) {
            // TODO handle possible IO exceptions
            e.printStackTrace();
        } catch (IOException e) {
            // TODO handle possible IO exceptions
            e.printStackTrace();
        }
    }
    
    /**
     * Handle opening an existing Project
     */
    @FXML private void handleOpenProject() {
        String projectName = listChooser.getSelectedText();
        Project project = ProjectManager.getInstance().openProject(projectName);
        try {
            openProjectToMainWindow(project);
        } catch (IOException e) {
            // TODO handle possible IO exceptions
            e.printStackTrace();
        }
    }
    
    
    private void openProjectToMainWindow(Project p) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PhtGUIView.fxml"));
        
        BorderPane root = (BorderPane)loader.load();
        Scene mainWindow = new Scene(root);
        PhtGUIController mainWindowController = (PhtGUIController) loader.getController();
        mainWindowController.setCurrentProject(p);
        
        Stage primaryStage = (Stage) buttonOpenProject.getScene().getWindow();
        primaryStage.setScene(mainWindow);
    }
}
