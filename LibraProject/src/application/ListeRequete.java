package application;

public class ListeRequete {
	String q1="INSERT INTO livre (etagere_id, titre, auteur, codeISBN, categorie, Disponibilite, Date_ajout) VALUES ('Nouveau Livre', 'Auteur Inconnu', 123456789, 'Inconnu', 1, '2024-01-01')";

	String q2="SELECT * FROM livre";
	String q3="DELETE FROM livre WHERE code = 12";

	String q4="INSERT INTO lecteur (cin, nom, prenom, Adresse, Email, Date_Inscription, phone, Date_Naissance, genre) VALUES (98765432, 'Nouveau', 'Lecteur', 'Adresse Inconnue', 'nouveau.lecteur@example.com', '2024-01-01', '12345678', '2000-01-01', 'féminin')";

	String q5="DELETE FROM lecteur WHERE cin = 98765432";
	String q6="SELECT * FROM livre WHERE titre LIKE '%le%'";
	String q66="SELECT * FROM livre WHERE titre LIKE '%le%'";
	String q7="SELECT * FROM livre WHERE auteur LIKE '%s%'";
	String q9="INSERT INTO details_emprunt (lecteur_cin, livre_id, Date_Emprunt, Date_Retour_Prevu) VALUES (12345678, 2, '2024-01-01', '2024-01-10')";
	String q10="UPDATE details_emprunt SET date_retour_reel = '2024-01-05' WHERE lecteur_cin = 12345678 AND livre_id = 2";
}
