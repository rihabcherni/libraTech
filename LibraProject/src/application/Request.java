package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Exceptions.ValidationException;
import Model.LecteurModel;
import Model.LivreModel;

public class Request {
	
	public static void afficherLivre() {
        String query = "SELECT * FROM livre";
        try (Connection connection = ConnexionDb.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                LivreModel livre = new LivreModel(
                        resultSet.getInt("code"),
                        resultSet.getInt("etagere_id"),
                        resultSet.getString("titre"),
                        resultSet.getString("auteur"),
                        resultSet.getString("categorie"),
                        resultSet.getInt("codeISBN"),
                        resultSet.getInt("Disponibilite"),
                        resultSet.getDate("Date_ajout")
                );
                System.out.println("Code: " + livre.getCode()+"\nTitre: " + livre.getTitre()+"\nAuteur: " + livre.getAuteur()+
                		"\nCode ISBN: " + livre.getCodeISBN()+"\nCatégorie: " + livre.getCategorie()+"\nDisponibilité: "
                		+ livre.getDisponibilite()+"\nDate d'ajout: " + livre.getDate_ajout());
                System.out.println(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	public static void ajouterLivre(LivreModel livre) throws ValidationException {
        String query = "INSERT INTO livre (etagere_id, titre, auteur, codeISBN, categorie, Disponibilite, Date_ajout) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnexionDb.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, livre.getEtagere_id());
            preparedStatement.setString(2, livre.getTitre());
            preparedStatement.setString(3, livre.getAuteur());
            preparedStatement.setInt(4, livre.getCodeISBN());
            preparedStatement.setString(5, livre.getCategorie());
            preparedStatement.setInt(6, livre.getDisponibilite());
            preparedStatement.setDate(7, livre.getDate_ajout());

            preparedStatement.executeUpdate();
            System.out.println("Livre ajouté avec succès à la base de données.");
        } catch (SQLSyntaxErrorException | SQLIntegrityConstraintViolationException e) {
            throw new ValidationException("Erreur lors de l'ajout du livre. Vérifiez les données saisies. "+e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	 public static void supprimerLivre(int codeLivre) {
	        String query = "DELETE FROM livre WHERE code = ?";

	        try (Connection connection = ConnexionDb.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            preparedStatement.setInt(1, codeLivre);

	            int rowsAffected = preparedStatement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Livre supprimé avec succès de la base de données.");
	            } else {
	                System.out.println("Aucun livre trouvé avec le code spécifié.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	 public static void ajouterLecteur(LecteurModel lecteur) throws ValidationException {
	        String query = "INSERT INTO lecteur (cin, nom, prenom, Adresse, Email, Date_Inscription, phone, Date_Naissance, genre) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	        try (Connection connection = ConnexionDb.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            preparedStatement.setInt(1, lecteur.getCin());
	            preparedStatement.setString(2, lecteur.getNom());
	            preparedStatement.setString(3, lecteur.getPrenom());
	            preparedStatement.setString(4, lecteur.getAdresse());
	            preparedStatement.setString(5, lecteur.getEmail());
	            preparedStatement.setDate(6, lecteur.getDate_Inscription());
	            preparedStatement.setString(7, lecteur.getPhone());
	            preparedStatement.setDate(8, lecteur.getDate_Naissance());
	            preparedStatement.setString(9, lecteur.getGenre());

	            preparedStatement.executeUpdate();
	            System.out.println("Lecteur ajouté avec succès à la base de données.");
	        } catch (SQLSyntaxErrorException | SQLIntegrityConstraintViolationException e) {
	            throw new ValidationException("Erreur lors de l'ajout du lecteur. Vérifiez les données saisies. " + e.getMessage());
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 public static void supprimerLecteur(int cinLecteur) {
		    String query = "DELETE FROM lecteur WHERE cin = ?";

		    try (Connection connection = ConnexionDb.getConnection();
		         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

		        preparedStatement.setInt(1, cinLecteur);

		        int rowsAffected = preparedStatement.executeUpdate();
		        if (rowsAffected > 0) {
		            System.out.println("Lecteur supprimé avec succès de la base de données.");
		        } else {
		            System.out.println("Aucun lecteur trouvé avec le CIN spécifié.");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	 public static List<LivreModel> rechercherLivres(String recherche) {
		    List<LivreModel> livres = new ArrayList<>();

		    try (Connection connection = ConnexionDb.getConnection()) {
		        String query = "SELECT * FROM livre";
		        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
		            ResultSet resultSet = preparedStatement.executeQuery()) {
		            String rechercheLowerCase = recherche.toLowerCase();
		            while (resultSet.next()) {
		                LivreModel livre = null;
						try {
							livre = resultSetToLivre(resultSet);
						} catch (Exception e) {
							e.printStackTrace();
						}
		                if (livre.getTitre().toLowerCase().contains(rechercheLowerCase)
		                        || livre.getAuteur().toLowerCase().contains(rechercheLowerCase)
		                        || livre.getTitre().toLowerCase().startsWith(rechercheLowerCase)) {
		                    livres.add(livre);
		                }
		                System.out.println("Code: " + livre.getCode()+"\nTitre: " + livre.getTitre()+"\nAuteur:" + livre.getAuteur()+
		                		"\nCode ISBN: " + livre.getCodeISBN()+"\nCatégorie: " + livre.getCategorie()+"\nDisponibilité: "
		                		+ livre.getDisponibilite()+"\nDate d'ajout:" + livre.getDate_ajout());
		                System.out.println(); 
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return livres;
		}
	 private static LivreModel resultSetToLivre(ResultSet resultSet) throws SQLException {
		    return new LivreModel(
		            resultSet.getInt("code"),
		            resultSet.getInt("etagere_id"),
		            resultSet.getString("titre"),
		            resultSet.getString("auteur"),
		            resultSet.getString("categorie"),
		            resultSet.getInt("codeISBN"),
		            resultSet.getInt("Disponibilite"),
		            resultSet.getDate("Date_ajout")
		    );
		}
	 public static void emprunterLivre(int cinLecteur, int codeLivre, LocalDate dateEmprunt, LocalDate dateRetourPrevu) {
		    try (Connection connection = ConnexionDb.getConnection()) {
		        if (!isLivreDisponible(connection, codeLivre)) {
		            System.out.println("Le livre n'est pas disponible pour l'emprunt.");
		            return;
		        }
		        if (!isLecteurExistant(connection, cinLecteur)) {
		            System.out.println("Le lecteur n'existe pas.");
		            return;
		        }
		        String insertQuery = "INSERT INTO details_emprunt (lecteur_cin, livre_id, Date_Emprunt, Date_Retour_Prevu) VALUES (?, ?, ?, ?)";
		        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
		            insertStatement.setInt(1, cinLecteur);
		            insertStatement.setInt(2, codeLivre);
		            insertStatement.setDate(3, Date.valueOf(dateEmprunt));
		            insertStatement.setDate(4, Date.valueOf(dateRetourPrevu));
		            insertStatement.executeUpdate();
		        }

		        String updateQuery = "UPDATE livre SET Disponibilite = 0 WHERE code = ?";
		        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
		            updateStatement.setInt(1, codeLivre);
		            updateStatement.executeUpdate();
		        }
		        System.out.println("Le livre a été emprunté avec succès.");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

		private static boolean isLivreDisponible(Connection connection, int codeLivre) throws SQLException {
		    String query = "SELECT Disponibilite FROM livre WHERE code = ?";
		    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
		        preparedStatement.setInt(1, codeLivre);
		        try (ResultSet resultSet = preparedStatement.executeQuery()) {
		            if (resultSet.next()) {
		                return resultSet.getInt("Disponibilite") == 1;
		            }
		        }
		    }
		    return false;
		}

		private static boolean isLecteurExistant(Connection connection, int cinLecteur) throws SQLException {
		    String query = "SELECT cin FROM lecteur WHERE cin = ?";
		    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
		        preparedStatement.setInt(1, cinLecteur);
		        try (ResultSet resultSet = preparedStatement.executeQuery()) {
		            return resultSet.next();
		        }
		    }
		}
		public static void retournerLivre(int cinLecteur, int codeLivre, LocalDate dateRetourReel) {
		    try (Connection connection = ConnexionDb.getConnection()) {
		        if (!isLivreEmprunteParLecteur(connection, cinLecteur, codeLivre)) {
		            System.out.println("Le lecteur n'a pas emprunté ce livre.");
		            return;
		        }
		        String updateQuery = "UPDATE details_emprunt SET date_retour_reel = ? WHERE lecteur_cin = ? AND livre_id = ?";
		        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
		            updateStatement.setDate(1, Date.valueOf(dateRetourReel));
		            updateStatement.setInt(2, cinLecteur);
		            updateStatement.setInt(3, codeLivre);
		            updateStatement.executeUpdate();
		        }
		        String updateLivreQuery = "UPDATE livre SET Disponibilite = 1 WHERE code = ?";
		        try (PreparedStatement updateLivreStatement = connection.prepareStatement(updateLivreQuery)) {
		            updateLivreStatement.setInt(1, codeLivre);
		            updateLivreStatement.executeUpdate();
		        }
		        System.out.println("Le livre a été retourné avec succès.");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

		private static boolean isLivreEmprunteParLecteur(Connection connection, int cinLecteur, int codeLivre) throws SQLException {
		    String query = "SELECT * FROM details_emprunt WHERE lecteur_cin = ? AND livre_id = ?";
		    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
		        preparedStatement.setInt(1, cinLecteur);
		        preparedStatement.setInt(2, codeLivre);
		        try (ResultSet resultSet = preparedStatement.executeQuery()) {
		            return resultSet.next();
		        }
		    }
		}

	public static void main(String[] args) {
		System.out.println("********************************************************************************");
		System.out.println("****************                 Liste des livres:              ****************");
		afficherLivre();
		System.out.println("********************************************************************************");
		
		System.out.println("********************************************************************************");
		System.out.println("****************                 Ajouter livre:                 ****************");
		try {
            LivreModel livre = new LivreModel(1, "Nouveau Livre", "Auteur Inconnu", 123456789, "Inconnu", 1, Date.valueOf(LocalDate.parse("2024-01-01")));
            ajouterLivre(livre);
        } catch (ValidationException e) {
            System.err.println("Erreur de données invalides : " + e.getMessage());
        }
		System.out.println("********************************************************************************");
		System.out.println("****************                 Supprimer livre:               ****************");
		supprimerLivre(13);
		System.out.println("********************************************************************************");
		System.out.println("****************                 Ajouter lecteur:               ****************");
		  	LecteurModel lecteur = new LecteurModel();
	        lecteur.setCin(123456789);
	        lecteur.setNom("rania");
	        lecteur.setPrenom("Jean");
	        lecteur.setAdresse("25 Rue de la Librairie");
	        lecteur.setEmail("rania@example.com");
	        lecteur.setDate_Inscription(Date.valueOf("2024-01-01"));
	        lecteur.setPhone("123456784");
	        lecteur.setDate_Naissance(Date.valueOf("1990-01-01"));
	        lecteur.setGenre("masculin");
	        try {
				ajouterLecteur(lecteur);
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		System.out.println("********************************************************************************");
		System.out.println("****************                 Supprimer lecteur:               ****************");
		 int cinLecteurASupprimer = 123456789; 
		    supprimerLecteur(cinLecteurASupprimer);
		System.out.println("********************************************************************************");
		System.out.println("****************                 Rechercher livre:               ****************");
		rechercherLivres("Le");
		System.out.println("********************************************************************************");	
		System.out.println("****************                 emprunter livre:               ****************");
		emprunterLivre(12345678, 2,LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10"));
		System.out.println("********************************************************************************");
		System.out.println("****************                 Retourner livre:               ****************");
		retournerLivre(12345678, 2,LocalDate.parse("2024-01-13"));
	}

}
