package com.d.apps.scoach.db.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="CounterData")
@NamedQueries({
//	@NamedQuery(name="counterData.SumByDay", query = "select DAY(addeddate), sum(datum) from counterdata where counter_id = 4 group by DAY(addeddate)"),
//	@NamedQuery(name="counterData.SumByMonth", query = "select MONTH(addeddate), sum(datum) from counterdata where counter_id = 4 group by MONTH(addeddate)"),
})
public class CounterData implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	private Double datum;
	
	@Getter @Setter
	private Timestamp addedDate;

	@Getter @Setter
	@ManyToOne (targetEntity=Counter.class)
	@JoinColumn(nullable=false, name="counter_id") 
	private Counter counter;
}