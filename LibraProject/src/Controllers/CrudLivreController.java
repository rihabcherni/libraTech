package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.ConnexionDb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CrudLivreController extends GlobalController {
	   @FXML
	    private TextField CodeISBN;
	    @FXML Button ajouterLivre;
	    @FXML
	    private Button annulerLivre;
	    @FXML Label titleLivre;
	    @FXML
	    private TextField auteur;
	    @FXML
	    private TextField categorie;
	    @FXML
	    private ComboBox<String> etagere_id;
	    @FXML
	    private TextField titre;
	String query = null;
	Connection connection = null ;
    int livreId;
    private boolean update;    
    PreparedStatement preparedStatement = null ;
	private LivreController livreController;
    @FXML
    private void initialize() throws SQLException {
        try {
            connection = ConnexionDb.getConnection();
            populateEtagereComboBox();
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
    void setTextField(int code, String titreText, String auteurText, String categorieText, int etagere, int codeISBN) {
        livreId = code;
        titre.setText(titreText);
        auteur.setText(auteurText);
        categorie.setText(categorieText);
        etagere_id.setValue(String.valueOf(etagere));
        CodeISBN.setText(String.valueOf(codeISBN)); 
    }
    private void populateEtagereComboBox() throws SQLException {
        connection = ConnexionDb.getConnection();
        if (connection != null) {
            try {
                String query = "SELECT * FROM etagere"; 
                preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                ObservableList<String> etagereList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    String etagereId = resultSet.getString("etagere_id");
                    etagereList.add(etagereId);
                }
                etagere_id.setItems(etagereList);
            } catch (SQLException ex) {
                Logger.getLogger(CrudLivreController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
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
        }
    }
    private boolean isValidInput() {
        if (titre.getText() == null || auteur.getText() == null || categorie.getText() == null || etagere_id.getValue() == null || CodeISBN.getText() == null) {
        	afficherAlerte("Remplir tous les champs");
            return false;
        }
        if (titre.getText().isEmpty() || auteur.getText().isEmpty() || categorie.getText().isEmpty()|| etagere_id.getValue().isEmpty() || CodeISBN.getText().isEmpty()) {
        	afficherAlerte("Remplir tous les champs");
            return false;
        }
        try {
            Integer.parseInt(CodeISBN.getText()); 
        } catch (NumberFormatException e) {
        	afficherAlerte("CodeISBN doit etre de type entier");
            return false;
        }
        return true;
    }
    public void setLivreController(LivreController livreController) {
        this.livreController = livreController;
    }
    @FXML
    private void AddUpdateLivre(MouseEvent event) throws SQLException {
        connection = ConnexionDb.getConnection();     
        if (!isValidInput()) {
            return; 
        }
        getQuery();
        if (update) {
            update();
            if (livreController != null) {
                livreController.refreshTable();
            }        
        } else {
            insert();
            if (livreController != null) {
                livreController.refreshTable();
            }
        }
        ViderLivre();
        Stage stage = (Stage) ajouterLivre.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void ViderLivre() {
        titre.setText(null);
        auteur.setText(null);
        CodeISBN.setText(null);
        etagere_id.setValue(null);
        categorie.setText(null);
    }
    private void getQuery() {
        if (update == false) { 
            query = "INSERT INTO `livre`( `etagere_id`, `titre`, `auteur`, `codeISBN`, `categorie`, `Disponibilite`, `Date_ajout`) VALUES (?,?,?,?,?,?,?)";
        }else{
            query = "UPDATE `livre` SET "
                    + "`etagere_id`=?,"
                    + "`titre`=?,"
                    + "`auteur`=?,"
                    + "`codeISBN`=?,"
                    + "`categorie`=? WHERE code = '"+livreId+"'";
        }
    }
    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, etagere_id.getValue());
            preparedStatement.setString(2, titre.getText());
            preparedStatement.setString(3, auteur.getText());
            preparedStatement.setString(4, CodeISBN.getText());
            preparedStatement.setString(5, categorie.getText());
            preparedStatement.setString(6, "1");
            preparedStatement.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.execute();
            if (livreController != null) {
                livreController.refreshTable();
            }
            afficherSuccess("Livre ajouter avec succès");
        } catch (SQLException ex) {
            Logger.getLogger(LivreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void update() throws SQLException {
        Connection conn = null;
        try {
            conn = ConnexionDb.getConnection();
            populateEtagereComboBox();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, etagere_id.getValue());
            preparedStatement.setString(2, titre.getText());
            preparedStatement.setString(3, auteur.getText());
            preparedStatement.setString(4, CodeISBN.getText());
            preparedStatement.setString(5, categorie.getText());
            preparedStatement.execute();
            afficherSuccess("Livre modifié avec succès");
        } catch (SQLException ex) {
            Logger.getLogger(LivreController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResources();  
        }
    }
}