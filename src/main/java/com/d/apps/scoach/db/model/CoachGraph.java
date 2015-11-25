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
	
	@Getter
	@ManyToOne(targetEntity=CoachInstance.class)
	@JoinColumn(nullable=false)
    private CoachInstance coach;
	
    @Getter
    @OneToMany(targetEntity=Counter.class, fetch=FetchType.EAGER)
    private List<Counter> counters= new ArrayList<Counter>();

}