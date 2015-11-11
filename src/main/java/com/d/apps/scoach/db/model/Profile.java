package com.d.apps.scoach.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="Profile")
@NamedQueries({
	@NamedQuery(name="profile.getAllProfiles", query = "SELECT p FROM Profile p"),
})

public class Profile implements DBEntity {
	private static final long serialVersionUID = 1321240600648558754L;
	
	@Id
    @GeneratedValue
    private Integer id;
    private String firstName;
    private boolean active = false;
    
    @OneToMany(targetEntity=CigaretteTrackEntry.class, mappedBy="profile",fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CigaretteTrackEntry> cigaretteTrackEntry = new ArrayList<CigaretteTrackEntry>();

    public List<CigaretteTrackEntry> getCigaretteTrack() {
		return cigaretteTrackEntry;
	}

    public int getSmokeCount(String dateString) {
    	for (CigaretteTrackEntry cigaretteTrackEntry2 : cigaretteTrackEntry) {
			if (cigaretteTrackEntry2.getDateString().equals(dateString)) {
				return cigaretteTrackEntry2.getCigaretteCount();
			}
		}
    	//TODO MAYBE -1 for special treatment ?
    	return 0;
    }
    
    public void addTrackEntry(CigaretteTrackEntry e) {
    	cigaretteTrackEntry.add(e);
    }
    
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}