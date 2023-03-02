package it.osys.jaxrsodata.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class TestEmbeddedEntity {

	private String number;

	private String street;

	private String city;

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

}