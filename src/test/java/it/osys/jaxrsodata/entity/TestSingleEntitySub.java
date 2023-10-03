package it.osys.jaxrsodata.entity;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "test_singleentity_sub")
@SuppressWarnings("serial")
public class TestSingleEntitySub implements Serializable {

	@Id
	@Column(name = "id", length = 100, nullable = false)
	private Long id;

	@Column(name = "string_type_1")
	private String stringType1;

	@ElementCollection(fetch = FetchType.LAZY)
	@MapKeyJoinColumn(name = "langcode")
	@CollectionTable(name = "test_singleentity_sub_names", joinColumns = @JoinColumn(name = "entity_id", referencedColumnName = "id"))
	@Column(name = "name", length = 50, nullable = false)
	private Map<Language, String> singlesubname;
	
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
		TestSingleEntitySub other = (TestSingleEntitySub) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
