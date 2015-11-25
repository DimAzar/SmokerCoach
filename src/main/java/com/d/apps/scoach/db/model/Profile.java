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
	
	@Getter @Setter	
	@Column(nullable=false)
	private String firstName;
    
	@Getter @Setter
	@Column(nullable=false)
	private boolean isActive = false;

    @Getter
    @OneToMany(targetEntity=CoachInstance.class, mappedBy="profile", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<CoachInstance> coaches = new ArrayList<CoachInstance>();

    public void addCoach(CoachInstance profileCoach) {
    	profileCoach.setProfile(this);
    	coaches.add(profileCoach);
    }
    
    public boolean updateCoach(CoachInstance coach) {
    	for (CoachInstance coachInstance : coaches) {
			if (coachInstance.getId() == coach.getId()) {
				coaches.remove(coachInstance);
				coaches.add(coach);
				return true;
			}
		}
    	return false;
    }
    
    public int removeCoach(String name) {
    	for (CoachInstance profileCoach : coaches) {
			if (profileCoach.getTemplate().getName().equals(name)) {
				coaches.remove(profileCoach);
				profileCoach.setProfile(null);
				return profileCoach.getId();
			}
		}
    	return -1;
    }
}