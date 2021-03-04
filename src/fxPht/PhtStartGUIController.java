package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
 *
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
        System.out.println(answer.isPresent() ?
           answer.get() : "Ei ollut vastausta");
    }
    
    /**
     * Käsitellään projektin avaaminen
     * @throws IOException 
     */
    @FXML private void handleOpenProject() throws IOException {
        //Dialogs.showMessageDialog("Ei osata vielä avata valittua projektia");
        Stage primaryStage = (Stage) buttonOpenProject.getScene().getWindow();
        BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("PhtGUIView.fxml"));
        PhtScene mainWindow = new PhtScene(root);
        String projectName = listChooser.getSelectedText();
        mainWindow.setProject(projectName);
        primaryStage.setScene(mainWindow);
    }
    
    

}
