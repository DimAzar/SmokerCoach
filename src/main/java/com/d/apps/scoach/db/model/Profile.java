package com.d.apps.scoach.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

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
    @Getter @Setter
    private Integer id;
	
	@Column(nullable=false)
	@Getter @Setter	
	private String firstName;
    
	@Column(nullable=false)
	@Getter @Setter
	private boolean isActive = false;

    @Getter
    @OneToMany(targetEntity=ProfileCoach.class, mappedBy="profile", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
    private List<ProfileCoach> profileCoaches = new ArrayList<ProfileCoach>();

    public void addCoach(ProfileCoach profileCoach) {
    	profileCoach.setProfile(this);
    	profileCoaches.add(profileCoach);
    }
    
    public void removeCoach(ProfileCoach profileCoach) {
    	if (profileCoaches.contains(profileCoach)) {
    		profileCoaches.remove(profileCoach);
    		profileCoach.setProfile(null);
    	}
    }
    
    public int removeCoach(String name) {
    	for (ProfileCoach profileCoach : profileCoaches) {
			if (profileCoach.getCoach().getName().equals(name)) {
				profileCoaches.remove(profileCoach);
				return profileCoach.getId();
			}
		}
    	return -1;
    }
}