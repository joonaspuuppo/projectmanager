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
			Scene mainWindow = new PhtScene(root);
			
			BorderPane root2 = (BorderPane)FXMLLoader.load(getClass().getResource("PhtStartGUIView.fxml"));
            Scene startWindow = new Scene(root2);
            
			mainWindow.getStylesheets().add(getClass().getResource("pht.css").toExternalForm());
			startWindow.getStylesheets().add(getClass().getResource("pht.css").toExternalForm());
			
			primaryStage.setScene(startWindow);
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
