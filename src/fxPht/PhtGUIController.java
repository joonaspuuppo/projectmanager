package fxPht;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version Jan 21, 2021
 *
 */
public class PhtGUIController implements Initializable {

    @FXML
    private MenuItem menuAvaaProjekti;
    
    @FXML
    private Button buttonLisaaTehtava;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Käsitellään uuden tehtävän lisääminen
     */
    @FXML private void handleLisaaTehtava() {
        Dialogs.showMessageDialog("Ei osata vielä lisätä");
    }
    
    /**
     * Käsitellään tehtävän merkitseminen valmiiksi
     */
    @FXML private void handleMerkitseValmiiksi() {
        Dialogs.showMessageDialog("Ei osata vielä merkitä valmiiksi");
    }
    
    /**
     * Käsitellään tehtävän poistaminen
     */
    @FXML private void handlePoistaTehtava() {
        Dialogs.showQuestionDialog("Poisto?", "Poistetaanko tehtävä?", "Kyllä", "Ei");
    }
     
    
    /**
     * Käsitellään tallennuskäsky
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    /**
     * Käsitellään projektin avaaminen
     * @throws IOException 
     */
    @FXML private void handleAvaaProjekti() throws IOException {
        // TODO: Tallennus ennen siirtymistä aloitukseen!
        Stage primaryStage = (Stage) buttonLisaaTehtava.getScene().getWindow();
        BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("PhtAloitusGUIView.fxml"));
        Scene aloitusikkuna = new Scene(root);
        primaryStage.setScene(aloitusikkuna);
        }
        
    
    /**
     * Käsitellään projektin luominen
     */
    @FXML private void handleLuoProjekti() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Uusi projekti");
        dialog.setHeaderText("Anna uuden projektin nimi");
        dialog.setContentText("Projektin nimi:");
        Optional<String> answer = dialog.showAndWait();
        System.out.println(answer.isPresent() ?
           answer.get() : "Ei ollut vastausta");
    }
    
    /**
     * Käsitellään projektin nimeäminen uudelleen
     */
    @FXML private void handleNimeaUudelleen() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nimeä uudelleen");
        dialog.setHeaderText("Anna projektin uusi nimi");
        dialog.setContentText("Uusi nimi:");
        Optional<String> answer = dialog.showAndWait();
        System.out.println(answer.isPresent() ?
           answer.get() : "Ei ollut vastausta");
    }
    
    /**
     * Käsitellään projektin poistaminen
     */
    @FXML private void handlePoistaProjekti() {
        Dialogs.showQuestionDialog("Poisto?", "Haluatko varmasti poistaa projektin?", "Kyllä", "Ei");
    }
    
    
    /**
     * Käsitellään lopetuskäsky
     */
    @FXML private void handleLopeta() {
        tallenna();
        Platform.exit();
    }

    
    /**
     * Tietojen tallennus
     */
    private void tallenna() {
        Dialogs.showMessageDialog("Tallennetetaan! Mutta ei toimi vielä");
    }



}
