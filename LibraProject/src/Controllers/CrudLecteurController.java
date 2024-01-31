package Controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CrudLecteurController extends GlobalController {	    
	    @FXML
	    private DatePicker Date_Naissance;
	    @FXML
	    private TextField adresse;
	    @FXML
	    private TextField cin;
	    @FXML
	    private TextField email;
	    @FXML
	    private ComboBox<String> genre;
	    @FXML
	    private TextField nom;
	    @FXML
	    private TextField phone;
	    @FXML
	    private TextField prenom;
	    @FXML Button ajouterLecteur;
	    @FXML
	    private Button annulerLecteur;
	    @FXML Label titleLecteur;   
	    
		String query = null;
		Connection connection = null ;
	    int lecteurId;
	    private boolean update;    
	    PreparedStatement preparedStatement = null ;
		private LecteurController lecteurController;
    @FXML
    private void initialize() throws SQLException {
        try {
            connection = ConnexionDb.getConnection();
            populateLecteurComboBox();
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
    void setTextField(int cinV, String nomText, String prenomText, String AdresseText, String EmailText, String genreText, String phoneV, Date dateNaissance) {
        lecteurId = cinV;
        cin.setText(String.valueOf(cinV));
        nom.setText(nomText);
        prenom.setText(prenomText);
        adresse.setText(AdresseText);
        email.setText(EmailText);
        genre.setValue(genreText);
        phone.setText(String.valueOf(phoneV));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateNaissanceStr = dateFormat.format(dateNaissance);
        Date_Naissance.setValue(LocalDate.parse(dateNaissanceStr));
    }
	private void populateLecteurComboBox() {
	    ObservableList<String> genderOptions = FXCollections.observableArrayList("Feminin", "Masculin");
	    genre.setItems(genderOptions);
	}
    private boolean isValidInput()  throws ValidationException, SQLException {        
        if (isNullOrEmpty(cin.getText()) || isNullOrEmpty(nom.getText()) || isNullOrEmpty(prenom.getText())
                || isNullOrEmpty(genre.getValue()) || Date_Naissance.getValue() == null
                || isNullOrEmpty(adresse.getText()) || isNullOrEmpty(email.getText())
                || isNullOrEmpty(phone.getText())) {
            throw new ValidationException("Remplir tous les champs");
        }   
        if (!isValidEmail(email.getText())) {
            throw new InvalidEmailException();
        }
        if (!isEmailUnique(email.getText(),"lecteur", connection)) {
            throw new ValidationException("Email must be unique.");
        }
        try {
            Integer.parseInt(cin.getText());
        } catch (NumberFormatException e) {
            throw new InvalidIntegerException("cin doit etre de type entier");
        }
        try {
            Integer.parseInt(phone.getText());
        } catch (NumberFormatException e) {
            throw new InvalidIntegerException("phone doit etre de type entier");
        }
        return true;
    }
    public void setLecteurController(LecteurController lecteurController) {
        this.lecteurController = lecteurController;
    }
    @FXML
    private void AddUpdateLecteur(MouseEvent event) throws SQLException {
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
            if (lecteurController != null) {
                lecteurController.refreshTable();
            }        
        } else {
            insert();
            if (lecteurController != null) {
                lecteurController.refreshTable();
            }
        }
        ViderLecteur();
        Stage stage = (Stage) ajouterLecteur.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void ViderLecteur() {
    	cin.setText(null);
    	nom.setText(null);
    	prenom.setText(null);
    	adresse.setText(null);
    	email.setText(null);
    	phone.setText(null);
    	email.setText(null);
    	genre.setValue(null);
    	Date_Naissance.setValue(null);
    }
    private void getQuery() {
        if (update == false) { 
            query = "INSERT INTO `lecteur`( `cin`, `nom`, `prenom`, `Adresse`, `Email`, `Date_Inscription`, `phone`, `Date_Naissance`, `genre`)"
            		+ " VALUES (?,?,?,?,?,?,?,?,?)";
        }else{
            query = "UPDATE `lecteur` SET "
                    + "`cin`=?,"
                    + "`nom`=?,"
                    + "`prenom`=?,"
                    + "`Adresse`=?,"
                    + "`Email`=?,"
                    + "`phone`=?,"
                    + "`Date_Naissance`=? WHERE cin = '"+lecteurId+"'";
        }
    }
    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cin.getText());
            preparedStatement.setString(2, nom.getText());
            preparedStatement.setString(3, prenom.getText());
            preparedStatement.setString(4, adresse.getText());
            preparedStatement.setString(5, email.getText());
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(7, phone.getText());
            preparedStatement.setDate(8, java.sql.Date.valueOf(Date_Naissance.getValue()));
            preparedStatement.setString(9, genre.getValue());
            preparedStatement.execute();
            if (lecteurController != null) {
                lecteurController.refreshTable();
            }
            afficherSuccess("Lecteur ajouter avec succès");
        } catch (SQLException ex) {
            Logger.getLogger(LecteurController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void update() throws SQLException {
        Connection conn = null;
        try {
            conn = ConnexionDb.getConnection();
            populateLecteurComboBox();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, cin.getText());
            preparedStatement.setString(2, nom.getText());
            preparedStatement.setString(3, prenom.getText());
            preparedStatement.setString(4, adresse.getText());
            preparedStatement.setString(5, email.getText());
            preparedStatement.setString(6, phone.getText());
            preparedStatement.setDate(7, java.sql.Date.valueOf(Date_Naissance.getValue()));
            preparedStatement.execute();
            afficherSuccess("Lecteur modifié avec succès");
        } catch (SQLException ex) {
            Logger.getLogger(LecteurController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResources();  
        }
    }
}