package org.esupportail.lecture.domain.model;

import java.util.List;

public class ComplexItem extends Item {

//	private List<Rubrique> rubriques;
	private VisibilitySets visibility; 
	private Author author;
	private List<RubriquesPlublisher> rubriques;
	public ComplexItem(Source source) {
		super(source);
	}
	public void setVisibility(VisibilitySets visibilitySets) {
		this.visibility = visibilitySets;
	}
	public VisibilitySets getVisibility() {
		return visibility;
	}
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public  List<RubriquesPlublisher> getRubriques() {
		return rubriques;
	}
	public void setRubriques( List<RubriquesPlublisher>rubriques) {
		this.rubriques = rubriques;
	}

	@Override
	public String toString() {
		return "ComplexItem{" +
				"visibility=" + visibility +
				", author=" + author +
				", rubriques=" + rubriques +
				"} " + super.toString();
	}

	@Override
	public String toStringLight() {
		return "ComplexItem{" +
				"visibility=" + visibility +
				", author=" + author +
				", rubriques=" + rubriques +
				"} " + super.toStringLight();
	}
}
