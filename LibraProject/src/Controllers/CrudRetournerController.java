package Controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ValidationException;
import application.ConnexionDb;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CrudRetournerController extends GlobalController {
    @FXML
    private TextField CodeISBN;
    @FXML
    private Button ajouterRetour;
    @FXML
    private Button annulerRetour;
    @FXML
    private TextField auteur;
    @FXML
    private TextField categorie;
    @FXML
    private TextField adresse;
    @FXML
    private TextField cinLecteur;
    @FXML
    private DatePicker dateEmprunt;
    @FXML
    private DatePicker dRP;
    @FXML
    private DatePicker dRReel;
    @FXML
    private TextField email;
    @FXML
    private TextField etagere_id;
    @FXML
    private TextField livreId;
    @FXML
    private TextField nom;
    @FXML
    private TextField phone;
    @FXML
    private TextField prenom;
    @FXML
    private TextField titre;
    @FXML
    private Button rechercheLecteur;
    @FXML
    private Button rechercheLivre;
    String query = null;
    Connection connection = null;
    int retourId;
    PreparedStatement preparedStatement = null;
    private EmpruntController empruntController;

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

    @FXML
    void rechLecteur(MouseEvent event) {
        String cin = cinLecteur.getText();
        searchLecteur(cin);
    }

    @FXML
    void rechLivre(MouseEvent event) {
        String livreCode = livreId.getText();
        searchLivre(livreCode);
    }
    void setTextField(int details_emprunts_id, int lcinText, int Livre_idText, Date Date_EmpruntText, Date Date_Retour_Prevu) {
    	retourId = details_emprunts_id;
        cinLecteur.setText(String.valueOf(lcinText));
        livreId.setText(String.valueOf(Livre_idText));
        dateEmprunt.setValue(LocalDate.parse(Date_EmpruntText.toString()));
        dRP.setValue(LocalDate.parse(Date_Retour_Prevu.toString()));
    }

    private boolean isValidInput() throws ValidationException {
        LocalDate empruntDate = dateEmprunt.getValue();
        LocalDate retourReelDate = dRReel.getValue();
        if (retourReelDate.isBefore(empruntDate)) {
            throw new ValidationException("La date de retour reel ne peut pas être antérieure à la date d'emprunt");
        }
        return true;
    }
    public void setEmpruntController(EmpruntController empruntController) {
        this.empruntController = empruntController;
    }

    @FXML
    private void AddUpdateRetour(MouseEvent event) throws SQLException, ValidationException {
        connection = ConnexionDb.getConnection();
        try {
            if (!isValidInput()) {
                return;
            }
        } catch (ValidationException e) {
            afficherAlerte(e.getMessage());
            return;
        }
        LocalDate retourReelDate = dRReel.getValue();
        LocalDate retourPrevuDate = dRP.getValue();

        if (retourReelDate.isAfter(retourPrevuDate)) {
            long daysOfDelay = ChronoUnit.DAYS.between(retourPrevuDate, retourReelDate);
            afficherAlerte("Retard de retour de livre. Nombre de jours de retard : " + daysOfDelay);
        }

         update();
         if (empruntController != null) {
                empruntController.refreshTable();
         }
         ViderRetour();
         Stage stage = (Stage) ajouterRetour.getScene().getWindow();
         stage.close();
    }

    private void clearLivreFields() {
        CodeISBN.setText(null);
        etagere_id.setText(null);
        titre.setText(null);
        auteur.setText(null);
        categorie.setText(null);
    }

    private void clearLecteurFields() {
        nom.setText(null);
        prenom.setText(null);
        email.setText(null);
        adresse.setText(null);
        phone.setText(null);
    }

    private void clearEmpruntForm() {
        livreId.setText(null);
        cinLecteur.setText(null);
        dateEmprunt.setValue(null);
        dRP.setValue(null);
    }

    @FXML
    private void ViderRetour() {
        clearLivreFields();
        clearLecteurFields();
        clearEmpruntForm();
    }

  
    private void updateDisponible(int livreIdValue) throws SQLException {
        String updateQuery = "UPDATE livre SET Disponibilite = 1  WHERE code = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setInt(1, livreIdValue);
            updateStatement.executeUpdate();
            System.out.println("update "+livreIdValue);
        }
    }

    private void update() throws SQLException {
        try {
            int livreIdValue = Integer.parseInt(livreId.getText());
            connection = ConnexionDb.getConnection(); 
            String updateQuery = "UPDATE details_emprunt SET date_retour_reel=? WHERE details_emprunts_id = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDate(1, Date.valueOf(dRReel.getValue()));
            preparedStatement.setInt(2, retourId);
            preparedStatement.execute();
            updateDisponible(livreIdValue);
            afficherSuccess("Emprunt modifié avec succès");
        } catch (SQLException ex) {
            Logger.getLogger(EmpruntController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResources();
        }
    }


    private void searchLecteur(String cin) {
        try (Connection connection = ConnexionDb.getConnection()) {
            String query = "SELECT * FROM lecteur WHERE cin = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, cin);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        nom.setText(resultSet.getString("nom"));
                        prenom.setText(resultSet.getString("prenom"));
                        email.setText(resultSet.getString("email"));
                        adresse.setText(resultSet.getString("Adresse"));
                        phone.setText(resultSet.getString("phone"));
                    } else {
                        clearLecteurFields();
                        afficherAlerte("Lecteur n'existe pas!");
                    }
                }
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur sql!");
            e.printStackTrace();
        }
    }

    private void searchLivre(String livreCode) {
        try (Connection connection = ConnexionDb.getConnection()) {
            String query = "SELECT * FROM livre WHERE code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, livreCode);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        titre.setText(resultSet.getString("titre"));
                        auteur.setText(resultSet.getString("auteur"));
                        CodeISBN.setText(resultSet.getString("CodeISBN"));
                        categorie.setText(resultSet.getString("categorie"));
                        etagere_id.setText(resultSet.getString("etagere_id"));

                    } else {
                        clearLivreFields();
                        afficherAlerte("Livre n'existe pas!");
                    }
                }
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur sql!");
            e.printStackTrace();
        }
    }

}