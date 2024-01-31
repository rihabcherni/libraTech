package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.ConnexionDb;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CrudEtagereController extends GlobalController {
	   @FXML
	    private TextField Nom_Etage;
	    @FXML
	    private TextField capacite;
	    @FXML Button ajouterEtage;
	    @FXML
	    private Button annulerEtage;
	    @FXML Label titleEtagere;
	String query = null;
	Connection connection = null ;
    int etagereId;
    private boolean update;    
    PreparedStatement preparedStatement = null ;
	private EtagereController etagereController;
    @FXML
    private void initialize() throws SQLException {
        try {
            connection = ConnexionDb.getConnection();
        } finally {
            closeResources();
        }
    }
    private void closeResources() {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void setUpdate(boolean b) {
        this.update = b;
    }
    void setTextField(int etagere_idText, String Nom_EtageText, int capaciteText) {
        etagereId = etagere_idText;
        Nom_Etage.setText(Nom_EtageText);
        capacite.setText(String.valueOf(capaciteText));
    }
    private boolean isValidInput() {
        if (Nom_Etage.getText() == null || capacite.getText() == null) {
        	afficherAlerte("Remplir tous les champs");
            return false;
        }
        if (Nom_Etage.getText().isEmpty() || capacite.getText().isEmpty()) {
        	afficherAlerte("Remplir tous les champs");
            return false;
        }
        try {
            Integer.parseInt(capacite.getText()); 
        } catch (NumberFormatException e) {
        	afficherAlerte("capacite doit etre de type entier");
            return false;
        }
        return true;
    }
    public void setEtagereController(EtagereController etagereController) {
        this.etagereController = etagereController;
    }
    @FXML
    private void AddUpdateEtagere(MouseEvent event) throws SQLException {
        connection = ConnexionDb.getConnection();     
        if (!isValidInput()) {
            return; 
        }
        getQuery();
        if (update) {
            update();
            if (etagereController != null) {
            	etagereController.refreshTable();
            }        
        } else {
            insert();
            if (etagereController != null) {
            	etagereController.refreshTable();
            }
        }
        ViderEtagere();
        Stage stage = (Stage) ajouterEtage.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void ViderEtagere() {
    	Nom_Etage.setText(null);
        capacite.setText(null);
    }
    private void getQuery() {
        if (update == false) { 
            query = "INSERT INTO `etagere`( `Nom_Etage`, `capacite`, `quantite_instantanee`) VALUES (?,?,?)";
        }else{
            query = "UPDATE `etagere` SET "
                    + "`Nom_Etage`=?,"
                    + "`capacite`=? WHERE etagere_id = '"+etagereId+"'";
        }
    }
    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Nom_Etage.getText());
            preparedStatement.setString(2, capacite.getText());
            preparedStatement.setString(3, "0");
            preparedStatement.execute();
            if (etagereController != null) {
            	etagereController.refreshTable();
            }
            afficherSuccess("Etagere ajouter avec succès");
        } catch (SQLException ex) {
            afficherAlerte("Erreur d'ajout d'Etagere");
            Logger.getLogger(EtagereController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void update() throws SQLException {
        Connection conn = null;
        try {
            conn = ConnexionDb.getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, Nom_Etage.getText());
            preparedStatement.setString(2, capacite.getText());
            preparedStatement.execute();
            afficherSuccess("Etagere modifié avec succès");
        } catch (SQLException ex) {
            afficherAlerte("Erreur d'update d'Etagere");
            Logger.getLogger(EtagereController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResources();  
        }
    }
}