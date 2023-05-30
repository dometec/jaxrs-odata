package it.osys.jaxrsodata.entity;

import java.io.Serializable;
import java.util.Locale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.Hibernate;

@Entity
@Table(name = "languages")
@SuppressWarnings("serial")
public class Language implements Serializable {

	@Id
	@Column(length = 3, nullable = false)
	private String code;

	@Column(length = 60, nullable = false)
	private String language;

	@Column(length = 60, nullable = false)
	private String enlanguage;

	public Locale toLocale() {
		return new Locale(code.toLowerCase());
	}

	public String getCode() {
		return code;
	}

	public String getLanguage() {
		return language;
	}

	public String getEnlanguage() {
		return enlanguage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (Hibernate.getClass(this) != Hibernate.getClass(obj))
			return false;
		Language other = (Language) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Language [code=" + code + ", language=" + language + "]";
	}

}
