package Controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.InvalidIntegerException;
import Exceptions.ValidationException;
import application.ConnexionDb;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CrudEmpruntController extends GlobalController {
    @FXML
    private TextField CodeISBN;
    @FXML
    private Button ajouterEmprunt;
    @FXML
    private Button annulerEmprunt;
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
    int empruntId;
    private boolean update;
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

    void setUpdate(boolean b) {
        this.update = b;
    }

    void setTextField(int codeDetails_emprunts_id, int lcinText, int Livre_idText, Date Date_EmpruntText,
            Date Date_Retour_Prevu) {
        empruntId = codeDetails_emprunts_id;
        cinLecteur.setText(String.valueOf(lcinText));
        livreId.setText(String.valueOf(Livre_idText));
        dateEmprunt.setValue(LocalDate.parse(Date_EmpruntText.toString()));
        dRP.setValue(LocalDate.parse(Date_Retour_Prevu.toString()));
    }

    private boolean isValidInput() throws ValidationException {
        if (isNullOrEmpty(livreId.getText()) || isNullOrEmpty(cinLecteur.getText())
                || dateEmprunt.getValue() == null
                || dRP.getValue() == null) {
            throw new ValidationException("Remplir tous les champs");
        }
        try {
            Integer.parseInt(livreId.getText());
        } catch (NumberFormatException e) {
            afficherAlerte("livre Id doit etre de type entier");
            return false;
        }
        if (!isBookAvailable(livreId.getText())) {
            throw new ValidationException("Le livre n'est pas disponible");
        }
        if(!isLecteur(cinLecteur.getText())){
            throw new ValidationException("Le livre n'est pas disponible");
        }
        try {
            Integer.parseInt(cinLecteur.getText());
        } catch (NumberFormatException e) {
            afficherAlerte("cin lecteur doit etre de type entier");
            return false;
        }
        LocalDate empruntDate = dateEmprunt.getValue();
        LocalDate retourPrevuDate = dRP.getValue();

        if (retourPrevuDate.isBefore(empruntDate)) {
            throw new ValidationException("La date de retour prévue ne peut pas être antérieure à la date d'emprunt");
        }

        return true;
    }

    private boolean isBookAvailable(String livreId) {
        try (Connection connection = ConnexionDb.getConnection()) {
            String query = "SELECT Disponibilite FROM livre WHERE code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, livreId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean("Disponibilite");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean isLecteur(String string) {
        try (Connection connection = ConnexionDb.getConnection()) {
            String query = "SELECT cin FROM lecteur WHERE cin = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, string);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void setEmpruntController(EmpruntController empruntController) {
        this.empruntController = empruntController;
    }

    @FXML
    private void AddUpdateEmprunt(MouseEvent event) throws SQLException {
        connection = ConnexionDb.getConnection();
        try {
            if (!isValidInput()) {
                return;
            }
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
            if (empruntController != null) {
                empruntController.refreshTable();
            }
        } else {
            insert();
            if (empruntController != null) {
                empruntController.refreshTable();
            }
        }
        ViderEmprunt();
        Stage stage = (Stage) ajouterEmprunt.getScene().getWindow();
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
    private void ViderEmprunt() {
        clearLivreFields();
        clearLecteurFields();
        clearEmpruntForm();
    }

    private void getQuery() {
        if (update == false) {
            query = "INSERT INTO `details_emprunt`( `lecteur_cin`,`livre_id`,  `Date_Emprunt`, `Date_Retour_Prevu` , `date_retour_reel`) VALUES (?,?,?,?,?)";
        } else {
            query = "UPDATE `details_emprunt` SET "
                    + "`lecteur_cin`=?,"
                    + "`livre_id`=?,"
                    + "`Date_Emprunt`=?,"
                    + "`Date_Retour_Prevu`=? WHERE details_emprunts_id = '" + empruntId + "'";
        }
    }

    private void insert() {
        try {
            int livreIdValue = Integer.parseInt(livreId.getText());

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cinLecteur.getText());
            preparedStatement.setString(2, livreId.getText());
            preparedStatement.setDate(3, Date.valueOf(dateEmprunt.getValue()));
            preparedStatement.setDate(4, Date.valueOf(dRP.getValue()));
            preparedStatement.setString(5, null);
            preparedStatement.execute();
            updateDisponible(livreIdValue);
            if (empruntController != null) {
                empruntController.refreshTable();
            }
            afficherSuccess("Emprunt ajouter avec succès");
        } catch (SQLException ex) {
            Logger.getLogger(EmpruntController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateDisponible(int livreIdValue) throws SQLException {
        String updateQuery = "UPDATE livre SET Disponibilite = 0 WHERE code = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setInt(1, livreIdValue);
            updateStatement.executeUpdate();
        }
    }

    private void update() throws SQLException {
        Connection conn = null;
        try {
            conn = ConnexionDb.getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, cinLecteur.getText());
            preparedStatement.setString(2, livreId.getText());
            preparedStatement.setDate(3, Date.valueOf(dateEmprunt.getValue()));
            preparedStatement.setDate(4, Date.valueOf(dRP.getValue()));
            preparedStatement.execute();
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
                        afficherAlerte("Lecteur  n'existe pas!");
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