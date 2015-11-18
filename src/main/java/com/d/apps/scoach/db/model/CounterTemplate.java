package com.d.apps.scoach.db.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="CounterTemplate")
@NamedQueries({
	@NamedQuery(name="counterTemplate.getAllCounters", query = "SELECT ct FROM CounterTemplate ct "),
	@NamedQuery(name="counterTemplate.getCounterByName", query = "SELECT ct FROM CounterTemplate ct where ct.name = :name"),
	@NamedQuery(name="counterTemplate.getCounterIDByName", query = "SELECT ct.id FROM CounterTemplate ct where ct.name = :name"),
})
public class CounterTemplate implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@Column(unique=true, updatable=true)
	private String name;
	
	@Getter @Setter
    @OneToMany(targetEntity=CounterInstance.class, mappedBy="template", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE, orphanRemoval=true)
	private List<CounterInstance> instances;
}