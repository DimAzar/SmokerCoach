package com.d.apps.scoach.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="CigaretteTrackEntry")
@NamedQueries({
	@NamedQuery(name="cTrackEntry.getAllEntries", query = "SELECT cte FROM CigaretteTrackEntry cte"),
})

public class CigaretteTrackEntry implements DBEntity {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue
    Integer pkey;
	
	@ManyToOne
	@JoinColumn(name="id", nullable= false)
	private Profile profile;
	
	@Column(nullable=false)
	private String dateString;

	@Column(nullable=false)
	private Integer cigaretteCount;
	
	@Override
	public Integer getId() {
		return pkey;
	}

	@Override
	public void setId(Integer pkey) {
		this.pkey = pkey;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public Integer getCigaretteCount() {
		return cigaretteCount;
	}

	public void setCigaretteCount(Integer cigaretteCount) {
		this.cigaretteCount = cigaretteCount;
	}
	
	public void incrementCigaretteCount() {
		cigaretteCount++;
	}
}
