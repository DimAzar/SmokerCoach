package com.d.apps.scoach.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.Utilities.ChartPlotType;
import com.d.apps.scoach.Utilities.CounterDimension;
import com.d.apps.scoach.Utilities.GraphAxisHigherFunctions;
import com.d.apps.scoach.Utilities.GraphDimensions;
import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="CoachGraph")
@NamedQueries({
})
public class CoachGraph implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@Column(updatable=false, nullable=false)
	private String name;
	
	@Getter @Setter
	@Column(updatable=true, nullable=false)
	private String title = "";
	
	@Getter @Setter
	@Column(updatable=true)
	private String xAxisTitle = "";

	@Getter @Setter
	@Column(updatable=true)
	private String yAxisTitle = "";

	@Getter @Setter
	@Column(updatable=true)
	private boolean showLegend = false;
	
	@Getter @Setter
	@Column(updatable=true)
	private boolean showTooltips = false;

	@Getter @Setter
	@Column(updatable=true, nullable=false)
	private ChartPlotType plotType = ChartPlotType.LINE;

	@Getter @Setter
	@Column(updatable=true, nullable=false)
	private GraphAxisHigherFunctions graphXHFunc = GraphAxisHigherFunctions.NONE; 

	@Getter @Setter
	@Column(updatable=true, nullable=false)
	private GraphAxisHigherFunctions graphYHFunc = GraphAxisHigherFunctions.NONE; 

	@Getter @Setter
	@Column(updatable=true, nullable=false)
	private GraphAxisHigherFunctions graphZHFunc = GraphAxisHigherFunctions.NONE; 

	@Getter @Setter
	@Column(updatable=true, nullable=false)
	private GraphDimensions graphDimension = GraphDimensions.D2GRAPH;

	@Getter @Setter
	@Column(updatable=true, nullable=false)
	private CounterDimension xAxisDataFetch = null;

	@Getter @Setter
	@Column(updatable=true, nullable=false)
	private CounterDimension yAxisDataFetch = null;

	@Getter @Setter
	@Column(updatable=true, nullable=true)
	private CounterDimension zAxisDataFetch = null;

	@Getter @Setter
	@ManyToOne(targetEntity=Coach.class)
	@JoinColumn(nullable=false, name="coach_id")
    private Coach coach;
	
    @Getter
    @ManyToMany(targetEntity=Counter.class, fetch=FetchType.EAGER)
    private List<Counter> counters= new ArrayList<Counter>();
    
	public void addGraphCounter(Counter counter) {
    	counter.addGraph(this);
    	counters.add(counter);
    }
	
	public void removeGraphCounter(Counter counter) {
    	counter.removeGraph(this);
    	counters.remove(counter);
    }
	
	public void removeCounters() {
		for (Counter counter : counters) {
			counter.removeGraph(this);
		}
		counters.clear();
	}
}