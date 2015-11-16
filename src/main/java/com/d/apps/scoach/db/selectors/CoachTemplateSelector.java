package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.db.model.CoachTemplate;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CoachTemplateSelector extends BaseSelector {
	public  List<CoachTemplate> getAllCoaches() {
    	return entityManager.createNamedQuery("coachTemplate.getAllCoaches", CoachTemplate.class)
    				.getResultList();
	}
	
	public  CoachTemplate getCoachByName(String name) {
    	return entityManager.createNamedQuery("coachTemplate.getCoachByName", CoachTemplate.class)
    				.setParameter("name", name)
    				.getSingleResult();
	}
	
	public  int getCoachIDByName(String name) {
    	return entityManager.createNamedQuery("coachTemplate.getCoachIDByName", CoachTemplate.class)
    				.setParameter("name", name)
    				.getSingleResult().getId();
	}
	public CoachTemplate createCoach(String name) {
    	entityManager.getTransaction().begin();
    	
    	CoachTemplate ct = new CoachTemplate();
    	ct.setName(name);
    	entityManager.persist(ct);
    	
    	entityManager.getTransaction().commit();
    	
    	return ct;

	}
}