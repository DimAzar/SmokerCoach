package com.d.apps.scoach.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="XCoachCounter")
@NamedQueries({
})
public class CoachCounter implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    Integer id;

	@Getter @Setter
	@ManyToOne(targetEntity=CounterInstance.class)
	@JoinColumn(nullable=false)
	private CounterInstance counter;

    @Getter @Setter
	@ManyToOne(targetEntity=CoachInstance.class)
	@JoinColumn(nullable= false)
	private CoachInstance coach;
	
	@Getter @Setter
	@Column(nullable=false, updatable=false)
	private Date dateAdded;
}