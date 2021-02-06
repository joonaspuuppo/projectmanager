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
 * @author joona
 * @version Jan 29, 2021
 *
 */
public class PhtAloitusGUIController implements Initializable {

    @FXML
    private Button aloitusAvaaProjekti;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Käsitellään uuden projektin lisääminen
     */
    @FXML private void handleLisaaProjekti() {
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
    @FXML private void handleAvaaProjekti() throws IOException {
        //Dialogs.showMessageDialog("Ei osata vielä avata valittua projektia");
        Stage primaryStage = (Stage) aloitusAvaaProjekti.getScene().getWindow();
        BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("PhtGUIView.fxml"));
        Scene paaikkuna = new Scene(root);
        primaryStage.setScene(paaikkuna);
    }
    
    

}
