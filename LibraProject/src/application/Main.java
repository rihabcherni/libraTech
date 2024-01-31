package application;
	
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			ProgressIndicator PI=new ProgressIndicator();  
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Links.welcomeLink));
			Parent root = loader.load();
		    if (root instanceof AnchorPane) {
		        PI.setTranslateX(345);
		        PI.setTranslateY(170);
		        PI.setStyle("-fx-progress-color: white;"); 
		    	((AnchorPane) root).getChildren().add(PI);
		    }			
		    Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Image icon = new Image(getClass().getResourceAsStream("/images/libratech.PNG"));
			stage.setResizable(false);
			stage.getIcons().add(icon);
			stage.setTitle("LibraTech");
			stage.setScene(scene);
			stage.show();
			
			Duration delay = Duration.seconds(6);
	        KeyFrame keyFrame = new KeyFrame(delay, event -> loadNewFXML(stage));
	        Timeline timeline = new Timeline(keyFrame);
	        timeline.play();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadNewFXML(Stage primaryStage) {
        try {
            FXMLLoader newLoader = new FXMLLoader(getClass().getResource(Links.loginLink));
            Parent newRoot = newLoader.load();
        	Image icon = new Image(getClass().getResourceAsStream("/images/libratech.PNG"));
        	primaryStage.setResizable(false);
        	primaryStage.getIcons().add(icon);
        	primaryStage.setTitle("LibraTech-Login");
            primaryStage.getScene().setRoot(newRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static void main(String[] args) {
		launch(args);
	}
}
