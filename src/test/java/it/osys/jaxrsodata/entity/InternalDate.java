package it.osys.jaxrsodata.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class InternalDate implements Serializable {

	@Column(name = "insert_date", nullable = false)
	private LocalDateTime insertDate;

	@Column(name = "last_update")
	private LocalDateTime lastUpdate;


	public LocalDateTime getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(LocalDateTime insertDate) {
		this.insertDate = insertDate;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "InternalDate [insertdate=" + insertDate + ", lastupdate=" + lastUpdate + "]";
	}

}
