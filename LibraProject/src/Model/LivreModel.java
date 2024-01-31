package Model;

import java.sql.Date;

public class LivreModel {
	private int code;
	private String titre;
	private String auteur;
	private int etagere_id;
	private int codeISBN;
	private int Disponibilite;
	private String categorie;
	private Date Date_ajout;
	public LivreModel(int code, int etagere_id, String titre, String auteur, String categorie, int codeISBN, int Disponibilite, Date Date_ajout) {
		super();
		this.code = code;
		this.titre = titre;
		this.auteur = auteur;
		this.etagere_id = etagere_id;
		this.codeISBN = codeISBN;
		this.categorie = categorie;
		this.Disponibilite = Disponibilite;
		this.Date_ajout = Date_ajout;
	}
	public LivreModel(int etagere_id,String titre, String auteur, int isbn, String categorie, int  Disponibilite, Date d) {
		this.titre = titre;
		this.auteur = auteur;
		this.etagere_id = etagere_id;
		this.codeISBN = isbn;
		this.categorie = categorie;
		this.Disponibilite = Disponibilite;
		this.Date_ajout = d;	
	}
	public Date getDate_ajout() {
		return Date_ajout;
	}
	public void setDate_ajout(Date date_ajout) {
		Date_ajout = date_ajout;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
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
	public int getEtagere_id() {
		return etagere_id;
	}
	public void setEtagere_id(int etagere_id) {
		this.etagere_id = etagere_id;
	}
	public int getCodeISBN() {
		return codeISBN;
	}
	public void setCodeISBN(int codeISBN) {
		this.codeISBN = codeISBN;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public int getDisponibilite() {
		return Disponibilite;
	}
	public void setDisponibilite(int Disponibilite) {
		this.Disponibilite = Disponibilite;
	}	
}
