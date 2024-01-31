package Model;

public class EmployeModel {
	private int employe_id;
	private String nom	;
	private String prenom;
	private String email;	
	private String phone;	
	private String poste;	
	private String mot_de_passe;

	public EmployeModel(int employe_id, String nom, String prenom, String email, String phone, String poste, String mot_de_passe) {
		this.employe_id = employe_id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.phone = phone;
		this.poste = poste;
		this.mot_de_passe = mot_de_passe;
	}
	
	public int getEmploye_id() {
		return employe_id;
	}
	public void setEmploye_id(int employe_id) {
		this.employe_id = employe_id;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMot_de_passe() {
		return mot_de_passe;
	}
	public void setMot_de_passe(String mot_de_passe) {
		this.mot_de_passe = mot_de_passe;
	}

	public String getPoste() {
		return poste;
	}

	public void setPoste(String poste) {
		this.poste = poste;
	}

}
