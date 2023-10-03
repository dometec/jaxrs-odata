package it.osys.jaxrsodata.entity;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;

@Embeddable
public class TestEmbeddedEntity {

	private String number;

	private String street;

	private String city;

	@ElementCollection(fetch = FetchType.LAZY)
	@MapKeyJoinColumn(name = "langcode")
	@CollectionTable(name = "embedded_names", joinColumns = @JoinColumn(name = "entity_id", referencedColumnName = "id"))
	@Column(name = "name", length = 50, nullable = false)
	private Map<Language, String> embeddedname;
	
	public TestEmbeddedEntity() {
		this.city = "city";
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Map<Language, String> getName() {
		return embeddedname;
	}

	public void setName(Map<Language, String> name) {
		this.embeddedname = name;
	}
	
	

}