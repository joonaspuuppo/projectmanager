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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.5 Mar 11, 2021
 * Controller of the start window.
 */
public class PhtStartGUIController implements Initializable {

    @FXML
    private Button buttonOpenProject;
    
    @FXML
    private ListChooser<String> listChooser;
    
    /**
     * Load all stored project names to the UI.
     */
    public void loadProjects() {
        String[] rows = ProjectManager.getInstance().listAllProjects(); 
        listChooser.setRivit(rows);
    }
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
    }
    
    
    @FXML private void handleCreateNewProject() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Uusi projekti");
        dialog.setHeaderText("Anna uuden projektin nimi");
        dialog.setContentText("Projektin nimi:");
        Optional<String> answer = dialog.showAndWait();
        
        String projectName = answer.isPresent() ? answer.get() : null;
        if (projectName == null) {
            return; 
        }
        ProjectManager pm = ProjectManager.getInstance();
        try {
            Project project = pm.createNewProject(projectName);
            openProjectToMainWindow(project);
        } catch (IllegalArgumentException e) {
            displayError("Virheellinen nimi projektille");
        } catch (IOException e) {
            //raised only when the .fxml file isn't found
            displayError("Odottamaton virhe...");
        }
    }
    
    
    @FXML private void handleOpenProject() {
        String projectName = listChooser.getSelectedText();
        if (projectName == null) {
            displayError("Ole hyvä ja valitse avattava projekti.");
            return;   
        }
        Project project = ProjectManager.getInstance().openProject(projectName);
        try {
            openProjectToMainWindow(project);
        } catch (IOException e) {
            //raised only when the .fxml file isn't found
            displayError("Odottamaton virhe...");
        }
    }
    
    
    /**
     * Switch to the main window and set the current Project instance to be modified.
     * @param p The Project instance to be opened.
     * @throws IOException if the .fxml file isn't found.
     */
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
    
    
    private void displayError(String info) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Virhe");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }
}
