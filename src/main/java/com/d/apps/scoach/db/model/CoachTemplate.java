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
@Table (name="CoachTemplate")
@NamedQueries({
	@NamedQuery(name="coachTemplate.getAllCoaches", query = "SELECT ct FROM CoachTemplate ct "),
	@NamedQuery(name="coachTemplate.getCoachByName", query = "SELECT ct FROM CoachTemplate ct where ct.name = :name"),
	@NamedQuery(name="coachTemplate.getCoachIDByName", query = "SELECT ct.id FROM CoachTemplate ct where ct.name = :name"),
})
public class CoachTemplate implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@Column(updatable=true)
	private String name;
	
	@Getter @Setter
    @OneToMany(targetEntity=CoachInstance.class, mappedBy="template", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<CoachInstance> instances;
}