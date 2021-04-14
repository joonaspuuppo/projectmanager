package fxPht;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


/**
 * Main entrypoint to the application.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 1, 2021
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi
 */
public class PhtMain extends Application {
	
    
    /**
     * Main entry to the application.
     * @param args Not in use.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Launch the GUI.
     */
    @Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader startWindowLoader = new FXMLLoader();
		    startWindowLoader.setLocation(getClass().getResource("PhtStartGUIView.fxml"));
	        
	        BorderPane startPane = (BorderPane)startWindowLoader.load();
	        Scene startWindow = new Scene(startPane);
	        PhtStartGUIController startWindowController = (PhtStartGUIController) startWindowLoader.getController();
	        startWindowController.loadProjects();
	        
			BorderPane mainPane = (BorderPane)FXMLLoader.load(getClass().getResource("PhtGUIView.fxml"));
			Scene mainWindow = new Scene(mainPane);
            
			mainWindow.getStylesheets().add(getClass().getResource("pht.css").toExternalForm());
			startWindow.getStylesheets().add(getClass().getResource("pht.css").toExternalForm());
			
			primaryStage.setScene(startWindow);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
