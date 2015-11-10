package com.d.apps.scoach.db.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity 
@Table (name="CigaretteTrackEntry")
@NamedQueries({
	@NamedQuery(name="cTrackEntry.getAllEntries", query = "SELECT cte FROM CigaretteTrackEntry cte"),
})

public class CigaretteTrackEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue
    Integer pkey;
	
	@ManyToOne
	@JoinColumn(name="id", nullable= false)
	private Profile profile;
	
	public void setProfile(Profile p) {
		profile = p;
	}
}
