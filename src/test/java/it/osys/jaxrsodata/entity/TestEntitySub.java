package it.osys.jaxrsodata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "test_entity_sub")
@SuppressWarnings("serial")
public class TestEntitySub implements Serializable {

	@Id
	@Column(name = "id", length = 100, nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "parent")
	private TestEntity parent;

	@Column(name = "string_type_1")
	private String stringType1;

	@Version
	private int version;

	public String getStringType1() {
		return stringType1;
	}

	public void setStringType1(String stringType1) {
		this.stringType1 = stringType1;
	}

	public Long getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "TestEntitySub [id=" + id + ", stringType1=" + stringType1 + ", version=" + version + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestEntitySub other = (TestEntitySub) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
