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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
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
	@ManyToOne(targetEntity=Profile.class)
	@JoinColumn(nullable=false, name="template_id") 
	private CoachTemplate template;
	
	@Getter
    @OneToMany(targetEntity=CoachCounter.class, mappedBy="counter", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
    private List<CoachCounter> counters = new ArrayList<CoachCounter>();
	
    @Getter
    @OneToMany(targetEntity=ProfileCoach.class, mappedBy="coach", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
    private List<ProfileCoach> coachProfiles = new ArrayList<ProfileCoach>();

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
}