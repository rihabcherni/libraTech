package Model;

public class EtagereModel {
	private int etagere_id;
	private String Nom_Etage;
	private int capacite;
	private int quantite_instantanee;
	
	public EtagereModel(int etagere_id, String nom_Etage, int capacite, int quantite_instantanee) {
		this.etagere_id = etagere_id;
		this.Nom_Etage = nom_Etage;
		this.capacite = capacite;
		this.quantite_instantanee = quantite_instantanee;
	}
	
	public int getEtagere_id() {
		return etagere_id;
	}
	public void setEtagere_id(int etagere_id) {
		this.etagere_id = etagere_id;
	}
	public String getNom_Etage() {
		return Nom_Etage;
	}
	public void setNom_Etage(String nom_Etage) {
		Nom_Etage = nom_Etage;
	}
	public int getCapacite() {
		return capacite;
	}
	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}
	public int getQuantite_instantanee() {
		return quantite_instantanee;
	}
	public void setQuantite_instantanee(int quantite_instantanee) {
		this.quantite_instantanee = quantite_instantanee;
	}
}
