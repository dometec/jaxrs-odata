package it.osys.jaxrsodata.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import it.osys.jaxrsodata.entity.enums.TestEnumEntity;

@Entity
@Table(name = "test_entity")
@SuppressWarnings("serial")
public class TestEntity implements InternalDateEnable, Serializable {

	@Id
	@Column(name = "id", length = 100, nullable = false)
	private Long id;

	private InternalDate internalDate;

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

	@Column(name = "localtime_type")
	private LocalTime localTimeType;

	@Column(name = "localdate_type")
	private LocalDate localDateType;

	@Column(name = "localdatetime_type")
	private LocalDateTime localDateTimeType;

	@Column(name = "not_implemented_type")
	private Float notImplementedType;

	@Version
	private int version;

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

}
