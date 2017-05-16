package org.esupportail.lecture.domain.model;

public class RubriquesPlublisher {
	
	public RubriquesPlublisher(String name, int uuid, String color, boolean highlight2) {
		this.nom = name;
		this.uid = uuid;
		this.couleur = color;
		this.highlight = highlight2;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getCouleur() {
		return couleur;
	}
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	public boolean isHighlight() {
		return highlight;
	}
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}
	private int uid;
	private String nom;
	private String couleur;
	private boolean highlight;

	@Override
	public String toString() {
		return "RubriquesPlublisher{" +
				"uid=" + uid +
				", nom='" + nom + '\'' +
				", couleur='" + couleur + '\'' +
				", highlight=" + highlight +
				'}';
	}
}
