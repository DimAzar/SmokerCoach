package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.db.model.CounterTemplate;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CounterTemplateSelector extends BaseSelector {
	public  List<CounterTemplate> getAllCounters() {
    	return entityManager.createNamedQuery("counterTemplate.getAllCounters", CounterTemplate.class)
    				.getResultList();
	}
	
	public  CounterTemplate getCounterByName(String name) {
    	return entityManager.createNamedQuery("counterTemplate.getCounterByName", CounterTemplate.class)
    				.setParameter("name", name)
    				.getSingleResult();
	}
	
	public  int getCounterIDByName(String name) {
    	return entityManager.createNamedQuery("counterTemplate.getCounterIDByName", CounterTemplate.class)
    				.setParameter("name", name)
    				.getSingleResult().getId();
	}
	
	public CounterTemplate createCounter(String name) {
    	entityManager.getTransaction().begin();
    	
    	CounterTemplate ct = new CounterTemplate();
    	ct.setName(name);
    	entityManager.persist(ct);
    	
    	entityManager.getTransaction().commit();
    	
    	return ct;
	}
}