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
	@Column(nullable=false, unique=true)
	private String firstName;
    
	@Getter @Setter
	@Column(nullable=false)
	private boolean isActive = false;

    @Getter
    @OneToMany(targetEntity=Coach.class, mappedBy="profile", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Coach> coaches = new ArrayList<Coach>();

    @Getter
    @OneToMany(targetEntity=Counter.class, mappedBy="profile", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Counter> counters = new ArrayList<Counter>();

    public void addCoach(Coach profileCoach) {
    	profileCoach.setProfile(this);
    	coaches.add(profileCoach);
    }
    
    public boolean updateCoach(Coach coach) {
    	for (Coach coachInstance : coaches) {
			if (coachInstance.getId() == coach.getId()) {
				coaches.remove(coachInstance);
				coaches.add(coach);
				return true;
			}
		}
    	return false;
    }
    
    public void removeCoach(int cid) {
    	for (Coach profileCoach : coaches) {
			if (profileCoach.getId() == cid) {
				coaches.remove(profileCoach);
				profileCoach.setProfile(null);
				return;
			}
		}
    }
}