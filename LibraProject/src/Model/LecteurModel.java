package Model;

import java.sql.Date;

public class LecteurModel {
	private int cin;
	private String nom;
	private String prenom;
	private String Adresse;
	private String Email;
	private String phone;
	private String genre;
	private Date Date_Inscription;
	private Date Date_Naissance;
	public LecteurModel(int cin, String nom, String prenom, String adresse, String email, String phone, String genre, Date date_Inscription, Date date_Naissance) {
		this.cin = cin;
		this.nom = nom;
		this.prenom = prenom;
		Adresse = adresse;
		Email = email;
		this.phone = phone;
		this.genre = genre;
		Date_Inscription = date_Inscription;
		Date_Naissance = date_Naissance;
	}
	public LecteurModel() {
	}
	public int getCin() {
		return cin;
	}
	public void setCin(int cin) {
		this.cin = cin;
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
	public String getAdresse() {
		return Adresse;
	}
	public void setAdresse(String adresse) {
		Adresse = adresse;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public Date getDate_Inscription() {
		return Date_Inscription;
	}
	public void setDate_Inscription(Date date_Inscription) {
		Date_Inscription = date_Inscription;
	}
	public Date getDate_Naissance() {
		return Date_Naissance;
	}
	public void setDate_Naissance(Date date_Naissance) {
		Date_Naissance = date_Naissance;
	}
}
