package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.InvalidEmailException;
import Exceptions.InvalidIntegerException;
import Exceptions.ValidationException;
import application.ConnexionDb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CrudEmployeController extends GlobalController {
		@FXML Button ajouterEmploye;
	    @FXML
	    private Button annulerEmploye;
	    @FXML
	    private TextField email;
	    @FXML
	    private PasswordField mot_de_passe;
	    @FXML
	    private TextField nom;
	    @FXML
	    private TextField phone;
	    @FXML
	    private ComboBox<String> poste;
	    @FXML
	    private TextField prenom;
	    @FXML Label titleEmploye;

	String query = null;
	Connection connection = null ;
    int employeId;
    private boolean update;    
    PreparedStatement preparedStatement = null ;
	private EmployeController EmployeController;
    @FXML
    private void initialize() throws SQLException {
        try {
            connection = ConnexionDb.getConnection();
            ObservableList<String> posteOptions = FXCollections.observableArrayList("Librarian", "Assistant", "Manager");
            poste.setItems(posteOptions);
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
    void setTextField(int employe_id, String nomText, String prenomText, String emailText, String phoneV, String posteV, String mot_de_passeV) {
        employeId = employe_id;
        nom.setText(nomText);
        prenom.setText(prenomText);
        email.setText(emailText);
        phone.setText(String.valueOf(phoneV));
        mot_de_passe.setText(mot_de_passeV); 
        poste.setValue(posteV); 
    }
   
    private boolean isValidInput() throws ValidationException, SQLException {
    	 if ( isNullOrEmpty(nom.getText()) || isNullOrEmpty(prenom.getText()) || isNullOrEmpty(mot_de_passe.getText()) ||
    			 isNullOrEmpty(poste.getValue()) || isNullOrEmpty(email.getText()) || isNullOrEmpty(phone.getText())) {
             throw new ValidationException("Remplir tous les champs");
         }   
         if (!isValidEmail(email.getText())) {
             throw new InvalidEmailException();
         }
         if (!isEmailUnique(email.getText(),"employe", connection)) {
             throw new ValidationException("Email must be unique.");
         }
         try {
             Integer.parseInt(phone.getText());
         } catch (NumberFormatException e) {
             throw new InvalidIntegerException("phone doit etre de type entier");
         }
        return true;
    }
    public void setEmployeController(EmployeController EmployeController) {
        this.EmployeController = EmployeController;
    }
    @FXML
    private void AddUpdateEmploye(MouseEvent event) throws SQLException {
        connection = ConnexionDb.getConnection();     
        try {
            if (!isValidInput()) {
                return; 
            }
        } catch (InvalidEmailException e) {
            afficherAlerte(e.getMessage());
            return;
        } catch (InvalidIntegerException e) {
            afficherAlerte(e.getMessage());
            return;
        } catch (ValidationException e) {
            afficherAlerte(e.getMessage());
            return;
        }
        getQuery();
        if (update) {
            update();
            if (EmployeController != null) {
                EmployeController.refreshTable();
            }        
        } else {
            insert();
            if (EmployeController != null) {
                EmployeController.refreshTable();
            }
        }
        ViderEmploye();
        Stage stage = (Stage) ajouterEmploye.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void ViderEmploye() {
        nom.setText(null);
        prenom.setText(null);
        email.setText(null);
        phone.setText(null);
        poste.setValue(null);
        mot_de_passe.setText(null);
    }
    private void getQuery() {
        if (update == false) { 
            query = "INSERT INTO `employe`( `nom`, `prenom`, `email`, `phone`, `poste`, `mot_de_passe`) VALUES (?,?,?,?,?,?)";
        }else{
            query = "UPDATE `employe` SET "
                    + "`nom`=?,"
                    + "`prenom`=?,"
                    + "`email`=?,"
                    + "`phone`=?,"
                    + "`poste`=?,"
                    + "`mot_de_passe`=? WHERE employe_id = '"+employeId+"'";
        }
    }
    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nom.getText());
            preparedStatement.setString(2, prenom.getText());
            preparedStatement.setString(3, email.getText());
            preparedStatement.setString(4, phone.getText());
            preparedStatement.setString(5, poste.getValue());
            preparedStatement.setString(6, mot_de_passe.getText());
            preparedStatement.execute();
            if (EmployeController != null) {
                EmployeController.refreshTable();
            }
            afficherSuccess("Employe ajouter avec succès");
        } catch (SQLException ex) {
            Logger.getLogger(EmployeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void update() throws SQLException {
        Connection conn = null;
        try {
            conn = ConnexionDb.getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nom.getText());
            preparedStatement.setString(2, prenom.getText());
            preparedStatement.setString(3, email.getText());
            preparedStatement.setString(4, phone.getText());
            preparedStatement.setString(5, poste.getValue());
            preparedStatement.setString(6, mot_de_passe.getText());
            preparedStatement.execute();
            afficherSuccess("Employe modifié avec succès");
        } catch (SQLException ex) {
            Logger.getLogger(EmployeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResources();  
        }
    }
}