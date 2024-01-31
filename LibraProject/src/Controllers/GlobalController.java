package Controllers;

import javafx.application.Application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GlobalController extends Application{
    protected static void afficherAlerte(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("LibraTech-Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    protected static void afficherAlerte(String titre, String contenu, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
    protected static void afficherSuccess(String message) {
    	 Stage customAlert = new Stage();
         customAlert.initStyle(StageStyle.UNDECORATED);

         Label label = new Label(message);
         label.setStyle("-fx-text-fill: green; -fx-font-size: 20;; -fx-font-weight: bold;");

         StackPane stackPane = new StackPane(label);
         stackPane.setStyle("-fx-background-color: #dff0d8; -fx-padding: 20;");

         customAlert.setScene(new Scene(stackPane, 400, 300));
         customAlert.show();

         Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> customAlert.close()));
         timeline.play();
    }
    protected boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    protected boolean isEmailUnique(String email,String tableName, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM "+tableName+" WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
            }
        }
        return false; 
    }

    protected boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
	@Override
	public void start(@SuppressWarnings("exports") Stage stage) throws Exception {
       
	}
}