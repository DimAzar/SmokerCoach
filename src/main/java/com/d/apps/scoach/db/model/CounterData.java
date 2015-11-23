package com.d.apps.scoach.db.model;

import java.sql.Date;

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
@Table (name="CounterData")
@NamedQueries({
})
public class CounterData implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@OneToOne
	@JoinColumn(nullable=false, name="counter_id") 
	private Counter counter;

	@Getter @Setter
	private Double datum;
	
	@Getter @Setter
	private Date addedDate;
}