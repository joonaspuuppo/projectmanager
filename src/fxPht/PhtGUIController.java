package fxPht;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextInputDialog;

/**
 * @author Joonas Puuppo
 * @version Jan 21, 2021
 *
 */
public class PhtGUIController implements Initializable {

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * K‰sitell‰‰n uuden teht‰v‰n lis‰‰minen
     */
    @FXML private void handleLisaaTehtava() {
        Dialogs.showMessageDialog("Ei osata viel‰ lis‰t‰");
    }
    
    /**
     * K‰sitell‰‰n teht‰v‰n merkitseminen valmiiksi
     */
    @FXML private void handleMerkitseValmiiksi() {
        Dialogs.showMessageDialog("Ei osata viel‰ merkit‰ valmiiksi");
    }
    
    /**
     * K‰sitell‰‰n teht‰v‰n poistaminen
     */
    @FXML private void handlePoistaTehtava() {
        Dialogs.showQuestionDialog("Poisto?", "Poistetaanko teht‰v‰?", "Kyll‰", "Ei");
    }
     
    
    /**
     * K‰sitell‰‰n tallennusk‰sky
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    /**
     * K‰sitell‰‰n projektin poistaminen
     */
    @FXML private void handleAvaaProjekti() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Avaa projekti");
        dialog.setHeaderText("Mik‰ projekti avataan");
        dialog.setContentText("Projektin nimi:");
        Optional<String> answer = dialog.showAndWait();
        System.out.println(answer.isPresent() ?
                answer.get() : "Ei ollut vastausta");
        // V‰liaikainen ehtolause, jotta saadaan testattua dialogit
        if (!answer.get().equals("projekti1")) {
            Dialogs.showMessageDialog("Projektia ei lˆydetty");
        }
    }
    
    
    /**
     * K‰sitell‰‰n projektin luominen
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
     * K‰sitell‰‰n projektin nime‰minen uudelleen
     */
    @FXML private void handleNimeaUudelleen() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nime‰ uudelleen");
        dialog.setHeaderText("Anna projektin uusi nimi");
        dialog.setContentText("Uusi nimi:");
        Optional<String> answer = dialog.showAndWait();
        System.out.println(answer.isPresent() ?
           answer.get() : "Ei ollut vastausta");
    }
    
    /**
     * K‰sitell‰‰n projektin poistaminen
     */
    @FXML private void handlePoistaProjekti() {
        Dialogs.showQuestionDialog("Poisto?", "Haluatko varmasti poistaa projektin?", "Kyll‰", "Ei");
    }
    
    
    /**
     * K‰sitell‰‰n lopetusk‰sky
     */
    @FXML private void handleLopeta() {
        tallenna();
        Platform.exit();
    }

    
    /**
     * Tietojen tallennus
     */
    private void tallenna() {
        Dialogs.showMessageDialog("Tallennetetaan! Mutta ei toimi viel‰");
    }



}
