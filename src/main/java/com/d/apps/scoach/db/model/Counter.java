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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

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
	
	@Getter
	@ManyToMany(targetEntity=CoachInstance.class)
	@JoinColumn(nullable=false)
    private List<CoachInstance> coaches = new ArrayList<CoachInstance>();

	@Getter @Setter
	@ManyToOne
	@JoinColumn(nullable=true, name="graph_id") 
	private CoachGraph graph;

	@Getter @Setter
    @OneToMany(targetEntity=CounterData.class, mappedBy="counter", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<CounterData> data;

	@Getter @Setter
	@Column(updatable=false)
	private Double stepValue;
	
	@Getter @Setter
	@Column(updatable=false)
	private CounterFunctionType type;
	
	public void addCoach(CoachInstance ci) {
		coaches.add(ci);
	}
	
	public void addDatum(CounterData datum) {
		datum.setCounter(this);
		data.add(datum);
	}
}