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
import javax.persistence.OneToMany;
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
    @OneToMany(targetEntity=CoachGraph.class, mappedBy="coach", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<CoachGraph> graphs = new ArrayList<CoachGraph>();

	@Getter @Setter
	@ManyToOne(targetEntity=Profile.class)
	@JoinColumn(nullable=false)
    private Profile profile;

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
	
	public void addGraph(CoachGraph graph) {
		graphs.add(graph);
		graph.setCoach(this);
	}
	
	public void removeCounter(Counter instance) {
		counters.remove(instance);
	}
}