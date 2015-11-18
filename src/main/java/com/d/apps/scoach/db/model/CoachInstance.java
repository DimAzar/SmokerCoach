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
import javax.persistence.NamedQueries;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="CoachInstance")
@NamedQueries({
})
public class CoachInstance implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@Column(unique=true, updatable=false)
	private String name;
	
	@Getter @Setter
	@OneToOne
	@JoinColumn(nullable=false, name="template_id") 
	private CoachTemplate template;
	
	@Getter
    @ManyToMany(targetEntity=CounterInstance.class, mappedBy="coaches", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
    private List<CounterInstance> counters = new ArrayList<CounterInstance>();
	
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
	
	public void addCounter(CounterInstance instance) {
		counters.add(instance);
	}
	
	public void removeCounter(CounterInstance instance) {
		counters.remove(instance);
	}
	
	public void addProfile(Profile p) {
		profiles.add(p);
	}

	public void removeProfile(Profile p) {
		profiles.remove(p);
	}
}