package com.d.apps.scoach.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.d.apps.scoach.db.model.base.DBEntity;

@Entity 
@Table (name="ProfileCoach")
@NamedQueries({
	@NamedQuery(name="profileCoach.getProfileCoaches", query = "SELECT pc FROM ProfileCoach pc where pc.profile.id = :pid"),
	@NamedQuery(name="profileCoach.getAllProfileCoaches", query = "SELECT pc FROM ProfileCoach pc"),
})
public class ProfileCoach implements DBEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Getter @Setter
    @GeneratedValue
    Integer id;

	@Getter @Setter
	@ManyToOne(targetEntity=Profile.class)
	@JoinColumn(nullable= false)
	private Profile profile;

	@ManyToOne(targetEntity=ProfileCoaches.class)
    @Getter @Setter
	private ProfileCoaches coach;
	
	@Getter @Setter
	@Column(nullable=false, updatable=false)
	private Date dateActivated;
}