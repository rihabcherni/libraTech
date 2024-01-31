package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.ConnexionDb;
import application.Links;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginController  extends GlobalController{
	   @FXML
	    private TextField email;

	    @FXML
	    private PasswordField mot_de_passe;

	    @FXML
	    private Button connexion;
	    @FXML
	    private Button annulerConnexion;
	    public int connectedId;

	    @FXML
	    private void initialize() {
	    }

	    @FXML
	    private void handleConnexion(ActionEvent event) throws IOException {
	        String emailValue = email.getText();
	        String motDePasseValue = mot_de_passe.getText();
	        try {
		       	Connection connection=ConnexionDb.getConnection();
	            String query = "SELECT * FROM employe WHERE email=? AND mot_de_passe=?";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                preparedStatement.setString(1, emailValue);
	                preparedStatement.setString(2, motDePasseValue);
	                ResultSet resultSet = preparedStatement.executeQuery();
	                if (resultSet.next()) {
	                	 String nom = resultSet.getString("nom");
	                     String prenom = resultSet.getString("prenom");
	                
	                     openDashboard(nom, prenom);
	                } else {
	                    afficherAlerte("Échec de la connexion. Vérifiez vos informations d'identification.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    private void openDashboard(String nom ,String prenom) {
	        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Links.sidebarLink));
	            Parent dashboard = loader.load();
	            Parent root = null;
	            SidebarController dashboardController = loader.getController();
	            dashboardController.setNomPrenom(nom, prenom);
	         
                root = FXMLLoader.load(this.getClass().getResource(Links.dashboardLink));
                dashboardController.linkPage.setText("Dashboard");
                dashboardController.page.getChildren().setAll(root);
	            Stage dashboardStage = new Stage();
				Image icon = new Image(getClass().getResourceAsStream("/images/libratech.PNG"));
				dashboardStage.setResizable(false);
				dashboardStage.getIcons().add(icon);
				dashboardStage.setTitle("LibraTech-Dashboard");
	            dashboardStage.setScene(new Scene(dashboard));
	            dashboardStage.show();
                afficherSuccess("Connexion réussie !\nNom: " + nom + "\nPrénom: " + prenom);
	            Stage loginStage = (Stage) email.getScene().getWindow();
	            loginStage.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    @FXML
	    private void cleanLogin() {
		    email.setText(null);
		    mot_de_passe.clear();	        
	    }
	@SuppressWarnings("exports")
	@Override
	public void start(Stage arg0) throws Exception {
		
	}
}
