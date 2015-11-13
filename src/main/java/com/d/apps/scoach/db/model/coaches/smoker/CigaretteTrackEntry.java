package com.d.apps.scoach.db.model.coaches.smoker;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.Profile;
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
    @Getter @Setter
    Integer id;
	
	@ManyToOne
	@JoinColumn(nullable= false)
    @Getter @Setter
    private Profile profile;
	
	@Column(nullable=false)
    @Getter @Setter
    private String dateString;

	@Column(nullable=false)
    @Getter @Setter
    private Integer cigaretteCount;
	
	public void incrementCigaretteCount() {
		cigaretteCount++;
	}
}
