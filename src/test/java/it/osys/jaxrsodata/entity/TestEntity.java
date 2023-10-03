package it.osys.jaxrsodata.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Formula;

import it.osys.jaxrsodata.entity.enums.TestEnumEntity;

@Entity
@Table(name = "test_entity")
@SuppressWarnings("serial")
public class TestEntity implements InternalDateEnable, Serializable {

	@Id
	@Column(name = "id", length = 100, nullable = false)
	private Long id;

	private InternalDate internalDate;

	@ElementCollection(fetch = FetchType.LAZY)
	@MapKeyJoinColumn(name = "langcode")
	@CollectionTable(name = "entity_names", joinColumns = @JoinColumn(name = "entity_id", referencedColumnName = "id"))
	@Column(name = "name", length = 50, nullable = false)
	private Map<Language, String> name;

	@Column(name = "enName")
	@Formula("(select en.name from entity_names en where en.entity_id = id and en.langcode = 'EN')")
	private String enName;

	@Column(name = "transName")
	@Formula("(select en.name from entity_names en where en.entity_id = id and en.langcode = '{CURRENT_LANGUAGE}')")
	private String transName;

	@Enumerated(EnumType.STRING)
	@Column(name = "enum_type", nullable = false, length = 20)
	private TestEnumEntity enumType;

	@Column(name = "string_type_1")
	private String stringType1;

	@Column(name = "string_type_2")
	private String stringType2;

	@Column(name = "string_type_3")
	private String stringType3;

	@Column(name = "string_type_4")
	private String stringType4;

	@Column(name = "boolfield")
	private boolean boolfield;

	@Column(name = "localtime_type")
	private LocalTime localTimeType;

	@Column(name = "localdate_type")
	private LocalDate localDateType;

	@Column(name = "localdatetime_type")
	private LocalDateTime localDateTimeType;

	@Column(name = "length")
	private Float length;

	@ElementCollection
	@CollectionTable(name = "test_entity_owners", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "owner")
	private Set<Integer> ownerids;

	private TestEmbeddedEntity address;

	@ManyToOne(fetch = FetchType.LAZY)
	private TestSingleEntitySub subsinglentity;

	@OneToMany(mappedBy = "parent")
	private Set<TestEntitySub> subentities;

	@Version
	private int version;

	public TestEntity() {
		this.ownerids = new HashSet<Integer>();
		this.address = new TestEmbeddedEntity();
	}

	public InternalDate getInternalDate() {
		return internalDate;
	}

	public void setInternalDate(InternalDate internalDate) {
		this.internalDate = internalDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<Language, String> getName() {
		return name;
	}

	public String getEnName() {
		return enName;
	}

	public String getTransName() {
		return transName;
	}

	public TestEnumEntity getEnumType() {
		return enumType;
	}

	public void setEnumType(TestEnumEntity enumType) {
		this.enumType = enumType;
	}

	public String getStringType1() {
		return stringType1;
	}

	public void setStringType1(String stringType1) {
		this.stringType1 = stringType1;
	}

	public String getStringType2() {
		return stringType2;
	}

	public void setStringType2(String stringType2) {
		this.stringType2 = stringType2;
	}

	public String getStringType3() {
		return stringType3;
	}

	public void setStringType3(String stringType3) {
		this.stringType3 = stringType3;
	}

	public String getStringType4() {
		return stringType4;
	}

	public void setStringType4(String stringType4) {
		this.stringType4 = stringType4;
	}

	public LocalTime getLocalTimeType() {
		return localTimeType;
	}

	public void setLocalTimeType(LocalTime localTimeType) {
		this.localTimeType = localTimeType;
	}

	public LocalDate getLocalDateType() {
		return localDateType;
	}

	public void setLocalDateType(LocalDate localDateType) {
		this.localDateType = localDateType;
	}

	public LocalDateTime getLocalDateTimeType() {
		return localDateTimeType;
	}

	public void setLocalDateTimeType(LocalDateTime localDateTimeType) {
		this.localDateTimeType = localDateTimeType;
	}

	public TestEmbeddedEntity getAddress() {
		return address;
	}

	public void setAddress(TestEmbeddedEntity address) {
		this.address = address;
	}

	public Set<TestEntitySub> getSubentities() {
		return subentities;
	}

	public void setSubentities(Set<TestEntitySub> subentities) {
		this.subentities = subentities;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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
		TestEntity other = (TestEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestEntity [longType=" + id + ", internalDate=" + internalDate + ", os=" + enumType + ", stringType1=" + stringType1
				+ ", stringType2=" + stringType2 + ", stringType3=" + stringType3 + ", stringType4=" + stringType4 + ", localTimeType="
				+ localTimeType + ", localDateType=" + localDateType + ", localDateTimeType=" + localDateTimeType + ", version=" + version
				+ "]";
	}

	public Set<Integer> getOwnerids() {
		return ownerids;
	}

	public void setOwnerids(Set<Integer> ownerids) {
		this.ownerids = ownerids;
	}

}
