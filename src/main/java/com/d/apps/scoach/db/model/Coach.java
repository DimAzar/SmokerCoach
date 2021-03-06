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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="Coach")
@NamedQueries({
	@NamedQuery(name="coachInstance.getAllCoachInstances", query = "SELECT ct FROM Coach ct "),
	@NamedQuery(name="coachInstance.getCoachInstanceByID", query = "SELECT ct FROM Coach ct where ct.id = :cid")
})
public class Coach implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@Column(updatable=false, unique=true)
	private String name;
	
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
	
	@Getter @Setter
	@ManyToOne(targetEntity=Profile.class)
	@JoinColumn(nullable=false)
    private Profile profile;

	@Getter
    @OneToMany(targetEntity=CoachGraph.class, mappedBy="coach", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
    private List<CoachGraph> graphs = new ArrayList<CoachGraph>();

	public void addGraph(CoachGraph graph) {
		graphs.add(graph);
		graph.setCoach(this);
	}
	
	public void removeGraph(CoachGraph graph) {
		graphs.remove(graph);
	}
	
	public CoachGraph getGraphById(int id) {
		for (CoachGraph graph : getGraphs()) {
			if (graph.getId() == id) {
				return graph;
			}
		}
		return null;
	}
}