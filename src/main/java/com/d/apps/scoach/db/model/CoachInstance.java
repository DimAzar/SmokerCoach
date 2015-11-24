package com.d.apps.scoach.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="CoachInstance")
@NamedQueries({
	@NamedQuery(name="coachInstance.getAllCoachInstances", query = "SELECT ct FROM CoachInstance ct "),
	@NamedQuery(name="coachInstance.getCoachInstanceByID", query = "SELECT ct FROM CoachInstance ct where ct.id = :cid")
})
public class CoachInstance implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@Column(updatable=false)
	private String name;
	
	@Getter @Setter
	@ManyToOne
	@JoinColumn(nullable=false, name="template_id") 
	private CoachTemplate template;
	
	@Getter
    @ManyToMany(targetEntity=Counter.class, mappedBy="coaches", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
    private List<Counter> counters = new ArrayList<Counter>();
	
	@Getter
	@ManyToMany(targetEntity=Profile.class)
	@JoinColumn(nullable=false)
    private List<Profile> profiles = new ArrayList<Profile>();

	//TODO move to counter, action table
	@Getter @Setter
	@Column(updatable=false)
	private String text;
	
	@Getter @Setter
	@Column(updatable=false)
	private String imageName;

	@Getter @Setter
	@Column(updatable=false)
	private String description;

	@Getter @Setter
	@Column(unique=true, updatable=false)
	private Character accelerator;
	
	public void addCounter(Counter instance) {
		instance.addCoach(this);
		counters.add(instance);
	}
	
	public void removeCounter(Counter instance) {
		counters.remove(instance);
	}
	
	public void addProfile(Profile p) {
		profiles.add(p);
	}

	public void removeProfile(Profile p) {
		profiles.remove(p);
	}
}