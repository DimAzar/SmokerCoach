package com.d.apps.scoach.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="CounterInstance")
@NamedQueries({
})
public class CounterInstance implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@Column(unique=true, updatable=false)
	private String name;
	
	@Getter @Setter
	@OneToOne(mappedBy="counter")
	private CoachCounter coach;
	
	@Getter @Setter
	@OneToOne
	@JoinColumn(name="template_id") 
	private CounterTemplate template;
	
	@Getter @Setter
	@Column(updatable=false)
	private String step;
	
	@Getter @Setter
	@Column(updatable=false)
	private String type;

	@Getter @Setter
	@Column(updatable=false)
	private String operation;

	@Getter @Setter
	@Column(updatable=false)
	private String function;
}