package fxPht;

import java.net.URL;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * @author joona
 * @version Jan 29, 2021
 *
 */
public class PhtAloitusGUIController implements Initializable {

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * K‰sitell‰‰n uuden projektin lis‰‰minen
     */
    @FXML private void handleLisaaProjekti() {
        Dialogs.showMessageDialog("Ei osata viel‰ lis‰t‰ uutta projektia");
    }
    
    /**
     * K‰sitell‰‰n projektin avaaminen
     */
    @FXML private void handleAvaaProjekti() {
        Dialogs.showMessageDialog("Ei osata viel‰ avata valittua projektia");
    }
    
    

}
