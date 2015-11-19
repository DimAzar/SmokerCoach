package com.d.apps.scoach.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
	
	@Getter @Setter	
	@Column(nullable=false)
	private String firstName;
    
	@Getter @Setter
	@Column(nullable=false)
	private boolean isActive = false;

    @Getter
    @ManyToMany(targetEntity=CoachInstance.class, mappedBy="profiles", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<CoachInstance> coaches = new ArrayList<CoachInstance>();

    public void addCoach(CoachInstance profileCoach) {
    	profileCoach.addProfile(this);
    	coaches.add(profileCoach);
    }
    
    public int removeCoach(String name) {
    	for (CoachInstance profileCoach : coaches) {
			if (profileCoach.getName().equals(name)) {
				coaches.remove(profileCoach);
				profileCoach.removeProfile(this);
				return profileCoach.getId();
			}
		}
    	return -1;
    }
}