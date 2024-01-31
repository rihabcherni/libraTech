package Model;

import java.sql.Date;

public class EmpruntModel {
	private int details_emprunts_id;
	private int lecteur_cin;
	private int livre_id;
	private Date Date_Emprunt;
	private Date Date_Retour_Prevu;
	private Date date_retour_reel;
	private String nom;
	private String prenom;
	private String titre;
	private String auteur;

	public EmpruntModel(int details_emprunts_id, int lecteur_cin, int livre_id, Date date_Emprunt,
			Date date_Retour_Prevu, String nom, String prenom, String titre, String auteur) {
		super();
		this.details_emprunts_id = details_emprunts_id;
		this.lecteur_cin = lecteur_cin;
		this.livre_id = livre_id;
		Date_Emprunt = date_Emprunt;
		Date_Retour_Prevu = date_Retour_Prevu;
		this.nom = nom;
		this.prenom = prenom;
		this.titre = titre;
		this.auteur = auteur;
	}

	public EmpruntModel(int details_emprunts_id, int lecteur_cin, int livre_id, Date date_Emprunt,
			Date date_Retour_Prevu, String nom, String prenom, String titre, String auteur, Date date_retour_reel) {
		super();
		this.details_emprunts_id = details_emprunts_id;
		this.lecteur_cin = lecteur_cin;
		this.livre_id = livre_id;
		Date_Emprunt = date_Emprunt;
		Date_Retour_Prevu = date_Retour_Prevu;
		this.nom = nom;
		this.prenom = prenom;
		this.titre = titre;
		this.auteur = auteur;
		this.date_retour_reel = date_retour_reel;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public EmpruntModel(int details_emprunts_id, int lecteur_cin, int livre_id, Date date_Emprunt,
			Date date_Retour_Prevu, Date date_retour_reel) {
		this.details_emprunts_id = details_emprunts_id;
		this.lecteur_cin = lecteur_cin;
		this.livre_id = livre_id;
		Date_Emprunt = date_Emprunt;
		Date_Retour_Prevu = date_Retour_Prevu;
		this.date_retour_reel = date_retour_reel;
	}

	public EmpruntModel(int details_emprunts_id, int lecteur_cin, int livre_id, Date date_Emprunt,
			Date date_Retour_Prevu) {
		this.details_emprunts_id = details_emprunts_id;
		this.lecteur_cin = lecteur_cin;
		this.livre_id = livre_id;
		Date_Emprunt = date_Emprunt;
		Date_Retour_Prevu = date_Retour_Prevu;
	}

	public int getDetails_emprunts_id() {
		return details_emprunts_id;
	}

	public void setDetails_emprunts_id(int details_emprunts_id) {
		this.details_emprunts_id = details_emprunts_id;
	}

	public int getLecteur_cin() {
		return lecteur_cin;
	}

	public void setLecteur_cin(int lecteur_cin) {
		this.lecteur_cin = lecteur_cin;
	}

	public int getLivre_id() {
		return livre_id;
	}

	public void setLivre_id(int livre_id) {
		this.livre_id = livre_id;
	}

	public Date getDate_Emprunt() {
		return Date_Emprunt;
	}

	public void setDate_Emprunt(Date date_Emprunt) {
		Date_Emprunt = date_Emprunt;
	}

	public Date getDate_Retour_Prevu() {
		return Date_Retour_Prevu;
	}

	public void setDate_Retour_Prevu(Date date_Retour_Prevu) {
		Date_Retour_Prevu = date_Retour_Prevu;
	}

	public Date getDate_retour_reel() {
		return date_retour_reel;
	}

	public void setDate_retour_reel(Date date_retour_reel) {
		this.date_retour_reel = date_retour_reel;
	}
}
