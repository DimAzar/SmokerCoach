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
	@Column(updatable=false)
	private String name;
	
	@Getter @Setter
	@ManyToOne(targetEntity=CoachInstance.class)
	@JoinColumn(nullable=false)
    private CoachInstance coach;
	
    @Getter
    @ManyToMany(targetEntity=Counter.class, fetch=FetchType.EAGER)
    private List<Counter> counters= new ArrayList<Counter>();
    
    public void addGraphCounter(Counter counter) {
    	counter.addGraph(this);
    	counters.add(counter);
    }

}