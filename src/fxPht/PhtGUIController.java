package fxPht;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
        Dialogs.showMessageDialog("Ei osata viel‰ poistaa");
    }
     
    
    /**
     * K‰sitell‰‰n tallennusk‰sky
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    /**
     * K‰sitell‰‰n tallennus nimell‰
     */
    @FXML private void handleTallennaNimella() {
        tallenna();
    }
    
    
    /**
     * K‰sitell‰‰n projektin poistaminen
     */
    @FXML private void handleAvaaProjekti() {
        Dialogs.showMessageDialog("Ei osata viel‰ avata");
    }
    
    
    /**
     * K‰sitell‰‰n projektin luominen
     */
    @FXML private void handleLuoProjekti() {
        Dialogs.showMessageDialog("Ei osata viel‰ luoda uutta projektia");
    }
    
    /**
     * K‰sitell‰‰n projektin nime‰minen uudelleen
     */
    @FXML private void handleNimeaUudelleen() {
        Dialogs.showMessageDialog("Ei osata viel‰ nimet‰ uudelleen");
    }
    
    /**
     * K‰sitell‰‰n projektin avaaminen
     */
    @FXML private void handlePoistaProjekti() {
        Dialogs.showMessageDialog("Ei osata viel‰ poistaa projektia");
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
