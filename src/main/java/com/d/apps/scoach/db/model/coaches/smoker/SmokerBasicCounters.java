package com.d.apps.scoach.db.model.coaches.smoker;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="SmokerBasicCounters")
@NamedQueries({
	@NamedQuery(name="smokerBasicCounters.getCounters", query = "SELECT cnt FROM SmokerBasicCounters cnt"),
})

public class SmokerBasicCounters implements DBEntity {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue
    @Getter @Setter
    Integer id;
	
    @Getter @Setter
    private double packPrice;
    
    @Getter @Setter
    private int cigsInPack;
    
    
    
}