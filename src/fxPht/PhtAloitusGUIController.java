package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
     * K�sitell��n uuden projektin lis��minen
     */
    @FXML private void handleLisaaProjekti() throws IOException {
        //Dialogs.showMessageDialog("Ei osata viel� lis�t� uutta projektia");
        Stage primaryStage = (Stage) aloitusAvaaProjekti.getScene().getWindow();
        BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("PhtGUIView.fxml"));
        Scene paaikkuna = new Scene(root);
        primaryStage.setScene(paaikkuna);
    }
    
    /**
     * K�sitell��n projektin avaaminen
     * @throws IOException 
     */
    @FXML private void handleAvaaProjekti() throws IOException {
        //Dialogs.showMessageDialog("Ei osata viel� avata valittua projektia");
        Stage primaryStage = (Stage) aloitusAvaaProjekti.getScene().getWindow();
        BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("PhtGUIView.fxml"));
        Scene paaikkuna = new Scene(root);
        primaryStage.setScene(paaikkuna);
    }
    
    

}
