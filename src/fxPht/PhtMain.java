package fxPht;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version Jan 21, 2021
 *
 */
public class PhtMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("PhtGUIView.fxml"));
			Scene paaikkuna = new Scene(root);
			
			BorderPane root2 = (BorderPane)FXMLLoader.load(getClass().getResource("PhtAloitusGUIView.fxml"));
            Scene aloitusikkuna = new Scene(root2);
            
			paaikkuna.getStylesheets().add(getClass().getResource("pht.css").toExternalForm());
			aloitusikkuna.getStylesheets().add(getClass().getResource("pht.css").toExternalForm());
			
			primaryStage.setScene(aloitusikkuna);
			primaryStage.show();
			
			// jos klikataan avaa tai luo projekti, avataan pääikkuna
			
		
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args ei käytässä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
