package com.d.apps.scoach.db.selectors.base;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.model.base.DBEntity;

public class BaseSelector {
	//MODEL
	@PersistenceContext
	protected EntityManager entityManager;

	protected BaseSelector() {}
	
	//GETTERS/SETTERS
	public EntityManager getEntityManager() {	return entityManager;	}
	public void setEntityManager(EntityManager entityManager) {	this.entityManager = entityManager;	}

    public  void deleteEntity(int id, Class<?> cls) {
    	entityManager.getTransaction().begin();
    	
    	entityManager.remove(entityManager.find(cls, id));
    	
    	entityManager.getTransaction().commit();
    }
    
    public DBEntity updateEntity(DBEntity e) {
    	entityManager.getTransaction().begin();
    	
    	entityManager.merge(e);
    	entityManager.flush();
    	entityManager.getTransaction().commit();
    	return e;
    }


}
