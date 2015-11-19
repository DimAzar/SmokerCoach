package com.d.apps.scoach.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

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