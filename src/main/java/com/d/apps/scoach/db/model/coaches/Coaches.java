package com.d.apps.scoach.db.model.coaches;

import javax.persistence.Column;
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
@Table (name="Coaches")
@NamedQueries({
	@NamedQuery(name="profileCoaches.getCoachIDByName", query = "SELECT pc.id FROM Coaches pc where pc.name = :name"),
	@NamedQuery(name="profileCoaches.getCoachByName", query = "SELECT pc FROM Coaches pc where pc.name = :name"),
	@NamedQuery(name="profileCoaches.getAllCoaches", query = "SELECT pc FROM Coaches pc "),
})
public class Coaches implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

	@Getter @Setter
	@Column(unique=true, updatable=false)
	private String name;
}