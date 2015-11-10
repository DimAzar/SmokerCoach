package com.d.apps.scoach.db.selectors.base;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseSelector {
	//MODEL
	@PersistenceContext
	protected EntityManager entityManager;

	protected BaseSelector() {}
	
	//GETTERS/SETTERS
	public EntityManager getEntityManager() {	return entityManager;	}
	public void setEntityManager(EntityManager entityManager) {	this.entityManager = entityManager;	}

}
