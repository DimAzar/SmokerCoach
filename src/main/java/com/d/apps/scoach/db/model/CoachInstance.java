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
@Table (name="CoachInstance")
@NamedQueries({
	@NamedQuery(name="coachInstance.getCoachIDByName", query = "SELECT pc.id FROM CoachInstance pc where pc.name = :name"),
	@NamedQuery(name="coachInstance.getCoachByName", query = "SELECT pc FROM CoachInstance pc where pc.name = :name"),
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
	private CoachTemplate template;
	
	@Getter @Setter
	private ProfileCoach profile; 

	@Getter
    @OneToMany(targetEntity=CoachCounter.class, mappedBy="coach", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
    private List<CoachCounter> counters = new ArrayList<CoachCounter>();
	
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