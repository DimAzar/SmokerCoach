package com.d.apps.scoach.db.model;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.Utilities.CounterDimension;
import com.d.apps.scoach.Utilities.CounterFunctionType;
import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="Counter")
@NamedQueries({
})
public class Counter implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@Column(unique=true, updatable=false)
	private String name;
	
	@Getter @Setter
	@Column(updatable=false)
	private Double stepValueX;
	
	@Getter @Setter
	@Column(updatable=false)
	private Double stepValueY;

	@Getter @Setter
	@Column(updatable=false)
	private Double stepValueZ;

	@Getter @Setter
	@Column(updatable=false)
	private CounterFunctionType type;
	
	@Getter @Setter
	@Column(updatable=false)
	private CounterDimension dimension;

	@Getter @Setter
	@ManyToOne(targetEntity=Profile.class)
	@JoinColumn(nullable=false)
    private Profile profile;

	@Getter @Setter
    @OneToMany(targetEntity=CounterData.class, mappedBy="counter", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<CounterData> data;

	@Getter
	@ManyToMany
	@JoinColumn(nullable=true, name="graph_id") 
	private List<CoachGraph> graphs;

	public void addDatum(CounterData datum) {
		datum.setCounter(this);
		data.add(datum);
	}
	
	public void addGraph(CoachGraph graph) {
		graphs.add(graph);
	}
	public void removeGraph(CoachGraph graph) {
		graphs.remove(graph);
	}
}